package tosatto.nextscanner.calc.threedim;
import java.awt.Point;


public class ProportionalCalculator implements ICalculator {

	private double _dFar, _dClose, _XProp, _ZProp;
	
	public ProportionalCalculator ()
	{
		
	}
	
	public void calibrate (Point pFar1, Point pFar2, double dFar, Point pClose1, Point pClose2, double dClose, double oHeight)
	{
		_dFar = dFar;
		_dClose = dClose;
		
		_XProp = pFar1.x - pClose1.x;
		
		_ZProp = (dFar/(pFar1.y - pFar2.y))*oHeight;
		
		
	}
	
	@Override
	public Point3D calcPosition(double x, double y, double angle) {
		// TODO Auto-generated method stub
		return null;
	}

}
