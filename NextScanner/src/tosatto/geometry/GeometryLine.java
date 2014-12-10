package tosatto.geometry;

import org.la4j.matrix.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;
import org.la4j.vector.dense.BasicVector;

public class GeometryLine extends GeometryObject {
	private GeometryPlane[] planes;
	
	public GeometryLine (GeometryPlane p1, GeometryPlane p2)
	{
		if (GeometryPosition.getPosition(p1, p2).isIncident())
		{
			planes = new GeometryPlane[2];
			
			planes[0] = p1;
			planes[1] = p2;
		}
		else
		{
			throw new IllegalArgumentException("Planes can't be parallel or incident");
		}
	}
	
	public GeometryLine (GeometryPoint p1, GeometryPoint p2)
	{
		if (!GeometryPosition.getPosition(p1, p2).isCoincident())
		{
			planes = new GeometryPlane[2];
			
			double [] pdr = {p2.getX() - p1.getX(), p2.getY() - p1.getY(), p2.getZ() - p1.getZ()};
			
			double l = pdr[0], m = pdr[1], n = pdr[2];
			
			if (l != 0)
			{
				planes[0] = new GeometryPlane(m/l, -1, 0, p1.getY() - (m/l) * p1.getX());
				planes[1] = new GeometryPlane(n/l, 0, -1, p1.getZ() - (n/l) * p1.getX());
			}
			else if (m != 0)
			{
				planes[0] = new GeometryPlane(-1, l/m, 0, p1.getX() - (l/m) * p1.getY());
				planes[1] = new GeometryPlane(0, n/m, -1, p1.getZ() - (n/m) * p1.getY());
			}
			else if (n != 0)
			{
				planes[0] = new GeometryPlane(-1, 0, l/n, p1.getX() - (l/n) * p1.getZ());
				planes[1] = new GeometryPlane(0, -1, m/n, p1.getY() - (m/n) * p1.getZ());
			}
		}
		else
		{
			throw new IllegalArgumentException("Points can't be coincident");
		}
	}
	
	public double [] getDirectorParameters ()
	{
		double[] res = new double[3];
		
		double[][] mtrx =
			{
				{planes[0].getA(), planes[0].getB(), planes[0].getC()},
				{planes[1].getA(), planes[1].getB(), planes[1].getC()}
			};
		
		Matrix m = new Basic2DMatrix(mtrx);
		
		res[0] = GeometryUtils.removeColumn(m, 0).determinant();
		res[1] = -GeometryUtils.removeColumn(m, 1).determinant();
		res[2] = GeometryUtils.removeColumn(m, 2).determinant();
		
		return res;
		
	}
	
	public void normalize ()
	{
		planes[0].normalize();
		planes[1].normalize();
	}
	
	public GeometryPlane[] getPlanes()
	{
		return planes;
	}
	
	public GeometryPoint getPoint (double t)
	{
		GeometryPlane pl = new GeometryPlane(1, 0, 0, t);
		GeometryPoint res;
		
		if (GeometryPosition.getPosition(pl, this).isIncident())
		{
			return (GeometryPoint) GeometryIntersection.intersect(pl, this);
		}
		
		pl = new GeometryPlane(0, 1, 0, t);
		
		if (GeometryPosition.getPosition(pl, this).isIncident())
		{
			return (GeometryPoint) GeometryIntersection.intersect(pl, this);
		}
		
		pl = new GeometryPlane(0, 0, 1, t);
		
		if (GeometryPosition.getPosition(pl, this).isIncident())
		{
			return (GeometryPoint) GeometryIntersection.intersect(pl, this);
		}
		
		return null;
	}
	
	@Override
	public String toString ()
	{
		return "[" + planes[0].toString() + "; " + planes[1].toString() + "]";
	}
}
