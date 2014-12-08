package tosatto.nextscanner.hardwarecom;

public interface IMotor {
	public static final int BEFORE_STEP = 0;
	public static final int AFTER_STEP = 0;
	
	public boolean doStep();
}
