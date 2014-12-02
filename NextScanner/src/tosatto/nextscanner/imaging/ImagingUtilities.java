package tosatto.nextscanner.imaging;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class ImagingUtilities {
	
	public static BufferedImage deepCopy(BufferedImage bi) {
		 ColorModel cm = bi.getColorModel();
		 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		 WritableRaster raster = bi.copyData(null);
		 return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
	
	public static int getRGB (int r, int g, int b)
	{
	int ris = 0;
	
		ris += (r << 16) 	& 	0x00ff0000;
		ris += (g << 8) 	& 	0x0000ff00;
	    ris += (b) 			& 	0x000000ff;
	
		return ris;
	}
	
	public static BufferedImage getImageFromResource (String name)
	{
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input = classLoader.getResourceAsStream(name);
		BufferedImage img = null;
		try {
			img = ImageIO.read(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return img;
	}
	
	public static BufferedImage setImageAlpha (BufferedImage img, int alpha)
	{
		BufferedImage ris;
		
		int width = img.getWidth();
		
		int[] imgData = new int[width];
		
		for (int y = 0; y < img.getHeight(); y++)
		{
			img.getRGB(0, y, width, 1, imgData, 0, 1);
			
			for (int x = 0; x < width; x++)
			{
				int color = imgData[x];
				color &= 0x00FFFFFF;
				color |= alpha<<24;
				imgData[x] = color;
			}
			
			img.setRGB(0, y, width, 1, imgData, 0, 1);
		}
		
		
		return img;
	}
	
	
}
