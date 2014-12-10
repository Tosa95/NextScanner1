package tosatto.geometry;

import java.awt.Color;

class GeometryObjectListEntry {
	private Color c;
	private GeometryObject o;
	private boolean visible;
	
	public GeometryObjectListEntry(Color color, GeometryObject obj) {
		this(color, obj, true);
	}
	
	public GeometryObjectListEntry(Color color, GeometryObject obj, boolean v) {
		c = color;
		o = obj;
		visible =v;
	}
	
	public Color getColor()
	{
		return c;
	}
	
	public GeometryObject getObject()
	{
		return o;
	}
	
	public void setColor (Color color)
	{
		c = color;
	}
	
	public void setObject (GeometryObject obj)
	{
		o = obj;
	}
	
	public void setVisible (boolean v)
	{
		visible = v;
	}
	
	public boolean getVisible ()
	{
		return visible;
	}
	
}
