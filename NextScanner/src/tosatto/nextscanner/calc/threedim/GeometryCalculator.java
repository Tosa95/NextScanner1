package tosatto.nextscanner.calc.threedim;

import java.awt.Color;

import org.la4j.vector.Vector;
import org.la4j.vector.dense.BasicVector;

import tosatto.geometry.GeometryIntersection;
import tosatto.geometry.GeometryLine;
import tosatto.geometry.GeometryOrtogonal;
import tosatto.geometry.GeometryPlane;
import tosatto.geometry.GeometryPoint;
import tosatto.geometry.GeometrySpace;
import tosatto.geometry.GeometryTransformation;
import tosatto.geometry.GeometryUtils;
import tosatto.nextscanner.main.notifier.EventCategory;
import tosatto.nextscanner.main.notifier.Notifier;
import tosatto.nextscanner.main.settings.SettingsManager;

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
	
	GeometryPoint[][] imgPts;
	
	private void initImgPts ()
	{
		GeometryPlane imgPlane;
		
		GeometryPoint centerRayIntersect = (GeometryPoint)GeometryIntersection.intersect(hPlane, lRot);
		
		double dist = (centerRayIntersect.getDistance(pCam))/3;
		
		double [] pdl = lRay.getDirectorParameters();
		
		pdl = GeometryUtils.normalizeVector(pdl);
		
		Vector v = new BasicVector (pdl);
		
		v = v.multiply(dist);
		
		GeometryPoint pt = GeometryTransformation.translate(pCam, v);
		
		imgPlane = GeometryOrtogonal.getOrtogonalPlane(lRay, pt);
		
		if (gSpace != null)
		{
			gSpace.addObject("imgPlane", imgPlane, Color.yellow, false);
		}
		
		GeometryLine a = getRay(ac.getFOVV()/2, -ac.getFOVH()/2);
		GeometryLine b = getRay(-ac.getFOVV()/2, -ac.getFOVH()/2);
		GeometryLine c = getRay(ac.getFOVV()/2, ac.getFOVH()/2);	
		
		GeometryPoint topLeft = (GeometryPoint)GeometryIntersection.intersect(imgPlane, a);
		GeometryPoint bottomLeft = (GeometryPoint)GeometryIntersection.intersect(imgPlane, b);
		GeometryPoint topRight = (GeometryPoint)GeometryIntersection.intersect(imgPlane, c);
		

		
		GeometryLine l1 = new GeometryLine(topLeft, topRight);
		GeometryLine l2 = new GeometryLine(topLeft, bottomLeft);
		
		double[] pdl1 = GeometryUtils.normalizeVector(l1.getDirectorParameters());
		double[] pdl2 = GeometryUtils.normalizeVector(l2.getDirectorParameters());
		
		double dist1 = topLeft.getDistance(topRight)/ac.getW();
		double dist2 = topLeft.getDistance(bottomLeft)/ac.getH();
		
		Vector v1 = new BasicVector(pdl1);
		Vector v2 = new BasicVector(pdl2);
		
		v1 = v1.multiply(-dist1);
		v2 = v2.multiply(-dist2);
		
		GeometryPoint tr = GeometryTransformation.translate(topLeft, v1);
		tr = GeometryTransformation.translate(tr, v2);
		
		imgPts = new GeometryPoint[(int)ac.getH()][(int)ac.getW()];
		
		for (int i = 0; i < ac.getH(); i++)
		{
			for (int j = 0; j < ac.getW(); j++)
			{
				Vector vH = v2.multiply(i);
				Vector vW = v1.multiply(j);
				
				Vector vAct = vW.add(vH);
				
				imgPts[i][j] = GeometryTransformation.translate(topLeft, vAct);
				
				if (gSpace != null && i%3 == 0 && j%3 == 0)
				{
					
					//gSpace.addObject("tr" + Integer.toString(i) + Integer.toString(j), imgPts[i][j], Color.green);
				}
			}
		}
		
		if (gSpace != null)
		{
			//gSpace.addObject("imgPlane", imgPlane, Color.blue);
			//gSpace.addObject("a", topLeft, Color.red);
			//gSpace.addObject("b", bottomLeft, Color.red);
			//gSpace.addObject("c", topRight, Color.red);
			gSpace.drawScene();
		}
	}
	
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
		
		initImgPts();
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
	
	
	private GeometryLine getRay (double vA, double hA)
	{
		GeometryPlane hPlaneRot = GeometryTransformation.rotate(hPlane, lCamOrt, vA);
		GeometryPlane vPlaneRot = GeometryTransformation.rotate(vPlane, lCam, hA);
		GeometryLine actRay = new GeometryLine(hPlaneRot, vPlaneRot);
		
		return actRay;
	}
	
	private GeometryLine getRay (int M, int N)
	{	
		return new GeometryLine (pCam, imgPts[M][N]);
	}
	
	@Override
	public Point3D calcPosition(int M, int N) {
		
		GeometryLine actRay = getRay(M, N);
		
		if (((int)SettingsManager.get().getValue("GEOMETRY_SLOW"))!=0)
		{
			GeometryPoint act = imgPts[M][N];
			
			gSpace.addObject("actRay", actRay, Color.green);
			gSpace.addObject("actPt", imgPts[M][N], Color.green);
			gSpace.drawScene();
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		GeometryPoint res = (GeometryPoint)GeometryIntersection.intersect(lPlane, actRay);
		
		return new Point3D(res.getX(), res.getY(), res.getZ());
	}

}
