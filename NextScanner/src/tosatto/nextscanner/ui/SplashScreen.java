package tosatto.nextscanner.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import tosatto.nextscanner.imaging.ImagingUtilities;
import tosatto.nextscanner.main.Initialization;

public class SplashScreen extends JFrame implements IInitListener{

	private JLabel log;
	private JProgressBar prog;
	private ImagePanel img;
	private JPanel mainPanel;
	private Initialization i;
	
	public SplashScreen(Initialization i)
	{
		
		
		this.setSize(new Dimension (400, 300));
		this.setUndecorated(true);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		
		this.setLocation((int)width/2 - 200, (int)height/2 - 150 );
		
		this.setAlwaysOnTop(true);
		
		this.setIconImage(ImagingUtilities.getImageFromResource("BlueLogo.png"));
		
		this.i = i;
		
		
		mainPanel = new JPanel(new GridBagLayout());
		
		//Adding image
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 0;
		c.gridy = 0;
		
		c.weightx = 1;
		c.weighty = 0.8;
		
		c.fill = GridBagConstraints.BOTH;
		
		c.gridwidth = 1;
		c.gridheight = 1;
		
		img = new ImagePanel();
		img.setBackground(Color.white);
		img.setImage(ImagingUtilities.getImageFromResource("SplashScreen.png"));
		
		mainPanel.add(img, c);
		mainPanel.setBackground(Color.white);
		
		//Adding label
		c = new GridBagConstraints();
		
		c.gridx = 0;
		c.gridy = 1;
		
		c.weightx = 1;
		c.weighty = 0.05;
		
		c.fill = GridBagConstraints.HORIZONTAL;
		
		c.gridwidth = 1;
		c.gridheight = 1;
		
		log = new JLabel(i.getActTask());
		
		mainPanel.add(log, c);
		
		//Adding progress bar
		c = new GridBagConstraints();
		
		c.gridx = 0;
		c.gridy = 2;
		
		c.weightx = 1;
		c.weighty = 0.02;
		
		c.fill = GridBagConstraints.HORIZONTAL;
		
		c.gridwidth = 1;
		c.gridheight = 1;
		
		prog = new JProgressBar();
		
		prog.setValue((int)(i.getCompletionPercentage()));
		
		mainPanel.add(prog, c);
		
		this.add(mainPanel);
		
		this.setVisible(true);
		
		i.setInitListener(this);
	}

	@Override
	public void initValuesChanged() {

		log.setText(i.getActTask());
		prog.setValue((int)i.getCompletionPercentage());
		
		if (Initialization.isInitialized() == true)
		{
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			this.setVisible(false);
			
		}
		
	}
}
