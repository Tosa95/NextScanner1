package tosatto.nextscanner.imaging;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageFiltering {
	
	static final int R = 2, G = 1, B = 0;
	
	public static double[][] getGaussianMatrix (int sz, double k, double var)
	{
	double[][] ris = new double[sz][sz];
	
		for (int i = 0; i < sz; i++)
		{
			for (int j = 0; j < sz; j++)
			{
				int rI = i - (sz/2);
				int rJ = j - (sz/2);
				
				ris[i][j] = k * (Math.exp(-((rI*rI+rJ*rJ)/(2*var*var))));
			}
		}
	
		return ris;
	}
	
	public static int[][] getGaussianMatrixInt (int sz, double k, double var)
	{
	int[][] ris = new int[sz][sz];
	
		double[][]gauss = getGaussianMatrix (sz, 1, var);
		
		for (int i = 0; i < sz; i++)
		{
			for (int j = 0; j < sz; j++)
			{
				
				ris[i][j] = (int)(k * gauss[i][j]);
			}
		}
	
		return ris;
	}
	
	static int extractColor (int color, int channel)
	{
		return (color >> (channel*8)) & 0xff;
	}
	
	static int getRGB (int r, int g, int b)
	{
	int ris = 0;
	
		ris += (r << 16) 	& 	0x00ff0000;
		ris += (g << 8) 	& 	0x0000ff00;
	    ris += (b) 			& 	0x000000ff;
	
		return ris;
	}
	
	static int computeColor (BufferedImage img, int[][] gMtrx, int i, int j)
	{
	int ris = 0, sz = gMtrx.length / 2;
	long sumR = 0, sumG = 0, sumB = 0, div = 0;
	
		for (int k1 = 0; k1 < gMtrx.length; k1++)
		{
			for (int k2 = 0; k2 < gMtrx.length; k2++)
			{
			int aI = (k1-sz) + i;
			int aJ = (k2-sz) + j;
				
				if (aI >= 0 && aI < img.getHeight() && aJ >= 0 && aJ < img.getWidth())
				{
				int actColor = img.getRGB(aJ, aI);
				
					div += gMtrx[k1][k2];
					
					sumR += extractColor(actColor, R) * gMtrx[k1][k2];
					sumG += extractColor(actColor, G) * gMtrx[k1][k2];
					sumB += extractColor(actColor, B) * gMtrx[k1][k2];
					
				}
				
				
				
			}
			
			
		}
	
		return getRGB ((int)(sumR/div), (int)(sumG/div), (int)(sumB/div));
	}
	
	public static BufferedImage filterImage (BufferedImage img, int[][] gMtrx)
	{
	BufferedImage ris = new BufferedImage (img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
	
		for (int i = 0; i < img.getHeight(); i++)
		{
			for (int j = 0; j < img.getWidth(); j++)
			{
				ris.setRGB(j,  i, computeColor (img, gMtrx, i, j));
			}
		}
	
		return ris;
	}
}
