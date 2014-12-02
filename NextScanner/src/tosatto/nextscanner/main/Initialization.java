package tosatto.nextscanner.main;

import tosatto.nextscanner.hardwarecom.HardwareManager;
import tosatto.nextscanner.main.settings.SettingsManager;

public class Initialization implements Runnable {
	private volatile static boolean initialized = false;
	
	private volatile double percentage;
	private volatile String actTask;

	public static boolean isInitialized ()
	{
		return initialized;
	}
	
	public double getCompletionPercentage ()
	{
		return percentage;
	}
	
	public String getActTask()
	{
		return actTask;
	}
	
	private Initialization()
	{
		percentage = 0.0;
		actTask = "";
	}
	
	public static Initialization initialize ()
	{
		Initialization i = new Initialization ();
		
		Thread t = new Thread(i);
		ThreadManager.addThread(t);
		t.start();
		
		return i;
	}
	
	@Override
	public void run() {
		
		percentage = 0.0;
		initialized = false;
		
		actTask = "Loading settings";
		SettingsManager.get().loadSettings();
		
		percentage = 10.0;
		
		actTask = "Connecting to hardware";
		HardwareManager.get();
		
		percentage = 100.0;
		initialized = true;
		
		
		
	}
}
