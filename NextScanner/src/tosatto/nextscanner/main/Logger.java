package tosatto.nextscanner.main;

import tosatto.nextscanner.main.notifier.EventCategory;
import tosatto.nextscanner.main.notifier.INotificationListener;

public class Logger implements INotificationListener {

	@Override
	public void eventRaised(String eName, EventCategory eCat, Object eData) {
		
		String data = "";
		
		if (eData != null)
			data = ": " + eData.toString();
		
		System.out.println(eName + " [" + eCat.getCategory() + "; " + Integer.toString(eCat.getPriority())
				+ "]" + data );
	}
	
}
