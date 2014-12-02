package tosatto.nextscanner.calc.threedim;

public class VerticalIndexer {
	
	private int height; //Altezza
	private double resolution; //Risoluzione di scanning
	private double step; //Passo di incremento
	private double num; //Numero di step
	
	private double index; //Indice attuale
	
	public VerticalIndexer (int h, double res)
	{
		height = h;
		resolution = res;
		
		index = 0;
		
		num = (height * resolution)/100.0;
		
		step = height/num;
	}
	
	public void next()
	{
		index += step;
	}
	
	public int act()
	{
		return (int)Math.round(index);
	}
	
	public int getNum ()
	{
		return (int)Math.round(num);
	}
	
	public void reset()
	{
		index = 0;
	}
	
	public boolean complete()
	{
		return act() >= height;
	}
}
