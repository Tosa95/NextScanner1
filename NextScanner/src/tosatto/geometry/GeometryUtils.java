package tosatto.geometry;

import java.math.*;

import org.la4j.matrix.Matrix;
import org.la4j.vector.Vector;
import org.la4j.vector.Vectors;
import org.la4j.vector.dense.BasicVector;

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
	
	public static double[] normalizeVector (double [] vec)
	{
		double[] result = new double[vec.length];
		
		Vector v = new BasicVector(vec);
		
		double n   = v.fold(Vectors.mkEuclideanNormAccumulator());
		
		v = v.divide (n);
		
		for (int i = 0; i < vec.length; i++)
		{
			result[i] = v.get(i);
		}
		
		return result;
	}
	
	private static int[] getIndexes (int i, int sz)
	{
		int[] res = new int[sz - 1];
		
		int k = 0;
		
		for (int j = 0; j < sz; j++)
		{
			if (j != i)
			{
				res[k++] = j;
			}
		}
		
		return res;
	}
	
	private static int[] getIndexes (int sz)
	{
		int[] res = new int[sz];
		
		int k = 0;
		
		for (int j = 0; j < sz; j++)
		{
				res[k++] = j;
		}
		
		return res;
	}
	
	public static Matrix removeColumn (Matrix m, int c)
	{
		int [] rowIndex, columnIndex;
		
		rowIndex = getIndexes(m.rows());
		columnIndex = getIndexes(c, m.columns());
		
		return m.select(rowIndex, columnIndex);
	}
	
	public static Matrix removeRow (Matrix m, int r)
	{
		int [] rowIndex, columnIndex;
		
		rowIndex = getIndexes(r, m.rows());
		columnIndex = getIndexes(m.columns());
		
		return m.select(rowIndex, columnIndex);
	}
	
	public static Matrix getMinor (Matrix m, int r, int c)
	{
		int [] rowIndex, columnIndex;
		
		rowIndex = getIndexes(r, m.rows());
		columnIndex = getIndexes(c, m.columns());
		
		return m.select(rowIndex, columnIndex);
	}
}
