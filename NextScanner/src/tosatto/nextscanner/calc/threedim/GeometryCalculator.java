package tosatto.nextscanner.calc.threedim;

import tosatto.geometry.GeometryPoint;

public class GeometryCalculator implements ICalculator {

	AngleCalculator ac;
	
	GeometryPoint camPos;
	
	public GeometryCalculator(double fovh, double fovv, double w, double h, GeometryPoint camPos) {
		this.camPos = camPos;
		ac = new AngleCalculator(fovh, fovv, w, h);
	}
	
	public GeometryCalculator(double fovv, double w, double h, GeometryPoint camPos) {
		this.camPos = camPos;
		ac = new AngleCalculator(fovv, w, h);
	}
	
	
	
	@Override
	public Point3D calcPosition(int M, int N) {
		// TODO Auto-generated method stub
		return null;
	}

}
