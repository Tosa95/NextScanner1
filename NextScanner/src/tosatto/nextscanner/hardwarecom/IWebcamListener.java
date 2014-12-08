package tosatto.nextscanner.hardwarecom;
import java.awt.image.*;

public interface IWebcamListener {
	void newFrame (BufferedImage I, IWebcam source);
}
