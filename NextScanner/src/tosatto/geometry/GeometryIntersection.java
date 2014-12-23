package tosatto.geometry;

import org.jdom2.output.LineSeparator;
import org.la4j.linear.LinearSystemSolver;
import org.la4j.matrix.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;
import org.la4j.vector.Vector;
import org.la4j.vector.dense.BasicVector;
import org.la4j.*;

public class GeometryIntersection {
	
	public static GeometryObject intersect (GeometryPlane p, GeometryLine l)
	{
		double [][] mtrx =
			{
				p.getCoefficients().clone(),
				l.getPlanes()[0].getCoefficients().clone(),
				l.getPlanes()[1].getCoefficients().clone()
			};
		
		mtrx[0][3] = -mtrx[0][3];
		mtrx[1][3] = -mtrx[1][3];
		mtrx[2][3] = -mtrx[2][3];
		
		Matrix AB = new Basic2DMatrix(mtrx);
		Matrix A = GeometryUtils.removeColumn(AB, 3);
		
		int rankAB = AB.rank();
		int rankA = A.rank();
		
		if (rankAB == 3 && rankA == 3)
		{
			double [] b = {mtrx[0][3], mtrx[1][3], mtrx[2][3]};
			
			Vector B = new BasicVector(b);
			
			LinearSystemSolver solver = A.withSolver(LinearAlgebra.FORWARD_BACK_SUBSTITUTION);
			
			Vector res = solver.solve(B);
			
			return new GeometryPoint(res.get(0), res.get(1), res.get(2));
		}
		else if (rankA == 2 && rankAB == 3)
		{
			return null;
		}
		else
		{
			return l;
		}
	}

	public static GeometryObject intersect (GeometryPlane p1, GeometryPlane p2, GeometryPlane p3)
	{
		double [][] mtrx =
			{
				p1.getCoefficients().clone(),
				p2.getCoefficients().clone(),
				p3.getCoefficients().clone()
			};
		
		mtrx[0][3] = -mtrx[0][3];
		mtrx[1][3] = -mtrx[1][3];
		mtrx[2][3] = -mtrx[2][3];
		
		Matrix AB = new Basic2DMatrix(mtrx);
		Matrix A = GeometryUtils.removeColumn(AB, 3);
		
		int rankAB = AB.rank();
		int rankA = A.rank();
		
		if (rankAB == 3 && rankA == 3)
		{
			double [] b = {mtrx[0][3], mtrx[1][3], mtrx[2][3]};
			
			Vector B = new BasicVector(b);
			
			LinearSystemSolver solver = A.withSolver(LinearAlgebra.FORWARD_BACK_SUBSTITUTION);
			
			Vector res = solver.solve(B);
			
			return new GeometryPoint(res.get(0), res.get(1), res.get(2));
		}
		else if (rankA != rankAB)
		{
			return null;
		}
		else if (rankA == 2)
		{
			return new GeometryLine(p1, p2);
		}
		else if (rankA == 1)
		{
			return p1;
		}
		
		throw new IllegalArgumentException("Trying to intersect something that isn't a plane");
	}
	
}
