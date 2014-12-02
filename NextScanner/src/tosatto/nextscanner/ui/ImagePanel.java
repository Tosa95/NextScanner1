package tosatto.nextscanner.ui;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel{

    private volatile BufferedImage image;
    
    public ImagePanel() {
    }
    
    public synchronized BufferedImage getImage ()
    {
    	return image;
    }
    
    public synchronized void setImage (BufferedImage img)
    {
    	image = img;
    	if (img != null) this.setSize(img.getWidth(), img.getHeight());
    	this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters            
    }
}
