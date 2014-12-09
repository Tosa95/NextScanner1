package tosatto.geometry;

public class GeometryTransformation {
	
	public static GeometryPoint translate (GeometryPoint p, double[] vector)
	{
		double x, y, z;
		
		x = p.getX() + vector[0];
		y = p.getY() + vector[1];
		z = p.getZ() + vector[2];
		
		return new GeometryPoint(x, y, z);
	}
	
}
