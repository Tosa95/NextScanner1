package tosatto.nextscanner.hardwarecom.fake;

import tosatto.nextscanner.hardwarecom.IMotor;
import tosatto.nextscanner.main.notifier.EventCategory;
import tosatto.nextscanner.main.notifier.Notifier;

public class FakeMotor implements IMotor {

	private void sendState (int state)
	{
		Notifier.get().raiseEvent("Motor step", new EventCategory("laser:state", 5), new Integer (state));
	}
	
	@Override
	public boolean doStep() {
		
		sendState(IMotor.BEFORE_STEP);
		
		sendState(IMotor.AFTER_STEP);
		
		return true;
	}

}
