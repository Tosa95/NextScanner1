package tosatto.nextscanner.hardwarecom;

public class SerialLaser implements ILaser{

	SerialControl serial;
	
	public SerialLaser (SerialControl s)
	{
		serial = s;
	}
	
	@Override
	public boolean beamON() {
		return serial.laserON();
	}

	@Override
	public boolean beamOFF() {
		return serial.laserOFF();
	}

}
