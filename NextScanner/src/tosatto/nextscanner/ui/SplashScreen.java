package tosatto.nextscanner.ui;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import tosatto.nextscanner.main.Initialization;

public class SplashScreen extends JFrame implements IInitListener{

	private JLabel log;
	private JProgressBar prog;
	private ImagePanel img;
	
	public SplashScreen(Initialization i)
	{
		this.setSize(new Dimension (400, 300));
		this.setUndecorated(true);
		
		
		
	}

	@Override
	public void initValuesChanged() {

		log.setText();
		
	}
}
