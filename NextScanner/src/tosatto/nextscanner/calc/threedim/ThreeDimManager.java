package tosatto.nextscanner.calc.threedim;

import java.awt.Point;

import tosatto.geometry.GeometryPoint;
import tosatto.nextscanner.main.MainWindow;
import tosatto.nextscanner.main.notifier.EventCategory;
import tosatto.nextscanner.main.notifier.Notifier;
import tosatto.nextscanner.main.settings.SettingsManager;
import tosatto.nextscanner.ui.ogl.*;

public class ThreeDimManager {
	
	private static final double CAM_FOV = (double)SettingsManager.get().getValue("CAM_FOV"); 
	private static final double CAM_B = (double)SettingsManager.get().getValue("CAM_B"); 
	private static final double CAM_D = (double)SettingsManager.get().getValue("CAM_D"); 
	private static final GeometryPoint CAM_POS = new GeometryPoint((double)SettingsManager.get().getValue("CAM_X"), (double)SettingsManager.get().getValue("CAM_Y"), (double)SettingsManager.get().getValue("CAM_Z"));
	
	private int w, h, pN, actPos, vS;
	
	private Point3DPool P3DPool;
	
	private ICalculator pc;
	
	private Renderer r;
	
	private double step, actAngle, res;
	
	private VerticalIndexer vI;
	
	public void reset ()
	{
		P3DPool = new Point3DPool(pN, vS);
		
		//pc = new PositionCalculator(CAM_FOV, CAM_B, CAM_D, w, h);
		
		pc = new GeometryCalculator(CAM_FOV, w, h, CAM_POS, MainWindow.gSpace);
		
		MainWindow.gRenderer.addSceneCreator((GeometryCalculator)pc);
		
		actPos = 0;
		actAngle = 0;
		
		r.clear();
	}
	
	public ThreeDimManager (int width, int height, int posNum, double res)
	{
		w = width;
		h = height;
		pN = posNum;
		
		r = new Renderer();
		
		reset();
		step = Math.toRadians(360)/posNum;
		
		vI = new VerticalIndexer(h, res);
		
		this.res = res;
		
		vS = vI.getNum();
	}
	
	public ThreeDimManager ()
	{
		this ((int)SettingsManager.get().getValue("WCAM_WIDTH"), (int)SettingsManager.get().getValue("WCAM_HEIGHT"),(int)SettingsManager.get().getValue("STEPS"),  (double)SettingsManager.get().getValue("RESOLUTION"));
	}
	
	public Renderer getRenderer()
	{
		return r;
	}
	
	
	private Point3D getPoint (int val, int height)
	{	
		return pc.calcPosition(height, val) ;
	}
	
	//Limita il punto come descritto in §2.6.1
	private Point3D limitPoint (Point3D p, double maxH)
	{
		//Recupera le coordinate (per comodità)
		double x = p.getX();
		double y = p.getY();
		double z = p.getZ();
		
		//Limitazione inferiore di altezza
		if (z < (double)SettingsManager.get().getValue("Z_MIN"))
		{
			x = 0;
			z = (double)SettingsManager.get().getValue("Z_MIN");
		}
		
		//Limitazione superiore di altezza
		if (z > maxH)
		{
			x = 0;
			z = maxH;
		}
		
		//Fa collassare i punti con coordinate negative 
		//(di sicuro sbagliati)
		if (x < 0) x = 0;
		if (y < 0) x = 0;
		
		//Trasla verso l'alto in modo da ottenere l'oggetto perfettamente
		//appoggiato sul piano coordinato xy
		z -= (double)SettingsManager.get().getValue("Z_MIN");
		
		//Ritorna il punto limitato
		return new Point3D(x, y, z);
	}
	
	//Aggiunge un profilo al pool di punti
	public void addStepData (int []values)
	{
		
			//Ripara la linea laser
			repairLine (values);
			
			//Calcola l'altezza massima
			double maxH = calcMaxHeight(values);
		
			//Ciclo che inserisce ogni punto nel pool (rispetta la risoluzione verticale assegnata)
			
			vI.reset();
			
			for (int i = 0; i < vS; i++, vI.next())
			{
				//Calcola le coordinate spaziali
				Point3D act = getPoint(values[i], vI.act());
				
				//Limita il punto, utilizzando l'altezza
				//calcolata in precedenza
				act = limitPoint(act, maxH);
				
				//Ruota il punto dell'angolazione corretta §2.3
				Point3D rot = Point3DManipulationUtilities.rotateZ(act, actAngle);
				
				P3DPool.insertPoint(rot);
			}
			
			//Aggiorna l'angolo di rotazione per il prossimo inserimento
			actAngle += step;
			
			if (actPos > 0)
				for (int i = 1; i < P3DPool.getSamples() - 1; i++)
				{
					Point3D[] v = {P3DPool.getPoint(actPos, i), P3DPool.getPoint(actPos - 1, i), P3DPool.getPoint(actPos, i+1)};
					r.AddFace(v);
					Point3D[] v1 = {P3DPool.getPoint(actPos - 1, i), P3DPool.getPoint(actPos, i+1), P3DPool.getPoint(actPos - 1, i+1)};
					r.AddFace(v1);
				}
			
			//r.display();
			
			actPos++;

	}
	
	
	public Point3DPool getPool ()
	{
		return P3DPool;
	}
	
	public double getZ (Point p)
	{
		double z = pc.calcPosition((int)p.getY(), (int)p.getX()).getZ();
		
		return z;
	}
	
	//Ripara la linea laser §2.6.2
	public void repairLine (int [] line)
	{
		//Se il primo punto è indeterminato, lo
		//suppone al centro
		if (line[0] == -1) line[0] = w/2;
		
		//Cicla sull'intera linea, eccezion fatta
		//per il primo punto
		for (int i = 1; i < line.length; i++)
		{
			//Se il punto è indeterminato, assegna la posizione
			//del punto precedente
			if (line [i] == -1) line[i] = line[i-1];
		}
	}
	
	//Calcola l'altezza massima del profilo attuale §2.6.1
	public double calcMaxHeight (int[] values)
	{
		VerticalIndexer vIndex = new VerticalIndexer(h, res);
		
		//Cicla sulla linea partendo dall'alto
		for (int i = 0; i < vIndex.getNum(); vIndex.next(), i++)
		{
			//Ritorna la Z del primo pixel con scostamento dal
			//centro maggiore di 0
			if ((w - values[i])-w/2 > 0)
			{
				Point3D p = getPoint(values[i], vIndex.act());
				
				return p.getZ();
			}
		}
		
		return 0;
	}
}
