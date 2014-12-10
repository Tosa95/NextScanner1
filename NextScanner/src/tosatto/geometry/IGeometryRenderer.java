package tosatto.geometry;

import java.awt.Color;

public interface IGeometryRenderer {
	
	public void resetScene ();
	
	public void drawTriangle (GeometryPoint p1, GeometryPoint p2, GeometryPoint p3, Color c);
	
	public void drawLine (GeometryPoint p1, GeometryPoint p2, Color c);
	
	public void drawPoint (GeometryPoint p, Color c);
}
