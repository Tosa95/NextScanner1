package tosatto.nextscanner.main;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;

import tosatto.nextscanner.calc.imgcmp.ImageComparer;
import tosatto.nextscanner.calc.threedim.ObjFileCreator;
import tosatto.nextscanner.calc.threedim.Point3D;
import tosatto.nextscanner.calc.threedim.Point3DManipulationUtilities;
import tosatto.nextscanner.calc.threedim.PositionCalculator;
import tosatto.nextscanner.calc.threedim.ThreeDimManager;
import tosatto.nextscanner.calc.threedim.VerticalIndexer;
import tosatto.nextscanner.hardwarecom.HardwareManager;
import tosatto.nextscanner.hardwarecom.INewFrameListener;
import tosatto.nextscanner.hardwarecom.IWebcam;
import tosatto.nextscanner.hardwarecom.IWebcamListener;
import tosatto.nextscanner.hardwarecom.SerialControl;
import tosatto.nextscanner.hardwarecom.usbWebCam;
import tosatto.nextscanner.imaging.*;
import tosatto.nextscanner.main.notifier.EventCategory;
import tosatto.nextscanner.main.notifier.INotificationListener;
import tosatto.nextscanner.main.notifier.Notifier;
import tosatto.nextscanner.main.settings.SettingsManager;
import tosatto.nextscanner.ui.ogl.MyJoglCanvas;
import tosatto.nextscanner.ui.ogl.Renderer;
import tosatto.nextscanner.calc.threedim.*;



public class ScanningThread extends Thread implements INotificationListener{
	
private static final int STEPS = (int)SettingsManager.get().getValue("STEPS");
private static final double RESOLUTION = (double)SettingsManager.get().getValue("RESOLUTION");
private static final double VERT_RES = (double)SettingsManager.get().getValue("VERT_RES");
private static final double REAL_STEPS = (STEPS/100.0)*RESOLUTION;
private static final int REV_PER_STEP = (int)(STEPS/REAL_STEPS);
	
private boolean run = true, finished = false;
int actPos = 0;
	
MainWindow main;
ThreeDimManager TDM = new ThreeDimManager(HardwareManager.WCAM_WIDTH, HardwareManager.WCAM_HEIGHT, (int)REAL_STEPS, VERT_RES);

	public ScanningThread(MainWindow mw)
	{
		super();
		
		Notifier.get().addListener(this, new EventCategory("webcam:frame"));
		
		main = mw;
	}
	
	@Override
	public void run()
	{
		//Dichiarazioni iniziali
		finished = false;
		int[] values = null;
		actPos = 0;
		

		//Finchè è attivo
		while(run)
		{
			BufferedImage pic1, pic2, picRis;
			
			HardwareManager HM = HardwareManager.get();
			ImageManager IM = ImageManager.get();
			
			//Cattura i due frame attuali
			HM.captureAct();
			
			//A seconda della precisione desiderata, 
			//eseue un numero variabile di step del motorino passo-passo
			for (int r = 0; r < REV_PER_STEP; r++)
			{
				HM.rotate();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			//Recupera i due frame
			pic1 = ImagingUtilities.deepCopy(HM.getLaserOFFPic());
			pic2 = ImagingUtilities.deepCopy(HM.getLaserONPic());
			
			//Filtra per la riduzione del rumore
			pic1 = IM.reduceNoise(pic1);
			pic2 = IM.reduceNoise(pic2);
			
			picRis = new BufferedImage(pic1.getWidth(), pic1.getHeight(), BufferedImage.TYPE_INT_RGB);
			
			//Esegue il confronto
			values = ImageComparer.compare(pic1, pic2, picRis, (double)SettingsManager.get().getValue("VERT_RES"));
			
			//Inserisce i valori nel pool
			TDM.addStepData(values);
			
			//Imposta l'interfaccia grafica
			main.pRis.setImage(picRis);
		    
		    actPos++;
		    
		    main.ScanningProgress.setValue((int)((actPos*100)/REAL_STEPS));
		    
		    //Se ha già eseguito l'ultimo step, si ferma		    
		    if (actPos >= (int)REAL_STEPS)
		    	run = false;
		}
		
		Path path = Paths.get("test.obj");
		
		if (actPos >= (int)REAL_STEPS)
		    try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)){
		    	ObjFileCreator.toObj(writer, TDM.getPool());
		    	
		    	writer.close();
		    } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    
	    finished = true;
	    
	    //Avvisa l'interfaccia che la scansione è completata
	    main.scanningFinished();
	}
	
	public void finish()
	{
		run = false;
	}
	
	public void start()
	{
		run = true;
		TDM.reset();
		
		if (!super.isAlive())
			super.start();
	}
	
	public boolean finished()
	{
		return finished;
	}
	
	protected ThreeDimManager getTDM()
	{
		return TDM;
	}

	@Override
	public void eventRaised(String eName, EventCategory eCat, Object eData) {
		
		
		
	}
	

}
