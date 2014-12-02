package tosatto.nextscanner.imaging;

import java.awt.image.BufferedImage;

public class ImageManager {
	
	private int[][] gMtrx;
	
	private static final int GAUSS_SIZE = 5;
	private static final int GAUSS_CONST = 100;
	private static final int GAUSS_VAR = 1;
	
	private static ImageManager IM = null;
	
	private void initVariables ()
	{
		gMtrx = ImageFiltering.getGaussianMatrixInt(GAUSS_SIZE, GAUSS_CONST, GAUSS_VAR);
	}
	
	public BufferedImage reduceNoise (BufferedImage img)
	{
		return ImageFiltering.filterImage(img, gMtrx);
	}
	
	private ImageManager()
	{
		initVariables();
	}
	
	public static ImageManager get()
	{
		if (IM == null)
			IM = new ImageManager();
		
		return IM;
	}
}
