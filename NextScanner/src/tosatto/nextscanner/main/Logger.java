package tosatto.nextscanner.main;

import java.util.Date;

import tosatto.nextscanner.main.notifier.EventCategory;
import tosatto.nextscanner.main.notifier.INotificationListener;

public class Logger implements INotificationListener {

	@Override
	public void eventRaised(String eName, EventCategory eCat, Object eData) {
		
		Date dt = new Date();
		
		String data = "";
		
		if (eData != null)
			data = ": " + eData.toString();
		
		System.out.println(dt.toString() + ": " + eName + " [" + eCat.getCategory() + "; " + Integer.toString(eCat.getPriority())
				+ "]" + data );
	}
	
}
