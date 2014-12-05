package tosatto.nextscanner.main.notifier;

public interface INotificationListener {
	public void eventRaised (String eName, EventCategory eCat, Object eData);
}
