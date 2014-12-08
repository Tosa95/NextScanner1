package tosatto.nextscanner.calc.threedim;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ObjFileCreator {
	
	//Crea il file .obj
	public static void toObj (BufferedWriter fW, Point3DPool pool)
	{
		//Lista delle linee
		List<String> lines = new ArrayList<String>();
		
		//Cicla su tutti i profili
		for (int j = 0; j < pool.getSteps(); j++)
		{
			//Cicla su tutti i punti del profilo
			for (int i = 0; i < pool.getSamples(); i++)
			{
				//Recupera le coordinate
				double x = pool.getPoint(j, i).getX();
				double y = pool.getPoint(j, i).getY();
				double z = pool.getPoint(j, i).getZ();
						
				//Crea la linea per il vertice attuale
				lines.add("v " + Double.toString(x) + " " + Double.toString(z) + " " + Double.toString(y) + " 1.0");
	
			}
		}
		
		//Cicla su tutti i profili, tranne l'ultimo
		for (int j = 0; j < pool.getSteps() - 1; j++)
		{
			//Cicla su tutti i punti, tranne l'ultimo
			for (int i = 1; i < pool.getSamples() - 1; i++)
			{
					//Crea il primo triangolo §2.4
					lines.add("f " + Integer.toString(i + j*pool.getSamples()) + " " + Integer.toString(i + (j+1)*pool.getSamples()) + " " + Integer.toString(i + j*pool.getSamples() + 1));
					//Crea il secondo triangolo §2.4
					lines.add("f " + Integer.toString(i + (j+1)*pool.getSamples()) + " " + Integer.toString(i + (j)*pool.getSamples() + 1) + " " + Integer.toString(i + (j+1)*pool.getSamples() + 1));
			}
		}
		
		int j = pool.getSteps() - 1;
		
		//Collega il primo profilo con l'ultimo		
		for (int i = 1; i < pool.getSamples() - 1; i++)
		{
				lines.add("f " + Integer.toString(i) + " " + Integer.toString(i + (j)*pool.getSamples()) + " " + Integer.toString(i + 1));
				lines.add("f " + Integer.toString(i + (j)*pool.getSamples()) + " " + Integer.toString(i  + 1) + " " + Integer.toString(i + (j)*pool.getSamples() + 1));
		}
		
		//Scrive ogni linea sul file
		for(String line : lines){
	        try {
	        	fW.write(line);
				fW.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
	}
	
	
	
}
