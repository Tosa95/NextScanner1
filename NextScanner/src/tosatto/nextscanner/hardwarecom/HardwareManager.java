package tosatto.nextscanner.hardwarecom;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import tosatto.nextscanner.hardwarecom.fake.FakeLaser;
import tosatto.nextscanner.hardwarecom.fake.FakeMotor;
import tosatto.nextscanner.hardwarecom.fake.FakeWebcam;
import tosatto.nextscanner.imaging.ImagingUtilities;
import tosatto.nextscanner.main.notifier.EventCategory;
import tosatto.nextscanner.main.notifier.INotificationListener;
import tosatto.nextscanner.main.notifier.Notifier;
import tosatto.nextscanner.main.settings.SettingsManager;

/**
 * @author Davide
 *
 * Class that manages the hardware
 *
 */
public class HardwareManager implements IWebcamListener, INotificationListener{

public static final int PIC_WAIT_TIME_OFF = (int)SettingsManager.get().getValue("PIC_WAIT_TIME_OFF");	//Time waited between laser change and pic capture
public static final int PIC_WAIT_TIME_ON = (int)SettingsManager.get().getValue("PIC_WAIT_TIME_ON");	//Time waited between laser change and pic capture
	
public static final int WCAM_FPS = (int)SettingsManager.get().getValue("WCAM_FPS");			//Frames Per Second
public static final int WCAM_WIDTH = (int)SettingsManager.get().getValue("WCAM_WIDTH");		//Width
public static final int WCAM_HEIGHT = (int)SettingsManager.get().getValue("WCAM_HEIGHT");		//Height
public static final int WCAM_ID = (int)SettingsManager.get().getValue("WCAM_ID");			//ID. Default: 0

public static final int SERIAL_BAUD = (int)SettingsManager.get().getValue("SERIAL_BAUD");				//Serial Port Baud Rate
public static final int SERIAL_TOUT = (int)SettingsManager.get().getValue("SERIAL_TOUT");				//Serial Port Timeout
public static final String[] SERIAL_PNAMES = {(String)SettingsManager.get().getValue("SERIAL_PNAME")};		//Serial Port Names

public static final boolean FAKE_MODE = (int)SettingsManager.get().getValue("ENABLE_FAKES")!=0?true:false;

private IWebcam cam;
private ILaser laser;
private IMotor motor;
private SerialControl serial;

private volatile BufferedImage actPic = null;
private volatile BufferedImage laserOFFPic = null;
private volatile BufferedImage laserONPic = null;
	
private ArrayList<INewFrameListener> FL = null;

private static HardwareManager HM = null; 

	private void initHWComm ()
	{
		if (!FAKE_MODE)
		{
			serial = new SerialControl(SERIAL_BAUD, SERIAL_TOUT, SERIAL_PNAMES);
			cam = new usbWebCam(WCAM_FPS, WCAM_WIDTH, WCAM_HEIGHT, WCAM_ID);
			cam.addIWebcamListener(this);
			laser = new SerialLaser(serial);
			motor = new SerialMotor(serial);
		}
		else
		{
			cam = new FakeWebcam();
			laser = new FakeLaser();
			motor = new FakeMotor();
		}
	}
	
	private void initVariables ()
	{
		actPic = new BufferedImage(WCAM_WIDTH, WCAM_HEIGHT, BufferedImage.TYPE_INT_RGB);
		laserOFFPic = new BufferedImage(WCAM_WIDTH, WCAM_HEIGHT, BufferedImage.TYPE_INT_RGB);
		laserONPic = new BufferedImage(WCAM_WIDTH, WCAM_HEIGHT, BufferedImage.TYPE_INT_RGB);
		FL = new ArrayList<INewFrameListener>();
	}

	private HardwareManager ()
	{
		initHWComm();
		initVariables();
		
		Notifier.get().addListener(this, new EventCategory("webcam:frame", 2));
	}
	
	public static HardwareManager get ()
	{
		if (HM == null)
			HM = new HardwareManager();
		
		return HM;
	}

	@Override
	public void newFrame(BufferedImage I, IWebcam source) {
		
		actPic = ImagingUtilities.deepCopy(I);
		
		if (FL != null)
		{
			for (INewFrameListener fl:FL)
				fl.newFrame(actPic);
		}
		
	}
	
	//Cattura i due frame correnti
	public void captureAct()
	{
		//spegne il laser
		laser.beamOFF();
		
		try {
			Thread.sleep(PIC_WAIT_TIME_OFF); //attende
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		laserOFFPic = actPic; //prende il frame
			
		//accende il laser
		laser.beamON();
		
		try {
			Thread.sleep(PIC_WAIT_TIME_ON);//attende
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//prende il frame
		laserONPic = actPic;
		
		//spegne nuovamente il laser
		laser.beamOFF();
	}
	
	public void rotate ()
	{
		motor.doStep();
	}
	
	public BufferedImage getActPic ()
	{
		return actPic;
	}
	
	public BufferedImage getLaserOFFPic ()
	{
		return laserOFFPic;
	}
	
	public BufferedImage getLaserONPic ()
	{
		return laserONPic;
	}
	
	public void close ()
	{
		if (!FAKE_MODE)
			serial.close();
		cam.close();
	}
	
	public void addINewFrameListener (INewFrameListener INFL)
	{
		FL.add(INFL);
	}
	
	public void removeINewFrameListener (INewFrameListener INFL)
	{
		FL.remove(INFL);
	}
	
	public ILaser getLaser ()
	{
		return laser;
	}

	@Override
	public void eventRaised(String eName, EventCategory eCat, Object eData) {

		newFrame((BufferedImage)eData, cam);
		
	}
}
