package tosatto.nextscanner.ui;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class SplashScreen extends JFrame {

	private JLabel log;
	private JProgressBar prog;
	private ImagePanel img;
	
	public SplashScreen()
	{
		this.setSize(new Dimension (400, 300));
		this.setUndecorated(true);
		
		
		
	}
}
