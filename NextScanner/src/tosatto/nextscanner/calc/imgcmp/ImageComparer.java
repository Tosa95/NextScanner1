package tosatto.nextscanner.calc.imgcmp;

import java.awt.image.BufferedImage;

import tosatto.nextscanner.imaging.ImageFiltering;
import tosatto.nextscanner.imaging.ImagingUtilities;
import tosatto.nextscanner.main.settings.SettingsManager;
import tosatto.nextscanner.calc.threedim.*;

public class ImageComparer {
	public static int MIN_DIFF = 40;
	
	public static int[] compare (BufferedImage cmp1, BufferedImage cmp2, BufferedImage ris, double resolution)
	{
		int height = cmp1.getHeight();
		int width = cmp2.getWidth();
		VerticalIndexer vI = new VerticalIndexer(height, resolution);
		int [] pos = new int[vI.getNum()];
		
		vI.reset();
		
		for (int k = 0; !vI.complete(); k++, vI.next())
		{
			
			int max = -1, best = -1;
			
			int i = vI.act();
			
			for (int j = 0; j<width; j++)
			{
				
				//Recupera il colore del pixel (j, i) sulla prima immagine
				int c1Red = cmp1.getRGB(j, i) & 0x000000ff;
				int c1Green = (cmp1.getRGB(j, i) & 0x0000ff00) >> 8;
			    int c1Blue = (cmp1.getRGB(j, i) & 0x00ff0000) >> 16;
				
			    //Recupera il colore del pixel (j, i) sulla prima immagine
				int c2Red = cmp2.getRGB(j, i) & 0x000000ff;
				int c2Green = (cmp2.getRGB(j, i) & 0x0000ff00) >> 8;
				int c2Blue = (cmp2.getRGB(j, i) & 0x00ff0000) >> 16;
				
				//Calcola le due medie
				int m1 = (c1Red + c1Green + c1Blue)/3;
				int m2 = (c2Red + c2Green + c2Blue)/3;
				
				//Calcolo la differenza (in valore assoluto)
				int diff = Math.abs(m1 - m2);
				
				//Se inferiore al valore minimo, la anullo
				if (diff < MIN_DIFF) diff = 0;
				
				//Prendo la differenza massima
				//Trascuro le differenze nulle in modo da rilevare le righe
				//che non contengono la linea laser
				if (diff > max && diff > MIN_DIFF)
				{
					max = diff;
					best = j;
				}
				
				//Compongo l'immagine risultato, settando il pixel (j,i)
				//Con il colore getRGB(diff, diff, diff)
				ris.setRGB(j, i, ImagingUtilities.getRGB(diff, diff, diff));
			}
			
			//Se è stata trovata la linea su questa riga
			if (max > -1)
			{
				//Setto a rosso il pixel corrispondente
				ris.setRGB(best, i, ImagingUtilities.getRGB(255, 0, 0));
			}
			
			//Inserisco i valori nel risultato
			pos[k] = best;
		}
		
		return pos;
	}
}
