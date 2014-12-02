package tosatto.nextscanner.calc.threedim;

public class Point3DPool {
	
	private Point3D[][] pool = null;
	private int steps, samples, actStp, actSmpl;
	
	public Point3DPool(int steps, int samples)
	{
		pool = new Point3D [steps][samples];
		
		this.steps = steps;
		this.samples = samples;
		
		reset();
	}
	
	public boolean insertPoint (Point3D p)
	{
		pool[actStp][actSmpl] = p;
		
		actSmpl++;
		
		if (actSmpl >= samples)
		{
			actSmpl = 0;
			actStp++;
		}
		
		if (actStp >= steps)
		{
			return false;
		}
		
		return true;
	}
	
	public void reset ()
	{
		actStp = 0;
		actSmpl = 0;
	}
	
	public Point3D getPoint (int i, int j)
	{
		return pool[i][j];
	}
	
	public int getSteps ()
	{
		return steps;
	}
	
	public int getSamples ()
	{
		return samples;
	}
}
