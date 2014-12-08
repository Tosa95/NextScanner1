package tosatto.nextscanner.imaging;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
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
		
		BufferedImage img = null;
			
		String path = ClassLoader.getSystemResource("").getFile();
		
		path = path.substring(0, path.length() - 4);
		
		path += "res/img/" + name;
		
		try {
			img = ImageIO.read(new File(path));
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
	
	public static BufferedImage resize (BufferedImage img, Dimension newSize)
	{
		int type = img.getType();
		
		if (type == 0)
			type = BufferedImage.TYPE_INT_ARGB;
		
		BufferedImage resizedImage = new BufferedImage((int)newSize.getWidth(), (int)newSize.getWidth(), type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(img, 0, 0, (int)newSize.getWidth(), (int)newSize.getWidth(), null);
		g.dispose();
		
		return resizedImage;
	}
	
	public static BufferedImage resizeMantainingRatio (BufferedImage img, Dimension newSize)
	{
		double w = img.getWidth();
		double h = img.getHeight();
		
		if (newSize.getWidth() != 0 && newSize.getHeight() != 0)
		{
			double ratioH = h/w;
			double ratioW = w/h;
			
			double newH, newW;
			
			newH = newSize.getHeight();
			newW = newH * ratioW;
			
			if (newW > newSize.getWidth())
			{
				newW = newSize.getWidth();
				newH = newW * ratioH;
			}
			
			return resize(img, new Dimension((int)newW, (int)newH));
		}
		else
		{
			return img;
		}
	}
	
	
}
