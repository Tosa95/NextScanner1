package tosatto.nextscanner.ui;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;

import tosatto.nextscanner.calc.imgcmp.ImageComparer;
import tosatto.nextscanner.calc.threedim.ObjFileCreator;
import tosatto.nextscanner.calc.threedim.Point3D;
import tosatto.nextscanner.calc.threedim.Point3DManipulationUtilities;
import tosatto.nextscanner.calc.threedim.PositionCalculator;
import tosatto.nextscanner.calc.threedim.ThreeDimManager;
import tosatto.nextscanner.hardwarecom.HardwareManager;
import tosatto.nextscanner.hardwarecom.INewFrameListener;
import tosatto.nextscanner.hardwarecom.IWebcam;
import tosatto.nextscanner.hardwarecom.IWebcamListener;
import tosatto.nextscanner.hardwarecom.SerialControl;
import tosatto.nextscanner.hardwarecom.usbWebCam;
import tosatto.nextscanner.imaging.*;
import tosatto.nextscanner.main.notifier.EventCategory;
import tosatto.nextscanner.main.notifier.INotificationListener;
import tosatto.nextscanner.main.notifier.Notifier;
import tosatto.nextscanner.main.settings.SettingsManager;
import tosatto.nextscanner.ui.ogl.MyJoglCanvas;
import tosatto.nextscanner.ui.ogl.Renderer;



public class CalibrationThread extends Thread implements INotificationListener{
	
private boolean run = true, finished = false;
int actPos = 0;
	
SettingsFrame main;

	public CalibrationThread(SettingsFrame mw)
	{
		super();
		
		main = mw;
		
		Notifier.get().addListener(this, new EventCategory("webcam:frame"));
	}
	
	@Override
	public void run()
	{

	}
	
	public void finish()
	{
		run = false;
		//while (!finished);
		Notifier.get().removeListener(this);
	}
	
	public void start()
	{
		run = true;
		
		if (!super.isAlive())
			super.start();
	}
	
	public boolean finished()
	{
		return finished;
	}

	@Override
	public void eventRaised(String eName, EventCategory eCat, Object eData) {

		if (eCat.sameCategory(new EventCategory("webcam:frame")))
		{
			if (main.pImg != null)
				main.pImg.setImage((BufferedImage) eData);
		}
		
	}
	

}

