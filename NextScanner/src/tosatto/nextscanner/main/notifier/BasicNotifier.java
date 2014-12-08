package tosatto.nextscanner.main.notifier;

import java.util.ArrayList;
import java.util.List;

public class BasicNotifier extends Notifier {
	
	private volatile List <ListenerListEntry> listenerList = new ArrayList<ListenerListEntry>();

	public BasicNotifier ()
	{
		
	}
	
	@Override
	public synchronized void raiseEvent(String eName, EventCategory eCat, Object eData) {
		
		//For concurrency troubles
		List <ListenerListEntry> copy = new ArrayList<ListenerListEntry>(listenerList);
		
		for (ListenerListEntry l: copy)
		{
			if (l.getEventCategory().eventCompatible(eCat))
			{
				l.getListener().eventRaised(eName, eCat, eData);
			}
		}
		

	}

	@Override
	public synchronized void addListener(INotificationListener lst, EventCategory eCat) {
		
		listenerList.add(new ListenerListEntry(lst, eCat));

	}

	@Override
	public synchronized void removeListener(INotificationListener lst) {
		
		//For concurrency troubles
		List <ListenerListEntry> copy = new ArrayList<ListenerListEntry>(listenerList);
		
		for (int i = 0; i < copy.size(); i++)
		{
			if (copy.get(i).getListener() == lst)
			{
				copy.remove(i);
				i--;
			}
		}
		
		listenerList = copy;

	}

}
