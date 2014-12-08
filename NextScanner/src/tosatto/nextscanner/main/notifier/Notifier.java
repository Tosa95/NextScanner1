package tosatto.nextscanner.main.notifier;

public abstract class Notifier {
	
	private static Notifier n;
	private static boolean set = false;
	
	public static Notifier get()
	{
		return n;
	}
	
	public static void setNotifier (Notifier notifier)
	{
		if (!set)
		{
			n = notifier;
			
			set = false;
		}
	}
	
	public abstract void raiseEvent (String eName, EventCategory eCat, Object eData);
	
	public abstract void addListener (INotificationListener lst, EventCategory eCat);
	public abstract void removeListener (INotificationListener lst);
	
}
