package tosatto.nextscanner.hardwarecom;

import tosatto.nextscanner.main.notifier.EventCategory;
import tosatto.nextscanner.main.notifier.Notifier;

public class SerialLaser implements ILaser{

	SerialControl serial;
	
	private void sendState (boolean state)
	{
		Notifier.get().raiseEvent("Laser state change", new EventCategory("laser:state", 5), new Boolean (state));
	}
	
	public SerialLaser (SerialControl s)
	{
		serial = s;
	}
	
	@Override
	public boolean beamON() {
		
		boolean res = serial.laserON();
		
		this.sendState(true);
		
		return res;
		
	}

	@Override
	public boolean beamOFF() {
		
		boolean res = serial.laserOFF();
		
		this.sendState(false);
		
		return res;
	}

}
