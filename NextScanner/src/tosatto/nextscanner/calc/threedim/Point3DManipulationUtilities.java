package tosatto.nextscanner.calc.threedim;

public class Point3DManipulationUtilities {
	
	public static double [][] getXRotationMatrix (double XAngle)
	{
	double [][] rX;
	
		rX = new double[3][3];
		
		rX[0][0] = 1; rX[0][1] = 0; rX[0][2] = 0;
		rX[1][0] = 0; rX[1][1] = Math.cos(XAngle); rX[1][2] = -Math.sin(XAngle);
		rX[2][0] = 0; rX[2][1] = Math.sin(XAngle); rX[2][2] = Math.cos(XAngle);
		
		return rX;
	}
	
	public static double [][] getYRotationMatrix (double YAngle)
	{
	double [][] rY;
	
		rY = new double[3][3];
		
		rY[0][0] = Math.cos(YAngle); rY[0][1] = 0; rY[0][2] = Math.sin(YAngle);
		rY[1][0] = 0; rY[1][1] = 1; rY[1][2] = 0;
		rY[2][0] = -Math.sin(YAngle); rY[2][1] = 0; rY[2][2] = Math.cos(YAngle);
		
		return rY;
	}
	
	//Calcola la matrice di rotazione sull'asse Z §2.3
	public static double [][] getZRotationMatrix (double ZAngle)
	{
	double [][] rZ;
	
		rZ = new double[3][3];
		
		rZ[0][0] = Math.cos(ZAngle); rZ[0][1] = -Math.sin(ZAngle); rZ[0][2] = 0;
		rZ[1][0] = Math.sin(ZAngle); rZ[1][1] = Math.cos(ZAngle); rZ[1][2] = 0;
		rZ[2][0] = 0; rZ[2][1] = 0; rZ[2][2] = 1;
		
		return rZ;
	}
	
	//Esegue il prodotto tra matrici
	public static double[][] multiplyMatrix (double [][] m1, double[][] m2)
	{
	double [][] ris;
	
		int rows1 = m1.length;
		int columns1 = m1[0].length;
		
		int rows2 = m2.length;
		int columns2 = m2[0].length;
		
	
		ris = new double [rows1][columns2];
		
		for (int i = 0; i < rows1; i++)
		{
			for (int j = 0; j < columns2; j++)
			{
				ris[i][j] = 0;
				for (int k = 0; k < columns1; k++)
				{
					ris	[i][j] += m1[i][k]*m2[k][j];
				}
			}
		}
		
		return ris;
	}
	
	//Ruota un punto
	public static Point3D rotateZ (Point3D p, double angle)
	{
		return new Point3D (multiplyMatrix(getZRotationMatrix(angle), p.getVector()));
	}
	
	public static Point3D rotateX (Point3D p, double angle)
	{
		return new Point3D (multiplyMatrix(getXRotationMatrix(angle), p.getVector()));
	}
	
	public static Point3D rotateY (Point3D p, double angle)
	{
		return new Point3D (multiplyMatrix(getYRotationMatrix(angle), p.getVector()));
	}
}
