package tosatto.nextscanner.hardwarecom.fake;

import tosatto.nextscanner.hardwarecom.ILaser;
import tosatto.nextscanner.main.notifier.EventCategory;
import tosatto.nextscanner.main.notifier.Notifier;

public class FakeLaser implements ILaser {
	
	private void sendState (boolean state)
	{
		Notifier.get().raiseEvent("Laser state change", new EventCategory("laser:state", 5), new Boolean (state));
	}
	
	@Override
	public boolean beamON() {

		sendState(true);
		
		return true;
	}

	@Override
	public boolean beamOFF() {

		sendState(false);
		
		return true;
	}

}
