package tosatto.nextscanner.ui.ogl;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.*;
import javax.media.opengl.glu.GLU;

import tosatto.nextscanner.calc.threedim.Point3D;

import com.jogamp.opengl.util.FPSAnimator;


public class MyJoglCanvas extends GLCanvas implements MouseListener, MouseMotionListener, MouseWheelListener{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private FPSAnimator animator;
	
	boolean mouseClicked = false;
	
	private Point mIPos;
	
	private Renderer rend;
	
	public MyJoglCanvas(int width, int height, GLCapabilities capabilities, Renderer r) 
	{
		super(capabilities);
        setSize(width, height);
        this.addGLEventListener(r);
        
        // Start animator (which should be a field).
        animator = new FPSAnimator(this, 60);
        animator.start();
        
        rend = r;
        
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
    }

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		double dX = mIPos.getX() - e.getX();
		double dY = mIPos.getY() - e.getY();
		rend.setZAngle(dX*0.05);
		rend.setXAngle(dY*0.05);
		
		mIPos = new Point(e.getX(), e.getY());
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

		
		if (mouseClicked)
		{
			/*double dX = mIPos.getX() - e.getX();
			double dY = mIPos.getY() - e.getY();
			rend.setZAngle(dX);
			System.out.println(dX);*/
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		mouseClicked = true;
		
		mIPos = new Point(e.getX(), e.getY());
		
		System.out.println("Pressed");
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		//mouseClicked = false;
	}
	
	public void updateRenderer (Renderer newR)
	{
		if (rend != null)
			this.removeGLEventListener(rend);
		this.addGLEventListener(newR);
		rend = newR;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		
		rend.setDist(e.getWheelRotation()*0.1);
		
	}
    

}
