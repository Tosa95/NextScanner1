package tosatto.geometry;

import org.la4j.matrix.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;

public class GeometryPosition {
	private boolean skew;
	private boolean parallel;
	private boolean incident;
	private boolean coincident;
	private boolean appartenent;
	
	public GeometryPosition ()
	{
		skew=parallel=incident=coincident=appartenent=false;
	}
	
	public static GeometryPosition skew()
	{
		GeometryPosition res = new GeometryPosition();
		
		res.skew = true;
		
		return res;
	}
	
	public static GeometryPosition parallel()
	{
		GeometryPosition res = new GeometryPosition();
		
		res.parallel = true;
		
		return res;
	}
	
	public static GeometryPosition incident()
	{
		GeometryPosition res = new GeometryPosition();
		
		res.incident = true;
		
		return res;
	}
	
	public static GeometryPosition coincident()
	{
		GeometryPosition res = new GeometryPosition();
		
		res.coincident = true;
		res.parallel = true;
		
		return res;
	}
	
	public static GeometryPosition appartenent()
	{
		GeometryPosition res = new GeometryPosition();
		
		res.appartenent = true;
		
		return res;
	}
	
	public boolean isSkew()
	{
		return skew;
	}
	
	public boolean isParallel()
	{
		return parallel;
	}
	
	public boolean isIncident()
	{
		return incident;
	}
	
	public boolean isCoincident()
	{
		return coincident;
	}
	
	public boolean isAppartenent()
	{
		return appartenent;
	}
	
	
	
	public static GeometryPosition getPosition (GeometryPlane p1, GeometryPlane p2)
	{
		double[] c1 = {p1.getA(), p1.getB(), p1.getC()};
		double[] c2 = {p2.getA(), p2.getB(), p2.getC()};
		
		GeometryUtils.normalizeVector(c1);
		GeometryUtils.normalizeVector(c2);
		
		if (!(c1[0]==c2[0] && c1[1]==c2[1] && c1[2]==c2[2]))
		{
			return incident();
		}
		else
		{
			if (p1.getD() != p2.getD())
			{
				return parallel();
			}
			else
			{
				return coincident();
			}
		}
	}
	
	public static GeometryPosition getPosition (GeometryPoint p, GeometryPlane pl)
	{
		if (p.getX()*pl.getA() + p.getY()*pl.getB() + p.getZ()*pl.getC() + pl.getD() == 0)
			return appartenent();
		else
			return new GeometryPosition();
	}
	
	public static GeometryPosition getPosition (GeometryLine l1, GeometryLine l2)
	{
		double[][] mtrx =
			{
				l1.getPlanes()[0].getCoefficients(),
				l1.getPlanes()[1].getCoefficients(),
				l2.getPlanes()[0].getCoefficients(),
				l2.getPlanes()[1].getCoefficients()
			};
		
		Matrix m = new Basic2DMatrix(mtrx);
		
		int rank = m.rank();
		
		if (rank == 4)
			return skew();
		else
		{
			Matrix a = GeometryUtils.removeColumn(m, 3);
			
			int rankA = a.rank();
			
			if (rank != rankA)
				return parallel();
			else
			{
				if (rankA == 3)
					return incident();
				else if (rankA == 2)
					return coincident();
			}
		}
		
		return new GeometryPosition();
	}
	
	public static GeometryPosition getPosition (GeometryPoint p, GeometryLine l)
	{
		GeometryPosition posA = getPosition(p, l.getPlanes()[0]);
		GeometryPosition posB = getPosition(p, l.getPlanes()[1]);
		
		if (posA.isAppartenent() && posB.isAppartenent())
		{
			return appartenent();
		}
		
		else 
			return new GeometryPosition();
	}
	
	public static GeometryPosition getPosition (GeometryPoint p1, GeometryPoint p2)
	{
		if (p1.getX() == p2.getX() && p1.getY() == p2.getY() && p1.getZ() == p2.getZ())
			return coincident();
		else
			return new GeometryPosition();
	}
	
	public static GeometryPosition getPosition (GeometryPlane p, GeometryLine l)
	{
		double [][] mtrx =
			{
				p.getCoefficients(),
				l.getPlanes()[0].getCoefficients(),
				l.getPlanes()[1].getCoefficients()
			};
		
		Matrix AB = new Basic2DMatrix(mtrx);
		Matrix A = GeometryUtils.removeColumn(AB, 3);
		
		int rankAB = AB.rank();
		int rankA = A.rank();
		
		if (rankAB != rankA)
			return parallel();
		else
		{
			if (rankA == 3)
				return incident();
			else if (rankA == 2)
				return appartenent();
		}
		
		return new GeometryPosition();
		
	}
	
	@Override
	public String toString()
	{
		String str;
		
		str = "";
		
		if (isSkew()) str += "skew ";
		if (isParallel()) str += "parallel ";
		if (isIncident()) str += "incident ";
		if (isCoincident()) str += "coincident ";
		if (isAppartenent()) str += "appartenent ";
		
		if (str == "") str = "none";
		
		return str;
	}
}
