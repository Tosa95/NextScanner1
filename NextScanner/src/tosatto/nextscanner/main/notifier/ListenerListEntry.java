package tosatto.nextscanner.main.notifier;

class ListenerListEntry {
	
	INotificationListener list;
	EventCategory eCat;
	
	public ListenerListEntry (INotificationListener l, EventCategory ct)
	{
		list = l;
		eCat = ct;
	}
	
	public INotificationListener getListener ()
	{
		return list;
	}
	
	public EventCategory getEventCategory ()
	{
		return eCat;
	}
}