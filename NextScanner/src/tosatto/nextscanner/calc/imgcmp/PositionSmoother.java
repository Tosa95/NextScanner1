package tosatto.nextscanner.calc.imgcmp;

import java.awt.image.BufferedImage;

public class PositionSmoother {
	
	public static double[] getGaussianVector (int sz, double k, double var)
	{
	double[] ris = new double[sz];

	int i = sz/2;
	
		for (int j = 0; j < sz; j++)
		{
			int rI = i - (sz/2);
			int rJ = j - (sz/2);
			
			ris[j] = k * (Math.exp(-((rI*rI+rJ*rJ)/(2*var*var))));
		}
	
		return ris;
	}
	
	static int computePosition (int pos[], double[] gVect, int i)
	{
	int sz = gVect.length / 2;
	long sum = 0, div = 0;
	
		for (int k1 = 0; k1 < gVect.length; k1++)
		{
			int aI = (k1-sz) + i;
				
				if (aI >= 0 && aI < pos.length)
				{
				
					div += gVect[k1];
					
					sum += pos[aI] * gVect[k1];

				}	
		}
		
		return (int) (sum/div);
	}
	
	static int [] smoothPosition (int[] pos, int sz, double k, double var)
	{
		double [] gVect = getGaussianVector(sz, k, var);
		int [] ris = new int[pos.length];
		
		for (int i = 0; i < pos.length; i++)
			ris[i] = computePosition (pos, gVect, i);
		
		return ris;
	}
	
}


