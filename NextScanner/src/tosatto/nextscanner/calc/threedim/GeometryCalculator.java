package tosatto.nextscanner.calc.threedim;

import java.awt.Color;

import tosatto.geometry.GeometryIntersection;
import tosatto.geometry.GeometryLine;
import tosatto.geometry.GeometryOrtogonal;
import tosatto.geometry.GeometryPlane;
import tosatto.geometry.GeometryPoint;
import tosatto.geometry.GeometrySpace;
import tosatto.geometry.GeometryTransformation;

public class GeometryCalculator implements ICalculator {

	AngleCalculator ac;
	
	GeometryPoint pCam;
	
	GeometryLine lRot;
	GeometryPlane lPlane;
	GeometryLine lCam;
	GeometryLine lCamOrt;
	GeometryPlane hPlane;
	GeometryPlane vPlane;
	GeometryLine lRay;
	
	GeometrySpace gSpace = null;
	
	private void initGeometryObjects ()
	{
		lRot = new GeometryLine(new GeometryPlane(1, 0, 0, 0), new GeometryPlane(0, 1, 0, 0));
		lPlane = new GeometryPlane(0, 1, 0, 0);
		lCam = new GeometryLine(new GeometryPlane(1, 0, 0, -pCam.getX()), new GeometryPlane(0, 1, 0, -pCam.getY()));
		vPlane = new GeometryPlane(new GeometryPoint(0, 0, 0), lCam);
		hPlane = GeometryOrtogonal.getOrtogonalPlane(lCam, pCam);
		lRay = new GeometryLine(hPlane, vPlane);
		lCamOrt = GeometryOrtogonal.getOrtogonalLine(vPlane, pCam);
		
		if (gSpace != null)
		{
			gSpace.addObject("pCam", pCam, Color.yellow);
			gSpace.addObject("lRot", lRot, Color.green);
			gSpace.addObject("lPlane", lPlane, Color.red);
			gSpace.addObject("lCam", lCam, Color.cyan);
			gSpace.addObject("vPlane", vPlane, Color.yellow, false);
			gSpace.addObject("hPlane", hPlane, Color.cyan, false);
			gSpace.addObject("z=0", new GeometryPlane(0, 0, 1, 0), Color.gray);
			gSpace.addObject("lRay", lRay, Color.orange);
			gSpace.addObject("lCamOrt", lCamOrt, Color.blue);
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
		
		double vA = -ac.getVerticalAngle(M);
		double hA = ac.getHorizontalAngle(N);
		
		GeometryPlane hPlaneRot = GeometryTransformation.rotate(hPlane, lCamOrt, vA);
		GeometryPlane vPlaneRot = GeometryTransformation.rotate(vPlane, lCam, hA);
		GeometryLine actRay = new GeometryLine(hPlaneRot, vPlaneRot);
		
		gSpace.addObject("actRay", actRay, Color.green);
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		GeometryPoint res = (GeometryPoint)GeometryIntersection.intersect(lPlane, actRay);
		
		return new Point3D(res.getX(), res.getY(), res.getZ());
	}

}
