package tosatto.nextscanner.calc.threedim;

import java.awt.Color;

import tosatto.geometry.GeometryLine;
import tosatto.geometry.GeometryOrtogonal;
import tosatto.geometry.GeometryPlane;
import tosatto.geometry.GeometryPoint;
import tosatto.geometry.GeometrySpace;

public class GeometryCalculator implements ICalculator {

	AngleCalculator ac;
	
	GeometryPoint pCam;
	
	GeometryLine lRot;
	GeometryPlane lPlane;
	GeometryLine lCam;
	GeometryPlane hPlane;
	GeometryPlane vPlane;
	
	GeometrySpace gSpace = null;
	
	private void initGeometryObjects ()
	{
		lRot = new GeometryLine(new GeometryPlane(1, 0, 0, 0), new GeometryPlane(0, 1, 0, 0));
		lPlane = new GeometryPlane(0, 1, 0, 0);
		lCam = new GeometryLine(new GeometryPlane(1, 0, 0, pCam.getX()), new GeometryPlane(0, 1, 0, pCam.getY()));
		vPlane = new GeometryPlane(new GeometryPoint(0, 0, 0), lCam);
		hPlane = GeometryOrtogonal.getOrtogonalPlane(lCam, pCam);
		
		if (gSpace != null)
		{
			//gSpace.addObject("pCam", pCam, Color.yellow);
			//gSpace.addObject("lRot", lRot, Color.green);
			//gSpace.addObject("lPlane", lPlane, Color.red);
			//gSpace.addObject("lCam", lCam, Color.cyan);
			//gSpace.addObject("vPlane", vPlane, Color.gray);
			gSpace.addObject("hPlane", hPlane, Color.cyan);
			gSpace.addObject("z=0", new GeometryPlane(0, 0, 1, 0), Color.gray);
		}
	}
	
	public GeometryCalculator(double fovh, double fovv, double w, double h, GeometryPoint camPos, GeometrySpace gS) {
		this.pCam = camPos;
		ac = new AngleCalculator(fovh, fovv, w, h);
		gSpace = gS;
		initGeometryObjects();
	}
	
	public GeometryCalculator(double fovv, double w, double h, GeometryPoint camPos, GeometrySpace gS) {
		this.pCam = camPos;
		ac = new AngleCalculator(fovv, w, h);
		gSpace = gS;
		initGeometryObjects();
	}
	
	
	
	@Override
	public Point3D calcPosition(int M, int N) {
		// TODO Auto-generated method stub
		return null;
	}

}
