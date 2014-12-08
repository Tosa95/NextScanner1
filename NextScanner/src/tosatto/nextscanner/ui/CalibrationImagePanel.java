package tosatto.nextscanner.ui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


public class CalibrationImagePanel extends ImagePanel implements MouseListener, MouseMotionListener{

	Point p1, p2;
	
	Dimension rectDim = new Dimension(7, 7);
	
	int actClicked = 0;
	
	PointsChangedListener pcl = null;
	
	public void setPointsChangedListener (PointsChangedListener l)
	{
		pcl = l;
	}
	
	private void pointsChanged()
	{
		if (pcl != null)
			pcl.PointsChanged();
		
		this.repaint();
	}
	
	public CalibrationImagePanel ()
	{
		super(ImagePanel.DO_NOT_ADAPT);
		
		super.setImagePosition(ImagePanel.TOPLEFT);
		
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		
		p1 = new Point(0, 0);
		p2 = new Point(0, 0);
		
		this.setBackground(new Color (255, 255, 255, 0));
	}

	private int pointClicked (int x, int y)
	{
		if (x>=0 && x<rectDim.getWidth())
		{
			if (y>=(int)(p1.getY()-rectDim.getHeight()/2) && y<(int)(p1.getY()+rectDim.getHeight()/2))
			{
				return 1;
			}
			else if (y>=(int)(p2.getY()-rectDim.getHeight()/2) && y<(int)(p2.getY()+rectDim.getHeight()/2))
			{
				return 2;
			}
		}
		
		return 0;
	}
	
	@Override
	public void mouseClicked(MouseEvent m) {

		
		
		
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent m) {
		// TODO Auto-generated method stub
		
		actClicked = pointClicked(m.getX(), m.getY());
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public Point getPointLow (){
		
		if (p1.getY()>p2.getY())
			return new Point(p1);
		else
			return new Point(p2);
	}
	
	public Point getPointHigh (){
		
		if (p1.getY()<p2.getY())
			return new Point(p1);
		else
			return new Point(p2);
	}
	
	public void setPointLow (Point p){
		
		p1 = p;
	}
	
	public void setPointHigh (Point p)
	{
		p2 = p;
	}

	
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (super.getImage() != null)
        {
	        int height = super.getImage().getHeight();
	        int width = super.getImage().getWidth();
	        
	        g.setColor(Color.green);
	        g.drawLine(0, p2.y, width, p2.y);
	        g.fillRect(0, (int)(p2.getY()-rectDim.getHeight()/2), (int)rectDim.getWidth(), (int)rectDim.getHeight());
	        
	        g.setColor(Color.red);
	        g.drawLine(0, p1.y, width, p1.y);
	        g.fillRect(0, (int)(p1.getY()-rectDim.getHeight()/2), (int)rectDim.getWidth(), (int)rectDim.getHeight());
	        
	        g.setColor(Color.yellow);
	        g.drawLine(width/2, 0, width/2, height);
        }
    }

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		switch (actClicked)
		{
			case 1:
				
				p1.setLocation(e.getX(), e.getY());
				
				break;
				
			case 2:
				
				p2.setLocation(e.getX(), e.getY());
				
				break;
		}
		
		pointsChanged();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
