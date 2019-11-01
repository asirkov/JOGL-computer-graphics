package ex3;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.*;

import javax.swing.*;
import java.util.function.Function;

public class JOGLFuncRenderer {
    private static final int WINDOW_WIDTH = 1280;
    private static final int WINDOW_HEIGHT = 720;

    private static final int SCALING = 1;

    // Default colors
    private static class Color {
        static final float[] WHITE = {1.0f, 1.0f, 1.0f};
        static final float[] BLACK = {0.0f, 0.0f, 0.0f};
    }

    // function for drawing
    //private static final Function<Integer, Integer> function;

    public static void main(String[] args) {
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        final GLCapabilities capabilities = new GLCapabilities(profile);

        GLCanvas canvas = new GLCanvas(capabilities);
        canvas.addGLEventListener( new Renderer() );

        JFrame window = new JFrame("EX3. Function drawing.");
        window.getContentPane().add(canvas);

        window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.setResizable(false);

        canvas.requestFocusInWindow();
    }

    /*
     * Class for rendering and drawing points and lines at frame
     *
     */
    private static class Renderer implements GLEventListener {

        @Override
        public void init(GLAutoDrawable drawable) {
            GL2 gl = drawable.getGL().getGL2();

            //window background color: WHITE
            final float[] CURRENT_COLOR = Color.WHITE;
            gl.glClearColor(CURRENT_COLOR[0], CURRENT_COLOR[1], CURRENT_COLOR[2], 1.0f);

            gl.glMatrixMode( GL2.GL_PROJECTION );
            gl.glLoadIdentity();

            //TODO edit ortho for scaling

            // now ortho set for 1 coord = 1 pixel; 0, 0 = center;
            gl.glOrtho((int)(-1 * WINDOW_WIDTH / 2), (int)(WINDOW_WIDTH / 2), (int)(-1 * WINDOW_HEIGHT / 2), (int)(WINDOW_HEIGHT / 2), 1.0, -1.0);
        }

        @Override
        public void display(GLAutoDrawable drawable) {
            final GL2 gl = drawable.getGL().getGL2();
            gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

            drawAxis(gl);

        }

        /*
         *
         *
         */

        private static void drawAxis(GL2 gl) {
            final float[] CURRENT_COLOR = Color.BLACK;

            // draw Y
            gl.glBegin(GL2.GL_LINES);
                gl.glColor3fv(CURRENT_COLOR, 0);
                gl.glVertex2i(0, WINDOW_HEIGHT / 2);
                gl.glVertex2i(0, -1 * WINDOW_HEIGHT / 2);
            gl.glEnd();

            // draw X
            gl.glBegin(GL2.GL_LINES);
                gl.glColor3fv(CURRENT_COLOR, 0);
                gl.glVertex2i(WINDOW_WIDTH / 2, 0);
                gl.glVertex2i(-1 * WINDOW_WIDTH / 2, 0);
            gl.glEnd();

        }

        // not used
        private static class point2i {
            int x;
            int y;
            point2i(int x, int y) {this.x = x; this.y = y;}
        }



        // not used
        private static point2i avg(point2i A, point2i B) {
            return new point2i((A.x + B.x) / 2, (A.y + B.y) / 2);
        }



        // Not used
        @Override
        public void dispose(GLAutoDrawable drawable) { }

        // Mot used
        @Override
        public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) { }
    }

}