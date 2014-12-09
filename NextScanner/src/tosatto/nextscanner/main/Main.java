package tosatto.nextscanner.main;

import tosatto.geometry.GeometryLine;
import tosatto.geometry.GeometryPlane;
import tosatto.geometry.GeometryPoint;
import tosatto.geometry.GeometryPosition;
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
		GeometryPoint p1 = new GeometryPoint(5, 7, 0);
		GeometryPoint p2 = new GeometryPoint(1, 1, 0);
		GeometryPoint p3 = new GeometryPoint(1, 5, 1);
		
		GeometryPlane pl1 = new GeometryPlane(2, 1, 0, -7);
		GeometryPlane pl2 = new GeometryPlane(4, 0, -1, -19);
		GeometryPlane pl3 = new GeometryPlane(1, 0, -1, -3);
		GeometryPlane pl4 = new GeometryPlane(0, 1, 4, -1);
		GeometryPlane pl5 = new GeometryPlane(0, 1, 4, 0);
		
		GeometryLine l1 = new GeometryLine(p1, p2);
		GeometryLine l2 = new GeometryLine(pl3, pl4);
		
		l1.getDirectorParameters();

		
		GeometryPosition pos = GeometryPosition.getPosition(pl5, l2);
		
		

	}
}
