package tosatto.nextscanner.hardwarecom;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.*;

import tosatto.nextscanner.main.ThreadManager;
import tosatto.nextscanner.main.notifier.EventCategory;
import tosatto.nextscanner.main.notifier.Notifier;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamMotionDetector;
import com.github.sarxos.webcam.WebcamMotionEvent;
import com.github.sarxos.webcam.WebcamMotionListener;


public class usbWebCam implements IWebcam, Runnable {

	volatile private BufferedImage actFrame = null;
	volatile private List<IWebcamListener> webList = new ArrayList<IWebcamListener>();
	
	volatile int ms, height, width, id;
	
	private volatile Webcam webcam;
	
	volatile boolean run;
	
	volatile Thread t;
	
	private void sendNewFrame ()
	{
		Notifier.get().raiseEvent("WebcamNewFrame", new EventCategory("webcam:frame", 2), actFrame);
	}
	
	static BufferedImage deepCopy(BufferedImage bi) {
		 ColorModel cm = bi.getColorModel();
		 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		 WritableRaster raster = bi.copyData(null);
		 return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
	
	//Funzione che apre il collegamento con la webcam
	public usbWebCam (int fps, int w, int h, int camId)
	{
		id = camId; //Id della webcam specificato in fase di chiamata
		
		
		ms = 1000/fps;
		
		width = w;
		height = h;
		
		run = true;
		
		System.out.println("Opening cam");
		
		webcam = Webcam.getWebcams().get(id); //Riceve l'handle per la webcam
		webcam.setViewSize (new Dimension(width, height)); //Imposta la dimensione
		webcam.open(); //Apre la connessione
		
		
		
		System.out.println("Cam opened");
		
		t = new Thread(this);
		
		ThreadManager.addThread(t);
		
		t.start();
	}
	
	@Override
	public BufferedImage getActFrame() {
		return deepCopy(actFrame);
	}

	@Override
	public void addIWebcamListener(IWebcamListener wL) {
		webList.add(wL);		
	}

	@Override
	//Funzione che legge il frame attuale
	public void run() {
		

		
		while (run)
		{
			actFrame = webcam.getImage();
			
			notifyNewFrame(); //Notifica che è appena stato letto un nuovo frame
			
			try {
				Thread.sleep(ms); //Aspetta del tempo
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

	@Override
	//funzione che chiude il collegamento con la webcam, liberando le risorse
	public void close() {
		
		run = false; //arresta il thread
		
		webcam.close(); //Chiude la connessione
		
	}
	
	synchronized private void notifyNewFrame ()
	{
		if (webList != null)
		{
			for (int i = 0; i < webList.size(); i++)
				webList.get(i).newFrame(actFrame, this);
		}
		
		sendNewFrame();
	}

}
