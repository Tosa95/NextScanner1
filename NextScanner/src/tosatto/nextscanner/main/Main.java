package tosatto.nextscanner.main;

import tosatto.nextscanner.ui.SplashScreen;

public class Main {
	public static void main (String[] args)
	{
		
		Initialization i = Initialization.initialize();
		
		new SplashScreen(i);
		
		System.out.println("Initializing");
		
		while (!Initialization.isInitialized())
		{
			
		}
		
		System.out.println("Initialized");
		
		
	}
}
