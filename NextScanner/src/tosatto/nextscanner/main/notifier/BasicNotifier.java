package tosatto.nextscanner.main.notifier;

import java.util.ArrayList;
import java.util.List;

public class BasicNotifier extends Notifier {
	
	List <ListenerListEntry> listenerList = new ArrayList<ListenerListEntry>();

	public BasicNotifier ()
	{
		
	}
	
	@Override
	public void raiseEvent(String eName, EventCategory eCat, Object eData) {
		
		for (ListenerListEntry l: listenerList)
		{
			if (l.getEventCategory().eventCompatible(eCat))
			{
				l.getListener().eventRaised(eName, eCat, eData);
			}
		}
		

	}

	@Override
	public void addListener(INotificationListener lst, EventCategory eCat) {
		
		listenerList.add(new ListenerListEntry(lst, eCat));

	}

	@Override
	public void removeListener(INotificationListener lst) {
		
		for (ListenerListEntry l: listenerList)
		{
			if (l.getListener() == lst)
			{
				listenerList.remove(l);
			}
		}

	}

}
