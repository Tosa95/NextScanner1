package tosatto.nextscanner.main;

import tosatto.geometry.GeometryPlane;
import tosatto.geometry.GeometryPoint;
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
		GeometryPoint p1 = new GeometryPoint(0, 1, 1);
		GeometryPoint p2 = new GeometryPoint(1, 1, 0);
		GeometryPoint p3 = new GeometryPoint(0, 0, 1);
		
		GeometryPlane pl = new GeometryPlane(2, 3, 1, 0);
		GeometryPlane pl1 = new GeometryPlane(p1, p2, p3);
		
		System.out.println(p1.toString());
		System.out.println(p2.toString());
		System.out.println(p1.getDistance(p2));
		
		System.out.println(pl.toString());
	}
}
