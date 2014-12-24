package tosatto.nextscanner.ui;

import javax.imageio.ImageIO;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import tosatto.nextscanner.calc.imgcmp.ImageComparer;
import tosatto.nextscanner.calc.threedim.PositionCalculator;
import tosatto.nextscanner.calc.threedim.ThreeDimManager;
import tosatto.nextscanner.hardwarecom.HardwareManager;
import tosatto.nextscanner.imaging.ImageFiltering;
import tosatto.nextscanner.imaging.ImagingUtilities;
import tosatto.nextscanner.main.settings.SettingsManager;
import tosatto.nextscanner.ui.ImagePanel;
import tosatto.nextscanner.ui.ogl.MyJoglCanvas;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageFilter;
import java.io.IOException;
import java.io.InputStream;

public class SettingsFrame extends JFrame implements ActionListener, WindowListener, MouseMotionListener, PointsChangedListener, ChangeListener{
	
	
	BufferedImage image, redLine, cmp1, cmp2, ris;
	
	Thread update;

	protected volatile CalibrationImagePanel pImg; //Immagine di calibrazione
	
	private JButton save;
	
	private JPanel pBtns;	//Bottoni (NORTH)
	private ImagePanel pCalib;	//Pannello principale tab Calibrazione
	private JPanel pCalPar;	//Parametri di calibrazione
	private ImagePanel pResolution; //Controlli di settaggio risoluzione
	
	private JTextField point1, point2; //Campi di testo contenenti le coordinate delle due linee
	
	private JLabel guide; //Guida rapida di calibrazione
	
	private JTabbedPane JTPane; //Pannello a tab
	
	private JSlider resolution; //Slider di controllo della risoluzione
	private JLabel resL; //Label per la risoluzione
	
	private JSlider vRes; //Slider di controllo della risoluzione verticale
	private JLabel vResL; //Label per la risoluzione verticale
	
	CalibrationThread ct;
	
	private void printPoints ()
	{
		point1.setText("Linea 1: " + Integer.toString((int)pImg.getPointHigh().getY()) + "px");
		point2.setText("Linea 2: " + Integer.toString((int)pImg.getPointLow().getY()) + "px");
		
		point1.updateUI();
		
	}
	
	private void createTabbedPane ()
	{
		JTPane = new JTabbedPane();
		
		pImg.setPreferredSize(new Dimension((int)SettingsManager.get().getValue("WCAM_WIDTH"), (int)SettingsManager.get().getValue("WCAM_HEIGHT")));
		
		pCalib = new ImagePanel(ImagePanel.RESIZE_IMAGE);
		pCalib.setLayout(new GridLayout(2, 0));
		pCalib.setImage(ImagingUtilities.getImageFromResource("Background.png"));
		pCalib.setBackground(Color.white);
		pCalib.setPreferredSize(new Dimension(640, 480));
		
		pCalPar = new JPanel(new GridBagLayout());
		pCalPar.setBackground(new Color(255, 255, 255, 0));
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 0;
		c.gridy = 0;
		
		c.weightx = 0.5;
		c.weighty = 0.1;
		
		c.fill = GridBagConstraints.HORIZONTAL;
		
		c.gridwidth = 1;
		c.gridheight = 1;
		
		pCalPar.add(point1, c);
		
		c = new GridBagConstraints();
		
		c.gridx = 0;
		c.gridy = 1;
		
		c.fill = GridBagConstraints.HORIZONTAL;
		
		c.weightx = 0.5;
		c.weighty = 0.1;
		
		c.gridwidth = 1;
		c.gridheight = 1;
		
		pCalPar.add(point2, c);
		
		c = new GridBagConstraints();
		
		c.gridx = 0;
		c.gridy = 2;
		
		c.fill = GridBagConstraints.HORIZONTAL;
		
		c.weightx = 1;
		c.weighty = 1;
		
		c.gridwidth = 2;
		c.gridheight = 1;
		
		pCalPar.add(guide, c);
		
		pCalib.add(pImg);
		pCalib.add(pCalPar);
		

		
		
		
		JTPane.addTab("Calibration", pCalib);
		
		pResolution = new ImagePanel(ImagePanel.RESIZE_IMAGE);
		pResolution.setLayout(new GridLayout(2, 0));
		pResolution.setBackground(Color.white);
		pResolution.setImage(ImagingUtilities.getImageFromResource("Background.png"));
		
		resL = new JLabel();
		
		resL.setText("Risoluzione angolare: ");
		
		c = new GridBagConstraints();
		
		c.gridx = 0;
		c.gridy = 0;
		
		c.fill = GridBagConstraints.HORIZONTAL;
		
		c.weightx = 0.5;
		c.weighty = 0.1;
		
		c.gridwidth = 1;
		c.gridheight = 1;
		
		pResolution.add(resL, c);
		
		resolution = new JSlider(JSlider.HORIZONTAL, 0, 100, (int)(double)SettingsManager.get().getValue("RESOLUTION"));
		
		resolution.setBackground(new Color (255,255,255,0));
		resolution.addChangeListener(this);
		
		resolution.setMinorTickSpacing(1);
		resolution.setMajorTickSpacing(10);
		resolution.setPaintTicks(true);
		resolution.setPaintLabels(true);
		
		c = new GridBagConstraints();
		
		c.gridx = 1;
		c.gridy = 0;
		
		c.fill = GridBagConstraints.HORIZONTAL;
		
		c.weightx = 0.5;
		c.weighty = 0.1;
		
		c.gridwidth = 1;
		c.gridheight = 1;
		
		c.ipadx = 20;
		
		pResolution.add(resolution, c);
		
		
		
		
		vResL = new JLabel();
		
		vResL.setText("Risoluzione verticale: ");
		
		c = new GridBagConstraints();
		
		c.gridx = 0;
		c.gridy = 1;
		
		c.fill = GridBagConstraints.NONE;
		
		c.weightx = 0.5;
		c.weighty = 0.1;
		
		c.gridwidth = 1;
		c.gridheight = 1;
		
		pResolution.add(vResL, c);
		
		vRes = new JSlider(JSlider.HORIZONTAL, 0, 100, (int)(double)SettingsManager.get().getValue("VERT_RES"));
		
		vRes.addChangeListener(this);
		
		vRes.setBackground(new Color (255, 255, 255, 0));
		
		//vRes.setPreferredSize(new Dimension (1000, 30));
		
		vRes.setMinorTickSpacing(1);
		vRes.setMajorTickSpacing(10);
		vRes.setPaintTicks(true);
		vRes.setPaintLabels(true);
		
		c = new GridBagConstraints();
		
		c.gridx = 1;
		c.gridy = 1;
		
		c.fill = GridBagConstraints.HORIZONTAL;
		
		c.weightx = 0.5;
		c.weighty = 0.1;
		
		c.gridwidth = 1;
		c.gridheight = 1;
		
		c.ipadx = 20;
		
		pResolution.add(vRes, c);
		
		JTPane.addTab ("Risoluzione", pResolution);
		
	}
	
	public SettingsFrame ()
	{	
		this.setTitle("Impostazioni");
		
		this.addWindowListener(this);
		
		ct = new CalibrationThread(this);
		
		pImg = new CalibrationImagePanel();
		pImg.addMouseMotionListener(this);
		pImg.setPointsChangedListener(this);
		pImg.setPointLow(new Point (0, (int)(double)SettingsManager.get().getValue("Z_MIN_PX")));
		
		
		point1 = new JTextField();
		point2 = new JTextField();
		
		printPoints();
		
		guide = new JLabel("<html>"
						+ "Istruzioni di calibrazione:<br>"
						+ "1. Posizionare l'apposito supporto sul piano girevole, in modo perpendicolare al piano del laser<br>"
						+ "2. Ruotare il laser facendo si che la line proiettata coincida con quella segnata al centro del supporto<br>"
						+ "3. Muovere la webcam in modo da allineare il centro (linea gialla) con la linea laser<br>"
						+ "4. Spostare la linea rossa (trascinandola da sx) in modo che coincida con la barra più bassa del support<br>"
						+ "5. Spostare la linea verde (trascinandola da sx) in modo che coincida con la barra più alta del supporto<br>"
						+ "<font color='red'>ATTENZIONE</font>: La precisione della calibrazione influisce fortemente sui risultati. Effetture con cura<br>"
						+ "Fine. Have fun with NextScanner</html>");
		
		save = new JButton("Save");
		
		save.addActionListener(this);
		
		pBtns = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 2));
		
		pBtns.add(save);
		
		this.addWindowListener(this);
		
		this.setSize(1000, 1000);
		
		this.setLayout(new BorderLayout());
		
		createTabbedPane();
		
		this.add(JTPane, BorderLayout.CENTER);
		this.add(pBtns, BorderLayout.PAGE_START);
		
		pImg.setImage(image);
		
		this.setVisible(true);


		this.setIconImage(ImagingUtilities.getImageFromResource("BlueLogo.png"));
	    
		

	}

	
	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == save)
		{
			SettingsManager.get().updateSetting("RESOLUTION", (double)resolution.getValue());
			SettingsManager.get().updateSetting("VERT_RES", (double)vRes.getValue());
			SettingsManager.get().updateSetting("Z_MIN_PX", pImg.getPointLow().y);
			
			ThreeDimManager TDM = new ThreeDimManager();
			
			HardwareManager.get().captureAct();
			
			BufferedImage img1 = HardwareManager.get().getLaserONPic();
			BufferedImage img2 = HardwareManager.get().getLaserOFFPic();
			BufferedImage res = new BufferedImage(img1.getWidth(), img1.getHeight(), BufferedImage.TYPE_INT_RGB);
			
			int[] values = ImageComparer.compare(img1, img2, res, 100);
			
			double z = TDM.getZ(new Point(values[(int)pImg.getPointLow().getY()], (int)pImg.getPointLow().getY()));
			
			SettingsManager.get().updateSetting("Z_MIN", z);
			
			SettingsManager.get().saveSettings();
		}
		else if (e.getSource() == pImg)
		{
			printPoints();
		}
		
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		HardwareManager.get().getLaser().beamON();
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		HardwareManager.get().getLaser().beamOFF();
		ct.finish();
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		HardwareManager.get().getLaser().beamOFF();
		ct.finish();
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

	@Override
	public void PointsChanged() {
		// TODO Auto-generated method stub
		printPoints();
		
		this.repaint();
		
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		// TODO Auto-generated method stub
		
		this.repaint();
	}
}
