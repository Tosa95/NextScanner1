package tosatto.nextscanner.main;

import tosatto.nextscanner.hardwarecom.HardwareManager;
import tosatto.nextscanner.main.notifier.BasicNotifier;
import tosatto.nextscanner.main.notifier.EventCategory;
import tosatto.nextscanner.main.notifier.Notifier;
import tosatto.nextscanner.main.settings.SettingsManager;
import tosatto.nextscanner.ui.IInitListener;

public class Initialization implements Runnable {
	private volatile static boolean initialized = false;
	
	private volatile double percentage;
	private volatile String actTask;
	private volatile IInitListener i;
	
	public void setInitListener (IInitListener in)
	{
		i = in;
	}

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
	
	private void notifyUpdate ()
	{
		if (i != null)
			i.initValuesChanged();
	}
	
	private void setProgress (double perc)
	{
		percentage = perc;
		notifyUpdate();
	}
	
	private void setTask (String tsk)
	{
		actTask = tsk;
		notifyUpdate();
	}
	
	private void sendState (String actState)
	{
		Notifier.get().raiseEvent (actState,  new EventCategory("initialization:state", 7), null);
	}
	
	@Override
	public void run() {
		
		Notifier.setNotifier(new BasicNotifier());
		
		Notifier.get().addListener(new Logger(), new EventCategory("", 5));
		
		sendState ("Initialization started");
		
		setProgress(0);
		initialized = false;
		
		setTask("Loading settings");
		SettingsManager.get().loadSettings();
		
		sendState ("SettingsLoaded");
		
		setProgress(2);
		
		setTask("Connecting to hardware");
		HardwareManager.get();
		
		sendState ("Connected to hardware");
		
		setProgress (60);
		
		setTask("Creating main window");
		new MainWindow();
		
		sendState ("Main window created");
		
		initialized = true;
		setProgress(100);
		
		sendState ("Initialization completed");
		
	}
}
