package tosatto.nextscanner.main;
import javax.imageio.ImageIO;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.swing.*;

import tosatto.nextscanner.calc.threedim.PositionCalculator;
import tosatto.nextscanner.hardwarecom.HardwareManager;
import tosatto.nextscanner.imaging.ImageFiltering;
import tosatto.nextscanner.main.settings.SettingsManager;
import tosatto.nextscanner.ui.ImagePanel;
import tosatto.nextscanner.ui.SettingsFrame;
import tosatto.nextscanner.ui.ogl.MyJoglCanvas;

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

public class MainWindow extends JFrame implements ActionListener, WindowListener, MouseMotionListener{
	
	
	
	BufferedImage image, redLine, ris;
	
	Thread update;

	protected volatile ImagePanel pImg, pRis;
	
	private JButton start, stop, save, settings;
	
	private JPanel pImgs, pBtns, pWCam, pCfrnt, pV3D, pSouth;
	
	private PositionCalculator pc;
	
	private int[] values;
	
	private JTabbedPane JTPane;
	
	private ScanningThread MT;
	
	protected MyJoglCanvas View3D;
	
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
		JTPane = new JTabbedPane();
		
		pWCam = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pWCam.setPreferredSize(new Dimension(800, 800));
		pCfrnt = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pCfrnt.setPreferredSize(new Dimension(800, 800));
		pV3D = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pV3D.setPreferredSize(new Dimension(800, 800));
		
		// setup OpenGL Version 2
    	GLProfile profile = GLProfile.get(GLProfile.GL2);
    	GLCapabilities capabilities = new GLCapabilities(profile);
		View3D = new MyJoglCanvas(640, 480, capabilities, MT.getTDM().getRenderer());
		
		
		pWCam.add(pImg);
		pCfrnt.add(pRis);
		pV3D.add(View3D);
		
		JTPane.addTab("Webcam View", pWCam);
		JTPane.addTab("Linea Laser", pCfrnt);
		JTPane.addTab("Vista 3D", pV3D);
		
	}
	
	private void interfaceSetup()
	{
		//SettingsManager.get().loadSettings();
		
		//HardwareManager.get();
		
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
		
		pImgs = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 2));
		
		pBtns = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 2));
		
		pBtns.add(start);
		pBtns.add(stop);
		pBtns.add(save);
		pBtns.add(settings);
		
		this.addWindowListener(this);
		
		this.setSize(1000, 1000);
		
		this.setLayout(new BorderLayout());
		
		createTabbedPane();
		pImgs.add(JTPane);
		
		pSouth = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		ScanningProgress = new JProgressBar(0, 100);
		pSouth.add(ScanningProgress);
		
		this.add(pImgs, BorderLayout.CENTER);
		this.add(pBtns, BorderLayout.PAGE_START);
		this.add(pSouth, BorderLayout.PAGE_END);
		
		pImg.setImage(image);
		
		this.setVisible(true);
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input = classLoader.getResourceAsStream("Icon.png");
		Image logo;
		try {
			logo = ImageIO.read(input);
			this.setIconImage(logo );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public MainWindow ()
	{	
		interfaceSetup();
	    
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
    	HardwareManager.get().close();
    	MT.finish();
    	ThreadManager.killAll();
    	System.out.println ("Erano aperti :" + Integer.toString(ThreadManager.getThreadNumber()) + " thread\n");
    	System.out.println("Resources are now free");
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
}
