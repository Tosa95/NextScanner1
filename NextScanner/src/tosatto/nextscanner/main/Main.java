package tosatto.nextscanner.main;

import java.awt.Color;

import tosatto.geometry.GeometryIntersection;
import tosatto.geometry.GeometryLine;
import tosatto.geometry.GeometryPlane;
import tosatto.geometry.GeometryPoint;
import tosatto.geometry.GeometryPosition;
import tosatto.geometry.GeometrySpace;
import tosatto.geometry.GeometryTransformation;
import tosatto.nextscanner.ui.SplashScreen;
import tosatto.nextscanner.ui.ogl.Renderer;

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
		
		GeometrySpace GS = new GeometrySpace(2, new Renderer());
		
		GS.addObject("r", new GeometryLine (new GeometryPoint(0,0,0), new GeometryPoint(0, 1, 0)), Color.white);
	}
}