package tosatto.geometry;

public class GeometryPoint extends GeometryObject {
	
	private double[] coordinates;
	
	public GeometryPoint (double x, double y, double z)
	{
		coordinates = new double[3];
		
		coordinates[0] = x;
		coordinates[1] = y;
		coordinates[2] = z;
	}
	
	public GeometryPoint (double[] coo)
	{
		coordinates = coo;
	}
	
	public double[] getCoordinates ()
	{
		return coordinates;
	}
	
	@Override
	public String toString ()
	{
		return "(" + String.format("%5.2f", coordinates[0]) + ", " +  String.format("%5.2f", coordinates[1])+ ", " +  String.format("%5.2f", coordinates[2]) + ")";
	}
	
	public double getDistance (GeometryPoint p)
	{
		return GeometryUtils.PointDistance(this, p);
	}
	
	public double getX ()
	{
		return coordinates[0];
	}
	
	public double getY ()
	{
		return coordinates[1];
	}
	
	public double getZ ()
	{
		return coordinates[2];
	}
	
	public static GeometryPoint getCenter ()
	{
		return new GeometryPoint (0, 0, 0);
	}
}
