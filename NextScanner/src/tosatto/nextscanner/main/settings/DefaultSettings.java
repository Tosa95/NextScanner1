package tosatto.nextscanner.main.settings;


public class DefaultSettings {
	
	private final static Setting[] dafaultS = 	{
															//HardwareManager
															new Setting("WCAM_HEIGHT", 240),
															new Setting("WCAM_WIDTH", 320),
															new Setting("WCAM_FPS", 30),
															new Setting("WCAM_ID", 0),
															
															new Setting("PIC_WAIT_TIME_OFF", 300),
															new Setting("PIC_WAIT_TIME_ON", 300),
															
															new Setting("SERIAL_BAUD", 9600),
															new Setting("SERIAL_TOUT", 2000),
															new Setting("SERIAL_PNAME", "COM3"),
															
															
															//ScanningThread
															new Setting("STEPS", 400),
															new Setting("RESOLUTION", 10.0),
															new Setting("VERT_RES", 30.0),
															
															
															//ThreeDimManager
															new Setting("CAM_FOV", Math.toRadians(52)),
															new Setting("CAM_B", Math.PI/7),
															new Setting("CAM_D", 0.5),
															new Setting("Z_MIN", 0.0),
															new Setting("Z_MIN_PX", 0.0),
															
															//Renderer
															new Setting("RENDER_EDGES", 0)
															
												};
	
	public static void defaultSettings (SettingsManager SM)
	{
		for (Setting s: DefaultSettings.dafaultS)
		{
			SM.addSetting(s.getName(), s.getValue());
		}
	}
}
