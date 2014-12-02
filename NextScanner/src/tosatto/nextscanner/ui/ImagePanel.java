package tosatto.nextscanner.ui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import tosatto.nextscanner.imaging.ImagingUtilities;

public class ImagePanel extends JPanel{

    private volatile BufferedImage image;
    private volatile int imagePosition;
    private volatile int adapt;
    private volatile int alpha;
    
    public static final int TOPLEFT = 0;
    public static final int CENTER = 1;
    
    public static final int ADAPT_TO_IMAGE = 0;
    public static final int DON_NO_ADAPT = 1;
    
    public ImagePanel() {
    	
    	this(ADAPT_TO_IMAGE);
    }
    
    public ImagePanel (int adaptionType)
    {
    	adapt = adaptionType;
    	imagePosition = CENTER;
    	alpha = 255;
    }
    
    public synchronized void  setAlpha (int a)
    {
    	alpha = a;
    	
    	if (image != null)
    	{
    		ImagingUtilities.setImageAlpha(image, a);
    	}
    	
    	this.validate();
    	this.repaint();
    }
    
    public synchronized BufferedImage getImage ()
    {
    	return image;
    }
    
    public synchronized void setImage (BufferedImage img)
    {
    	image = img;
    	if (img != null && adapt == ADAPT_TO_IMAGE)
		{
    		this.setSize(img.getWidth(), img.getHeight());
    		this.setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
		}
    	
    	if (img != null && alpha != 255)
    	{
    		ImagingUtilities.setImageAlpha (image, alpha);
    	}
    	
    	this.validate();
    	this.repaint();
    }

    public synchronized void setImagePosition (int pos)
    {
    	imagePosition = pos;
    }
    
    public synchronized int getImagePosition ()
    {
    	return imagePosition;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
    	
    	super.paintComponent(g);
    	
    	g.setColor(this.getBackground());
    	g.fillRect(0, 0, this.getWidth(), this.getHeight());
    	
    	
    	
        int x = 0, y = 0;
        
        if (image != null)
        {
	        if (imagePosition == CENTER)
	        {
	        	
	        	x = this.getWidth()/2 - image.getWidth()/2;
	        	y = this.getHeight()/2 - image.getHeight()/2;
	        }
	        else if (imagePosition == TOPLEFT)
	        {
	        	x = y = 0;
	        }
        }
        
        g.drawImage(image, x, y, null);
        
        
        /*Color c = this.getBackground();
        this.setBackground(new Color(255, 255, 255, 0));
        super.paintComponents(g);
        this.setBackground(c);*/
        
        
    }
}
