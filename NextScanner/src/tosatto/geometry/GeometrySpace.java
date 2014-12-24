package tosatto.geometry;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.la4j.matrix.source.Array1DMatrixSource;

public class GeometrySpace {
	
	private double semisize;
	
	private IGeometryRenderer renderer = null;
	
	private GeometryPlane[] bounds;
	
	private Map<String, GeometryObjectListEntry> oList = new HashMap<String, GeometryObjectListEntry>();
	
	public GeometrySpace (double size)
	{
		bounds = new GeometryPlane[6];
		
		double ss = size/2; //semisize

		semisize = ss;
		
		bounds[0] = new GeometryPlane(1, 0, 0, ss);
		bounds[1] = new GeometryPlane(0, 1, 0, ss);
		bounds[2] = new GeometryPlane(0, 0, 1, ss);

		bounds[3] = new GeometryPlane(1, 0, 0, -ss);
		bounds[4] = new GeometryPlane(0, 1, 0, -ss);
		bounds[5] = new GeometryPlane(0, 0, 1, -ss);
		
	}
	
	public GeometrySpace (double size, IGeometryRenderer r)
	{
		this(size);
		
		renderer = r;
	}
	
	public void drawScene()
	{
		if (renderer != null)
		{
			Map<String, GeometryObjectListEntry> oListC = (Map<String, GeometryObjectListEntry>) (( HashMap<String, GeometryObjectListEntry>)oList).clone();
			
			renderer.resetScene();
			
			Set<String> keySet = oListC.keySet();
			
			for (String s: keySet)
			{
				GeometryObjectListEntry act = oListC.get(s);
				
				if (act.getVisible())
				{
					if (act.getObject().getClass() == GeometryPoint.class)
					{
						renderer.drawPoint((GeometryPoint)(act.getObject()), act.getColor());
					}
					else if (act.getObject().getClass() == GeometryLine.class)
					{
						GeometryLine l = (GeometryLine)act.getObject();
						
						GeometryPoint[] pts = new GeometryPoint[2];
						
						int i = 0;
						
						for (int j = 0; j < bounds.length && i < 2; j++)
						{
							if (GeometryPosition.getPosition(bounds[j], l).isIncident())
							{
								GeometryPoint p = (GeometryPoint)GeometryIntersection.intersect(bounds[j], l);
								
								if (	p.getX()>=-semisize && p.getX()<=semisize &&
										p.getY()>=-semisize && p.getY()<=semisize &&
										p.getZ()>=-semisize && p.getZ()<=semisize && (i == 0 || !GeometryPosition.getPosition(pts[0], p).isCoincident()))
								{
									pts[i++] = p;
								}
							}
						}
						
						if (i == 2)
						{
							renderer.drawLine(pts[0], pts[1], act.getColor());
						}
					}
					else if (act.getObject().getClass() == GeometryPlane.class)
					{
						GeometryPlane pl = (GeometryPlane) act.getObject();
						
						GeometryPoint[] pts = new GeometryPoint[4];
						
						int k = 0;
						
						for (int i = 0; i < 6 && k<4; i++)
						{
							for (int j = 0; j < 6 && k<4; j++)
							{
								if (GeometryPosition.areProperStar(bounds[i], bounds[j], pl))
								{
									GeometryPoint p = (GeometryPoint)GeometryIntersection.intersect(bounds[i], bounds[j], pl);
									
									if (check(pts, p, k))
										pts[k++] = p;
								}
							}
						}
						
						if (k == 4)
						{
							renderer.drawTriangle (pts[0], pts[1], pts[2], act.getColor());
							renderer.drawTriangle(pts[1], pts[2], pts[3], act.getColor());
							renderer.drawTriangle(pts[2], pts[3], pts[0], act.getColor());
						}
					}
				}
			}
		}
	}
	
	private boolean check (GeometryPoint[] pts, GeometryPoint pt, int sz)
	{
		for (int i = 0; i < sz; i++)
		{
			if (GeometryPosition.getPosition(pts[i], pt).isCoincident())
				return false;
		}
		
		if (pt.getX()>=-semisize && pt.getX()<=semisize && pt.getY()>=-semisize && pt.getY()<=semisize && pt.getZ()>=-semisize && pt.getZ()<=semisize)
			return true;
		
		return false;
	}
	
	public void addObject (String n, GeometryObject o, Color c)
	{
		
		oList.put(n, new GeometryObjectListEntry(c, o));
		
		//drawScene();
	}
	
	public void addObject (String n, GeometryObject o, Color c, boolean visible)
	{
		
		oList.put(n, new GeometryObjectListEntry(c, o, visible));
		
		//drawScene();
	}
	
	public void removeObject (String n)
	{
		
		oList.remove(n);
		
		//drawScene();
	}
	
	public void updateObject (String n, GeometryObject newO, Color newC)
	{	
		removeObject(n);
		addObject (n, newO, newC);
	}
	
	public void setVisible (String n, boolean visible)
	{
		
		oList.get(n).setVisible(visible);
	}
	
	public GeometryObject get (String name)
	{
		return oList.get(name).getObject();
	}
	
}