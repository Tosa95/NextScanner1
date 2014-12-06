package tosatto.geometry;

import java.math.*;

public class GeometryUtils {
	
	public static double PointDistance (GeometryPoint p1, GeometryPoint p2)
	{
		double a, b, c;
		
		a = p2.getCoordinates()[0] - p1.getCoordinates()[0];
		b = p2.getCoordinates()[1] - p1.getCoordinates()[1];
		c = p2.getCoordinates()[2] - p1.getCoordinates()[2];
		
		a *= a;
		b *= b;
		c *= c;
		
		return Math.sqrt(a + b + c);
	}
	
	public static double[] AddCoordinate (double [] coo)
	{
		double [] res = new double[coo.length + 1];
		
		res[0] = coo[0];
		res[1] = coo[1];
		res[2] = coo[2];
		
		return res;
	}
}
