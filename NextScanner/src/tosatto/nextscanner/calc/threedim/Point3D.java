package tosatto.nextscanner.calc.threedim;

public class Point3D {
	private double x, y, z;
	
	public Point3D ()
	{
		x = 0;
		y = 0; 
		z = 0;
	}
	
	public Point3D (Point3D p)
	{
		x = p.x;
		y = p.y;
		z = p.z;
	}
	
	public Point3D (double px, double py, double pz)
	{
		x = px;
		y = py;
		z = pz;
	}
	
	public Point3D (double[][] p)
	{
		x = p[0][0];
		y = p[1][0];
		z = p[2][0];
	}
	
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}
	
	public double getZ()
	{
		return z;
	}
	
	public void setX(double px)
	{
		x = px;
	}
	
	public void setY(double py)
	{
		y = py;
	}
	
	public void setZ(double pz)
	{
		z = pz;
	}
	
	double [][] getVector ()
	{
	double [][] ris;
	
		ris = new double [3][1];
		
		ris[0][0] = x;
		ris[1][0] = y;
		ris[2][0] = z;
		
		return ris;
	}
}
