package tosatto.geometry;

import org.la4j.vector.Vector;
import org.la4j.vector.dense.BasicVector;

public class GeometryTransformation {
	
	public static GeometryPoint translate (GeometryPoint p, double[] vector)
	{
		return translate (p, new BasicVector(vector));
	}
	
	public static GeometryPoint translate (GeometryPoint p, Vector vector)
	{
		double x, y, z;
		
		x = p.getX() + vector.get(0);
		y = p.getY() + vector.get(1);
		z = p.getZ() + vector.get(2);
		
		return new GeometryPoint(x, y, z);
	}
	
	public static GeometryPoint rotate (GeometryPoint p, GeometryLine a, double angle)
	{
		
		/*
		 * Piano contenente a e passante per p
		 */
		GeometryPlane pl1 = new GeometryPlane(p, a);
		
		/*
		 * Piano ortogonale ad a e passante per p
		 */
		GeometryPlane pl2 = GeometryOrtogonal.getOrtogonalPlane(a, p);
		
		/*
		 * Retta ortogonale ad a e passante per p
		 */
		GeometryLine l1 = new GeometryLine(pl1, pl2);
		
		GeometryPoint c = (GeometryPoint)GeometryIntersection.intersect(pl2, a);
		
		double dist = c.getDistance(p);
		
		double cos = Math.cos(angle) * dist;
		double sin = Math.sin(angle) * dist;
		double tan = Math.tan(angle) * dist;
		
		angle %= 2*Math.PI;
		
		if (angle < 0)
			angle = 2*Math.PI + angle;
		
		if (angle > (3*Math.PI)/4 || angle < Math.PI/2)
		{
			GeometryPlane pl3 = GeometryOrtogonal.getOrtogonalPlane(l1, p);
			
			GeometryLine l2 = new GeometryLine(pl2, pl3);
			
			double[] pdl2 = l2.getDirectorParameters();
			
			pdl2 = GeometryUtils.normalizeVector(pdl2);
			
			Vector v = new BasicVector(pdl2);
			
			v = v.multiply(tan);
			
			GeometryPoint p2 = translate(p, v);
			
			GeometryLine l3 = new GeometryLine(c, p2);
			
			double [] pdl3 = GeometryUtils.normalizeVector(l3.getDirectorParameters());
			
			Vector v2 = new BasicVector(pdl3);
			
			v2 = v2.multiply(dist);
			
			return translate (c, v2);
		}
		
		
		return null;
	}
	
	public static GeometryPlane rotate (GeometryPlane pl, GeometryLine a, double angle)
	{
		GeometryPoint p = pl.getPoint();
		
		GeometryPoint pR = rotate(p, a, angle);
		
		return new GeometryPlane(pR, a);
	}
	
}
