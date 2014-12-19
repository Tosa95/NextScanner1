package tosatto.nextscanner.calc.threedim;

public class PositionCalculator implements ICalculator {
	private double fov, b, d, w, h, fovh;
	
	private AngleCalculator ac = null;
	
	public PositionCalculator (double fov, double b, double d, double w, double h)
	{
		this.fov = fov;
		this.b = b;
		this.d = d;
		this.w = w;
		this.h = h;
		
		fovh = 2*Math.atan((Math.tan(fov/2)*w)/(double)h); //Caclola il FOV orizzontale
		
		ac = new AngleCalculator(fov, w, h);
	}
	
	//Calcola l'angolo corrispondente all'attuale scostamento rispetto
	//Al centro dell'immagine [nPix] (§2.2.2)
	public double calcAngle (int N)
	{
		/*return ((fovh/2)/(w/2))*N;*/
		
		return ac.getHorizontalAngle((int)(N + w/2));
	}
	
	//Caclola la distanza D1 ($2.2.2)
	public double calcCos (int N)
	{
		double a = calcAngle (N);
		
		return d*(Math.tan(a)/(Math.tan(a)+Math.tan(b)));
	}
	
	public double calcSin (int N)
	{
		return Math.sin(b)*calcDistFromSource(N);
	}
	
	//Calcola X (§2.2.2)
	public double calcDistFromSource (int N)
	{
	double A = calcCos(N);
	
		return A / Math.cos(b);
	}
	
	public double calcZCoord (int M, int N)
	{
		
		int real;
		real = (int)(N - w/2); //Calcola lo scostamento rispetto al centro
		
		double dist = d - ((real>0)?calcCos (Math.abs(real)):(-calcCos (Math.abs(real)))); //Calcola D2 (§2.2.3)
		double npix = M-h/2; //Calcola lo scostamento verticale rispetto al centro
		double a = ac.getVerticalAngle(M); /*(Math.abs(npix)*fov)/h;*/ //Calcola l'angolazione verticale attuale (§2.2.3)
		double ris = dist * Math.tan(a); //Caclola il risultato (§2.2.3)
		
		if (npix < 0)
			ris = -ris; //Inverte in caso di scostamento negativo
		
		return ris;
	}
	
	public double calcXCoord (int N)
	{
	double ris;
	int real;
		real = (int)(N - w/2); //Calcola lo scostamento rispetto al centro
		
		ris = calcDistFromSource(Math.abs(real));
		
		if (real < 0) ris = -ris; //inverte nel caso di scostamento negativo
		
		return ris;
	}
	
	public double calcYCoord (int N)
	{
		double ris;
		int real;
			real = (int)(N - w/2);
			
			ris = calcSin(Math.abs(real));
			
			if (real < 0) ris = -ris;
			
			return ris;
	}

	@Override
	public Point3D calcPosition(int M, int N) {

		double x = calcXCoord((int)(w - N));
		double y = 0;
		double z = calcZCoord((int)(h-M), N);
		
		return new Point3D(x, y, z);
		
	}
}
