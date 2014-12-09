package tosatto.nextscanner.main;

import tosatto.geometry.GeometryIntersection;
import tosatto.geometry.GeometryLine;
import tosatto.geometry.GeometryPlane;
import tosatto.geometry.GeometryPoint;
import tosatto.geometry.GeometryPosition;
import tosatto.geometry.GeometryTransformation;
import tosatto.nextscanner.ui.SplashScreen;

public class Main {
	public static void main (String[] args)
	{
		testGeometry();
		
		Initialization i = Initialization.initialize();
		
		new SplashScreen(i);
		
		System.out.println("Initializing");
		
		while (!Initialization.isInitialized())
		{
			
		}
		
		System.out.println("Initialized");
		
		
	}
	
	private static void testGeometry ()
	{
		GeometryPoint p1 = new GeometryPoint(2, 2, 0);
		GeometryPoint p2 = new GeometryPoint(1, 5, 0);
		GeometryPoint p3 = new GeometryPoint(3, 3, 0);
		GeometryPoint p4 = new GeometryPoint(5, 7, 2);
		
		double [] trVect = {3, 0, 0};
		
		GeometryPlane pl = new GeometryPlane(p1, p2, p3);
		GeometryLine l = new GeometryLine(p1, p4);
		
		p2 = GeometryTransformation.translate(p1, trVect);

		GeometryIntersection.intersect(pl, l);
	}
}
