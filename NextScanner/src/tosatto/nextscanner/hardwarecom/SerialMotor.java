package tosatto.nextscanner.hardwarecom;

import tosatto.nextscanner.main.notifier.EventCategory;
import tosatto.nextscanner.main.notifier.Notifier;

public class SerialMotor implements IMotor {
	
	SerialControl serial;
	
	public SerialMotor (SerialControl s)
	{
		serial = s;
	}

	private void sendState (int state)
	{
		Notifier.get().raiseEvent("Motor step", new EventCategory("laser:state", 5), new Integer (state));
	}
	
	@Override
	public boolean doStep() {
		
		sendState(IMotor.BEFORE_STEP);
		
		boolean res = serial.step();
		
		sendState(IMotor.AFTER_STEP);
		
		return res;
	}
}
