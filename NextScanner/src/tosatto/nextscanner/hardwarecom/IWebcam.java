package tosatto.nextscanner.hardwarecom;
import java.awt.image.*;

public interface IWebcam {
	BufferedImage getActFrame();
	void addIWebcamListener (IWebcamListener wL);
	void close ();
}
