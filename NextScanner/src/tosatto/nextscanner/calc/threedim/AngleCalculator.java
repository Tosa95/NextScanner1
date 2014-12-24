package tosatto.nextscanner.calc.threedim;

public class AngleCalculator {
	double fovh, fovv, w, h;
	
	
	public AngleCalculator (double fovh, double fovv, double w, double h)
	{
		this.fovh = fovh;
		this.fovv = fovv;
		this.w = w;
		this.h = h;
	}
	
	public AngleCalculator (double fovv, double w, double h)
	{
		this(getHorizontalFov(fovv, w, h), fovv, w, h);
	}
	
	public double getVerticalAngle (int M)
	{
		//M = Math.abs((int)(M - h/2));
		
		M = (int)(M - h/2);
		
		return (M*fovv)/h;
	}
	
	public double getHorizontalAngle (int N)
	{
		//N = Math.abs((int)(N - (w/2)));
		
		N = (int)(N - (w/2));
		
		return ((fovh/2)/(w/2))*N;
	}
	
	public static double getHorizontalFov (double fovv, double w, double h)
	{
		return 2*Math.atan((Math.tan(fovv/2)*w)/(double)h);
	}
	
	public double getW()
	{
		return w;
	}
	
	public double getH()
	{
		return h;
	}
	
	public double getFOVV()
	{
		return fovv;
	}
	
	public double getFOVH()
	{
		return fovh;
	}
}
