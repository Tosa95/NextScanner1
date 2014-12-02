package tosatto.nextscanner.hardwarecom;

public class SerialMotor implements IMotor {
	
	SerialControl serial;
	
	public SerialMotor (SerialControl s)
	{
		serial = s;
	}

	@Override
	public boolean doStep() {
		return serial.step();
	}
}
