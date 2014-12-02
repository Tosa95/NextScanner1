package tosatto.nextscanner.main;

public class Main {
	public static void main (String[] args)
	{
		Initialization i = Initialization.initialize();
		
		System.out.println("Initializing");
		
		while (!Initialization.isInitialized())
		{
			
		}
		
		System.out.println("Initialized");
		
		new MainWindow();
	}
}
