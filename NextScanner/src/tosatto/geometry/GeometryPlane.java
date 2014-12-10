package tosatto.geometry;

import org.la4j.LinearAlgebra;
import org.la4j.linear.LinearSystemSolver;
import org.la4j.matrix.Matrix;
import org.la4j.matrix.sparse.CRSMatrix;
import org.la4j.vector.Vector;
import org.la4j.vector.Vectors;
import org.la4j.vector.dense.BasicVector;

public class GeometryPlane extends GeometryObject {
	private double [] coefficients;
	
	public void normalize ()
	{
		coefficients = GeometryUtils.normalizeVector(coefficients);
	}
	
	public GeometryPlane (double a, double b, double c, double d)
	{
		if (a != 0 || b != 0 || c != 0)
		{
			coefficients = new double [4];
			
			coefficients[0] = a;
			coefficients[1] = b;
			coefficients[2] = c;
			coefficients[3] = d;
		}
		else
		{
			throw new IllegalArgumentException("At least one of (a, b, c) coefficients must be different by 0");
		}
	}
	
	public GeometryPlane (double [] coeff)
	{
		if (coeff[0] != 0 || coeff[1] != 0 || coeff[2] != 0)
		{
			coefficients = coeff;
		}
		else
		{
			throw new IllegalArgumentException("At least one of (a, b, c) coefficients must be different by 0");
		}
	}
	
	public int[] getColumnIndices (int i)
	{
		int[] res = new int[3];
		
		int k = 0;
		
		for (int j = 0; j < 4; j++)
		{
			if (j != i)
			{
				res[k++] = j;
			}
		}
		
		return res;
	}
	
	public GeometryPlane (GeometryPoint p1, GeometryPoint p2, GeometryPoint p3)
	{
		if (GeometryPosition.areAllineated(p1, p2, p3) == false)
		{
			this.coefficients = new double[4];
			
			/* Matrice:
			 * |p1x, p1y, p1z, 1|
			 * |p2x, p2y, p2z, 1|
			 * |p3x, p3y, p3z, 1|
			 * 
			 * ossia la parte inferiore di A: 
			 * 
			 * |  x,   y,   z, 1|
			 * |p1x, p1y, p1z, 1|
			 * |p2x, p2y, p2z, 1|
			 * |p3x, p3y, p3z, 1|
			 * 
			 * condizione di complanarità di 4 pti: |A| = 0
			 * 
			 * dà l'equazione del piano.
			 * 
			 * per trovare i coefficienti faccio il det con Laplace
			 * sulla prima riga di |a|
			 * 
			 * a = |A00|
			 * b = |A01|
			 * c = |A02|
			 * d = |A03|
			 */
			Matrix m = new CRSMatrix(new double[][] {
					   {p1.getCoordinates()[0], p1.getCoordinates()[1], p1.getCoordinates()[2], 1},
					   {p2.getCoordinates()[0], p2.getCoordinates()[1], p2.getCoordinates()[2], 1},
					   {p3.getCoordinates()[0], p3.getCoordinates()[1], p3.getCoordinates()[2], 1}
					});
	
			int [] column = getColumnIndices(0);
			int [] row = {0, 1, 2};
			
			Matrix ma = m.select(row, column);
			
			column = getColumnIndices(1);
			Matrix mb = m.select(row, column);
			
			column = getColumnIndices(2);
			Matrix mc = m.select(row, column);
			
			column = getColumnIndices(3);
			Matrix md = m.select(row, column);
			
			coefficients[0] = ma.determinant();
			coefficients[1] = -mb.determinant();
			coefficients[2] = mc.determinant();
			coefficients[3] = -md.determinant();
		}
		else
		{
			throw new IllegalArgumentException("Points can't be allineated");
		}
	}
	
	public GeometryPlane (GeometryPoint p, GeometryLine l)
	{
		this (p, l.getPoint(0), l.getPoint(1));
	}
	
	public double [] getCoefficients ()
	{
		return coefficients;
	}
	
	public double getA()
	{
		return coefficients[0];
	}
	
	public double getB()
	{
		return coefficients[1];
	}
	
	public double getC()
	{
		return coefficients[2];
	}
	
	public double getD()
	{
		return coefficients[3];
	}
	
	@Override
	public String toString()
	{
		String a = String.format("%5.2f", coefficients[0]) + "x";
		String b = String.format("%5.2f", coefficients[1]) + "y";
		String c = String.format("%5.2f", coefficients[2]) + "z";
		String d = String.format("%5.2f", coefficients[3]);
		
		return a + " + " + b + " + " +  c + " + " + d;
	}
}
