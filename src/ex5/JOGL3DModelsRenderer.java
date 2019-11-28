package ex5;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;

public class JOGL3DModelsRenderer implements GLEventListener {
    private GLU glu = new GLU();
    private float rtri =0.0f;

    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 500;

    public static void main(String[] args) {
        //getting the capabilities object of GL2 profile
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        // The canvas
        final GLCanvas glcanvas = new GLCanvas(capabilities);
        JOGL3DModelsRenderer renderer = new JOGL3DModelsRenderer();
        glcanvas.addGLEventListener(renderer);
        glcanvas.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        //creating frame
        final JFrame frame = new JFrame ("EX5. 3D models renderer.");
        //adding canvas to it
        frame.getContentPane().add(glcanvas);
        frame.setSize(frame.getContentPane().getPreferredSize());
        frame.setVisible(true);

        final FPSAnimator animator = new FPSAnimator( glcanvas, 500,true );
        animator.start();
    }

    @Override
    public void init(GLAutoDrawable drawable) {

    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    @Override
    public void display(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        // Clear The Screen And The Depth Buffer
        gl.glClear( GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT );
        gl.glLoadIdentity();                       // Reset The View
        gl.glTranslatef( -0.5f, 0.0f, -6.0f );         // Move the triangle
        gl.glRotatef( rtri, 0.0f, 1.0f, 0.0f );
        gl.glBegin(  GL2.GL_TRIANGLES );
        //drawing triangle in all dimensions
        // Front
        gl.glColor3f( 1.0f, 1.0f, 1.0f ); // Red
        gl.glVertex3f( 1.0f, 2.0f, 0.0f ); // Top Of Triangle (Front)
        gl.glVertex3f( -1.0f, -1.0f, 1.0f ); // Left Of Triangle (Front)
        gl.glVertex3f( 1.0f, -1.0f, 1.0f ); // Right Of Triangle (Front)

        // Right
        gl.glColor3f( 0.0f, 0.0f, 1.0f ); // Blue
        gl.glVertex3f( 1.0f, 2.0f, 0.0f ); // Top Of Triangle (Right)
        gl.glVertex3f( 1.0f, -1.0f, 1.0f ); // Left Of Triangle (Right)
        gl.glVertex3f( 1.0f, -1.0f, -1.0f ); // Right Of Triangle (Right)
        // Left
        gl.glColor3f( 0.0f, 1.0f, 0.0f ); // Green
        gl.glVertex3f( 1.0f, 2.0f, 0.0f ); // Top Of Triangle (Back)
        gl.glVertex3f( 1.0f, -1.0f, -1.0f ); // Left Of Triangle (Back)
        gl.glVertex3f( -1.0f, -1.0f, -1.0f ); // Right Of Triangle (Back)
        //left
        gl.glColor3f( 0.5f, 0.5f, 0.5f ); // Red
        gl.glVertex3f( 1.0f, 2.0f, 0.0f ); // Top Of Triangle (Left)
        gl.glVertex3f( -1.0f, -1.0f, -1.0f ); // Left Of Triangle (Left)
        gl.glVertex3f( -1.0f, -1.0f, 1.0f ); // Right Of Triangle (Left)

        gl.glEnd(); // Done Drawing 3d  triangle (Pyramid)
        gl.glFlush();
        rtri +=0.2f;
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        final GL2 gl = drawable.getGL().getGL2();
        if(height <= 0)
            height = 1;
        final float h = ( float ) width / ( float ) height;
        gl.glViewport( 0, 0, width, height );
        gl.glMatrixMode( GL2.GL_PROJECTION );
        gl.glLoadIdentity();
        glu.gluPerspective( 45.0f, h, 1.0, 20.0 );
        gl.glMatrixMode( GL2.GL_MODELVIEW );
        gl.glLoadIdentity();
    }
}