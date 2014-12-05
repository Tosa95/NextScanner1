package tosatto.nextscanner.hardwarecom.fake;

import java.awt.image.BufferedImage;

import tosatto.nextscanner.hardwarecom.IWebcam;
import tosatto.nextscanner.hardwarecom.IWebcamListener;
import tosatto.nextscanner.imaging.ImagingUtilities;
import tosatto.nextscanner.main.notifier.EventCategory;
import tosatto.nextscanner.main.notifier.INotificationListener;
import tosatto.nextscanner.main.notifier.Notifier;

public class FakeWebcam implements IWebcam, INotificationListener {

	private BufferedImage actFrame;
	
	public FakeWebcam()
	{
		actFrame = ImagingUtilities.getImageFromResource("fake/LaserOff.png");
		
		sendNewFrame();
		
		Notifier.get().addListener(this, new EventCategory ("laser:state", 0));
		Notifier.get().addListener(this, new EventCategory ("initialization:state", 0));
	}
	
	@Override
	public BufferedImage getActFrame() {
		return actFrame;
	}

	@Override
	public void addIWebcamListener(IWebcamListener wL) {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() {
		Notifier.get().removeListener(this);
	}

	private void sendNewFrame ()
	{
		Notifier.get().raiseEvent("WebcamNewFrame", new EventCategory("webcam:frame", 2), actFrame);
	}
	
	@Override
	public void eventRaised(String eName, EventCategory eCat, Object eData) {
		
		if (eName.equals("Laser state change"))
		{
			boolean b = (Boolean)eData;
			
			if (b == true)
				actFrame = ImagingUtilities.getImageFromResource("fake/LaserOn.png");
			else
				actFrame = ImagingUtilities.getImageFromResource("fake/LaserOff.png");
			
			sendNewFrame();
		}
		else if (eName.equals("Main window created"))
		{
			sendNewFrame();
		}
		
	}

}
