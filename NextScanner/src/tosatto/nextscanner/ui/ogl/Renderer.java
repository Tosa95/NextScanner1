package tosatto.nextscanner.ui.ogl;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import tosatto.nextscanner.calc.threedim.Point3D;
import tosatto.nextscanner.calc.threedim.Point3DManipulationUtilities;
 
public class Renderer implements GLEventListener 
{
    private GLU glu = new GLU();
 
    private List<Point3D []> faces;
    
    private GLAutoDrawable glAD = null;
    
    private double xAngle = 0;
    private double zAngle = 0;
    private double pos = 2.0;
    private double dPos = 0, dXAngle = 0, dZAngle = 0;
    
    private int _x, _y, _width, _height;
    
    private Point3D cam = new Point3D(pos, 0, 0);
    
    private boolean reset = true;
    
    public Renderer ()
    {
    	clear();
    	
    	//Point3D [] face1 = {new Point3D(0.1, 0.0, 0.0), new Point3D(0.0, 0.1, 0.0), new Point3D(0.0, 0.0, 0.1)};
    	
    	//AddFace(face1);
    }
    
    public void AddFace (Point3D[] points)
    {
    	faces.add(points);
    }
    
    public void clear()
    {
    	faces = new ArrayList<Point3D[]>();

    	reset = true;
    }
    
    public void display(GLAutoDrawable gLDrawable) 
    {
    	
    	glAD = gLDrawable;
        GL2 gl = gLDrawable.getGL().getGL2();
        
        if (reset)
        {
        	gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT | GL2.GL_ACCUM_BUFFER_BIT | GL2.GL_STENCIL_BUFFER_BIT);
        	reset = false;
        }
        else
        	gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT | GL2.GL_ACCUM_BUFFER_BIT);
        
        gl.glLoadIdentity();
        
        reshape(gLDrawable,_x, _y, _width, _height);
        
     // Global settings.
        gl.glEnable(GL2.GL_DEPTH_TEST);
        //gl.glDepthFunc(GL2.GL_LEQUAL);
        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
        gl.glClearColor(0f, 0f, 0f, 1f);
        
     // Prepare light parameters.
        float SHINE_ALL_DIRECTIONS = 1;
        float[] lightPos = {0, -3, 1, SHINE_ALL_DIRECTIONS};
        float[] lightColorAmbient = {0.1f, 0.1f, 0.1f, 1f};
        float[] lightColorSpecular = { 1f, 0.6f, 0f, 1f };
        float[] lightColorDiffuse = {1f, 1f, 1f, 1f};
        
        // Set light parameters.
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, lightPos, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, lightColorAmbient, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, lightColorSpecular, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, lightColorDiffuse, 0);
        
        // Enable lighting in GL.
        gl.glEnable(GL2.GL_LIGHT1);
        gl.glEnable(GL2.GL_LIGHTING);

        // Set material properties.
        float[] rgba = {1f, 0f, 0f};
        gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, rgba, 0);
        gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, rgba, 0);
        gl.glMaterialf(GL.GL_FRONT_AND_BACK, GL2.GL_SHININESS, 0.5f);
        
        for (int i = 0; i < faces.size(); i++)
        {
        	Point3D p1 = faces.get(i)[0];
        	Point3D p2 = faces.get(i)[1];
        	Point3D p3 = faces.get(i)[2];
        	
        	
        	gl.glColor3f(0.1f, 0.1f, 0.1f);
        	
        	gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE); 
        	
        	float[] rgbae = {1f, 1f, 1f};
	        gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, rgbae, 0);
	        gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, rgbae, 0);
	        gl.glMaterialf(GL.GL_FRONT_AND_BACK, GL2.GL_SHININESS, 0.5f);
        	
            gl.glBegin(GL2.GL_TRIANGLES);		
            gl.glVertex3f((float)p1.getX()*3,(float)p1.getY()*3,(float)p1.getZ()*3);	
            gl.glVertex3f((float)p2.getX()*3,(float)p2.getY()*3,(float)p2.getZ()*3);	
            gl.glVertex3f((float)p3.getX()*3,(float)p3.getY()*3,(float)p3.getZ()*3);	
            gl.glEnd();
            
            //Imposta il colore
            gl.glColor3f(0.5f, 0.5f, 0.5f);
        	
            //Imposta la modalità di disegno
        	gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL); 
        	
        	//Imposta i parametri del materiale di cu è composta la faccia
        	float[] rgbaf = {1f, 0f, 0f};
	        gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, rgbaf, 0);
	        gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, rgbaf, 0);
	        gl.glMaterialf(GL.GL_FRONT_AND_BACK, GL2.GL_SHININESS, 0.5f);
        	
	        //Inizia a disegnare un triangolo
            gl.glBegin(GL2.GL_TRIANGLES);
            
            //Inserisce i 3 vertici
            gl.glVertex3f((float)p1.getX()*3,(float)p1.getY()*3,(float)p1.getZ()*3);	
            gl.glVertex3f((float)p2.getX()*3,(float)p2.getY()*3,(float)p2.getZ()*3);	
            gl.glVertex3f((float)p3.getX()*3,(float)p3.getY()*3,(float)p3.getZ()*3);	
            
            //Finalizza il rendering del triangolo
            gl.glEnd();
        }
        
        float[] rgbap = {0.3f, 0.3f, 0.3f};
        gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, rgbap, 0);
        gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, rgbap, 0);
        gl.glMaterialf(GL.GL_FRONT_AND_BACK, GL2.GL_SHININESS, 0.5f);
    	
        gl.glBegin(GL2.GL_QUADS);		
        gl.glVertex3f(-1, -1, 0);	
        gl.glVertex3f(-1, 1, 0);	
        gl.glVertex3f(1, 1, 0);
        gl.glVertex3f(1, -1, 0);
        gl.glEnd();
    }
 
    public void display ()
    {
    	display(glAD);
    }
    
    public void displayChanged(GLAutoDrawable gLDrawable, boolean modeChanged, boolean deviceChanged) 
    {
    	System.out.println("displayChanged called");
    }
 
    public void init(GLAutoDrawable gLDrawable) 
    {
    	System.out.println("init() called");
        GL2 gl = gLDrawable.getGL().getGL2();
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glShadeModel(GL2.GL_FLAT);
    }
 
    public void reshape(GLAutoDrawable gLDrawable, int x, int y, int width, int height) 
    {
    	_x = x;
    	_y = y;
    	_width = width;
    	_height = height;
    	
    	//System.out.println("reshape() called: x = "+x+", y = "+y+", width = "+width+", height = "+height);
        final GL2 gl = gLDrawable.getGL().getGL2();
 
        if (height <= 0) // avoid a divide by zero error!
        {
            height = 1;
        }
 
        final float h = (float) width / (float) height;
 
        
        
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0f, h, 1.0, 20.0);
      
        pos += dPos;
        
        cam = new Point3D(pos, 0, 0);
        
        xAngle += dXAngle;
        zAngle += dZAngle;
        
        cam = Point3DManipulationUtilities.rotateY(cam, xAngle);
        cam = Point3DManipulationUtilities.rotateZ(cam, zAngle);
        
        
        
        dPos = 0;
        xAngle = 0;
        zAngle = 0;
        
        int a;
        
        if (zAngle != 0)
        	a = 0;
        
        glu.gluLookAt(cam.getX(), cam.getY(), cam.getZ(), 0, 0, 0, 0, 0, 1);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }
 
 
	public void dispose(GLAutoDrawable arg0) 
	{
		System.out.println("dispose() called");
	}
	
	public void setXAngle(double angle)
	{
		dXAngle += angle;
	}
	
	public double getXAngle()
	{
		return xAngle;
	}
	
	public void setZAngle(double angle)
	{
		dZAngle += angle;
	}
	
	public double getZAngle()
	{
		return zAngle;
	}
	
	public void setDist(double dPos)
	{
		this.dPos += dPos;
	}
	
	public double getDist()
	{
		return dPos;
	}
}