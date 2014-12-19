package tosatto.geometry;

public class GeometryOrtogonal {
	
	public static GeometryPlane getOrtogonalPlane (GeometryLine l, GeometryPoint p)
	{
		double[] pdl = l.getDirectorParameters();
		
		double[] coeff = {pdl[0], pdl[1], pdl[2], 0};
		
		coeff[3] = -(p.getX() * pdl[0] + p.getY() * pdl[1] + p.getZ() * pdl[2]);
		
		return new GeometryPlane (coeff);
	}
}
