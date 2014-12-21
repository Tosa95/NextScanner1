package tosatto.nextscanner.main;
import javax.imageio.ImageIO;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.swing.*;

import tosatto.geometry.GeometryLine;
import tosatto.geometry.GeometryOrtogonal;
import tosatto.geometry.GeometryPlane;
import tosatto.geometry.GeometryPoint;
import tosatto.geometry.GeometrySpace;
import tosatto.geometry.GeometryTransformation;
import tosatto.nextscanner.calc.threedim.GeometryCalculator;
import tosatto.nextscanner.calc.threedim.PositionCalculator;
import tosatto.nextscanner.hardwarecom.HardwareManager;
import tosatto.nextscanner.imaging.ImageFiltering;
import tosatto.nextscanner.imaging.ImagingUtilities;
import tosatto.nextscanner.main.notifier.EventCategory;
import tosatto.nextscanner.main.notifier.INotificationListener;
import tosatto.nextscanner.main.notifier.Notifier;
import tosatto.nextscanner.main.settings.SettingsManager;
import tosatto.nextscanner.ui.ImagePanel;
import tosatto.nextscanner.ui.SettingsFrame;
import tosatto.nextscanner.ui.ogl.MyJoglCanvas;
import tosatto.nextscanner.ui.ogl.Renderer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class MainWindow extends JFrame implements ActionListener, WindowListener, MouseMotionListener, INotificationListener{
	
	
	
	BufferedImage image, redLine, ris;
	
	Thread update;

	protected volatile ImagePanel pImg, pRis;
	
	private JButton start, stop, save, settings;
	
	private JPanel pImgs, pBtns, pSouth;
	
	private ImagePanel pWCam, pCfrnt, pV3D, pGV;
	
	private PositionCalculator pc;
	
	private int[] values;
	
	private JTabbedPane JTPane;
	
	private ScanningThread MT;
	
	private GeometrySpace gSpace;
	
	private Renderer gRenderer;
	
	protected MyJoglCanvas View3D, GeometryView;
	
	protected volatile JProgressBar ScanningProgress;
	
	static int getRGB (int r, int g, int b)
	{
	int ris = 0;
	
		ris += (r << 16) 	& 	0x00ff0000;
		ris += (g << 8) 	& 	0x0000ff00;
	    ris += (b) 			& 	0x000000ff;
	
		return ris;
	}
	
	private void createTabbedPane ()
	{
		this.setTitle("NextScanner");
		
		this.setExtendedState(this.getExtendedState()|JFrame.MAXIMIZED_BOTH);
		
		JTPane = new JTabbedPane();
		
		pWCam = new ImagePanel(ImagePanel.RESIZE_IMAGE);
		pWCam.setLayout(new FlowLayout(FlowLayout.LEFT));
		pWCam.setPreferredSize(new Dimension(800, 800));
		pWCam.setBackground(Color.white);
		pWCam.setImage(ImagingUtilities.getImageFromResource("Background.png"));
		
		pCfrnt = new ImagePanel(ImagePanel.RESIZE_IMAGE);
		pCfrnt.setLayout(new FlowLayout(FlowLayout.LEFT));
		pCfrnt.setPreferredSize(new Dimension(800, 800));
		pCfrnt.setBackground(Color.white);
		pCfrnt.setImage(ImagingUtilities.getImageFromResource("Background.png"));
		
		pV3D = new ImagePanel(ImagePanel.RESIZE_IMAGE);
		pV3D.setLayout(new BorderLayout());
		pV3D.setPreferredSize(new Dimension(800, 800));
		pV3D.setBackground(Color.white);
		pV3D.setImage(ImagingUtilities.getImageFromResource("Background.png"));
		
		pGV = new ImagePanel(ImagePanel.RESIZE_IMAGE);
		pGV.setLayout(new BorderLayout());
		pGV.setPreferredSize(new Dimension(800, 800));
		pGV.setBackground(Color.white);
		pGV.setImage(ImagingUtilities.getImageFromResource("Background.png"));
		
		// setup OpenGL Version 2
    	GLProfile profile = GLProfile.get(GLProfile.GL2);
    	GLCapabilities capabilities = new GLCapabilities(profile);
		View3D = new MyJoglCanvas(640, 480, capabilities, MT.getTDM().getRenderer());
		
		
		pWCam.add(pImg);
		pCfrnt.add(pRis);
		pV3D.add(View3D, BorderLayout.CENTER);
		pGV.add(GeometryView, BorderLayout.CENTER);
		
		JTPane.addTab("Webcam View", pWCam);
		JTPane.addTab("Linea Laser", pCfrnt);
		JTPane.addTab("Vista 3D", pV3D);
		JTPane.addTab("Geometria", pGV);
		
		JTPane.setBackground(Color.white);
	}
	
	private void interfaceSetup()
	{
		//SettingsManager.get().loadSettings();
		
		//HardwareManager.get();
		
		gRenderer = new Renderer();
		gSpace = new GeometrySpace(2, gRenderer);
		
    	GLProfile profile = GLProfile.get(GLProfile.GL2);
    	GLCapabilities capabilities = new GLCapabilities(profile);
		GeometryView = new MyJoglCanvas(640, 480, capabilities, gRenderer);
		
		MT = new ScanningThread(this);
		
		pImg = new ImagePanel();
		pRis = new ImagePanel();
		
		pRis.addMouseMotionListener(this);
		
		start = new JButton("Start Scanning");
		stop = new JButton("Stop Scanning");
		save = new JButton("Save");
		settings = new JButton("Settings");
		
		start.addActionListener(this);
		stop.addActionListener(this);
		save.addActionListener(this);
		settings.addActionListener(this);
		
		//pImgs = new JPanel(new GridLayout(0, 2));
		
		pImgs = new JPanel(new BorderLayout());
		
		pBtns = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 2));
		
		pBtns.add(start);
		pBtns.add(stop);
		pBtns.add(save);
		pBtns.add(settings);
		
		this.addWindowListener(this);
		
		this.setSize(1000, 1000);
		
		this.setLayout(new BorderLayout());
		
		createTabbedPane();
		pImgs.add(JTPane, BorderLayout.CENTER);
		
		pSouth = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		ScanningProgress = new JProgressBar(0, 100);
		pSouth.add(ScanningProgress);
		
		this.add(pImgs, BorderLayout.CENTER);
		this.add(pBtns, BorderLayout.PAGE_START);
		this.add(pSouth, BorderLayout.PAGE_END);
		
		pImg.setImage(image);
		
		this.setVisible(true);
		
		this.setIconImage(ImagingUtilities.getImageFromResource("BlueLogo.png"));
	}
	
	public MainWindow ()
	{	
		Notifier.get().addListener(this, new EventCategory("webcam:frame"));
		
		interfaceSetup();
	    
		/*Geometry*/
		
		new GeometryCalculator(Math.PI/4, 320, 240, new GeometryPoint(0.3, 0.1, 0.05), gSpace);
		
		enableScanning();
	}

	void computeRealValues ()
	{
	int n = image.getWidth();
		
		for (int i = 0; i < values.length; i++)
		{
			if (values[i] != -1)
				values[i] -= n/2;
			else
				values[i] = -1000000;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == start)
		{
			startScanning();
		}
		else if (e.getSource() == stop)
		{
			stopScanning();
		}
		else if (e.getSource() == save)
		{
			
		}
		else if (e.getSource() == settings)
		{
			MT.finish();
			start.setEnabled(true);
			stop.setEnabled(false);
			
			SettingsFrame SF = new SettingsFrame();
			
			SF.setVisible(true);
		}
		
	}
	

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		
		Notifier.get().raiseEvent("Application closing", new EventCategory ("application:state", 9), null);
		
    	HardwareManager.get().close();
    	MT.finish();
    	ThreadManager.killAll();
    	
    	Notifier.get().raiseEvent("Application closed. "  + Integer.toString(ThreadManager.getThreadNumber()) + " thread were open.", new EventCategory ("application:state", 9), null);
		
    	System.exit(0);
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent me) {
		
	}

	protected void newView3D ()
	{
		pV3D.remove(View3D);
		
		// setup OpenGL Version 2
    	GLProfile profile = GLProfile.get(GLProfile.GL2);
    	GLCapabilities capabilities = new GLCapabilities(profile);
		View3D = new MyJoglCanvas(640, 480, capabilities, MT.getTDM().getRenderer());
		
		pV3D.add(View3D);
	}
	
	protected synchronized void scanningFinished ()
	{
		enableScanning();
	}
	
	private void enableScanning()
	{
		start.setEnabled(true);
		stop.setEnabled(false);
	}
	
	private void disableScanning()
	{
		start.setEnabled(false);
		stop.setEnabled(true);
	}
	
	private void startScanning ()
	{
		MT = new ScanningThread(this);
		newView3D();
		MT.start();
		disableScanning();
	}
	
	private void stopScanning()
	{
		MT.finish();
		enableScanning();
	}
	
	private void showSettings()
	{
		stopScanning();
		
		SettingsFrame SF = new SettingsFrame();
		
		SF.setVisible(true);
	}

	@Override
	public void eventRaised(String eName, EventCategory eCat, Object eData) {
		
		if (new EventCategory ("webcam:frame").sameCategory(eCat))
		{
			if (pImg != null)
				pImg.setImage((BufferedImage) eData);
		}
		
	}
}
