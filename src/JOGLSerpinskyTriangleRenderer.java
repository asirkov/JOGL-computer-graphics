import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;

import javax.swing.*;

public class JOGLSerpinskyTriangleRenderer {

    private static final int WINDOW_WIDTH = 1280;
    private static final int WINDOW_HEIGHT = 720;
    // Not used
    //private static final float scaling = 1.0f;

    public static void main(String[] args) {
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        final GLCapabilities capabilities = new GLCapabilities(profile);

        GLCanvas canvas = new GLCanvas(capabilities);
        canvas.addGLEventListener( new Renderer() );

        JFrame window = new JFrame("EX2. Serpinsky triangle.");
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
            gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

            gl.glMatrixMode( GL2.GL_PROJECTION );
            gl.glLoadIdentity();
            gl.glOrtho((int)(-1 * WINDOW_WIDTH / 2), (int)(WINDOW_WIDTH / 2), (int)(-1 * WINDOW_HEIGHT / 2), (int)(WINDOW_HEIGHT / 2), 1.0, -1.0);
            //gl.glOrtho(0.0, 0.0, 0.0, 0.0, 1.0, -1.0);
        }

        @Override
        public void display(GLAutoDrawable drawable) {
            GL2 gl = drawable.getGL().getGL2();
            gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

            final int iterations = 4;

            // для рассчета координат от центра
            int W = WINDOW_WIDTH;
            int H = WINDOW_HEIGHT;

            point2i[] points = {
                    new point2i((-1 * W) / 4, (int)((-1 * H) / 2.5) ),
                    new point2i(W / 4, (int)(H / 2.5) ),
                    new point2i((-1 * W) / 3, 0 )
            };

            drawTriangle(gl, points[0], points[1], points[2], Color.BLACK, iterations);
        }

        /*
         *
         *
         */

        private static class point2i {
            int x;
            int y;
            point2i(int x, int y) {this.x = x; this.y = y;}
        }

        private static class Color {
            static final float[] WHITE = {1.0f, 1.0f, 1.0f};
            static final float[] BLACK = {0.0f, 0.0f, 0.0f};
        }

        private static point2i avg(point2i A, point2i B) {
            return new point2i((A.x + B.x) / 2, (A.y + B.y) / 2);
        }

        private void drawTriangle(GL2 gl, point2i A, point2i B, point2i C, float[] color, int iteration) {
            if(iteration == 0)
                return;

            gl.glEnable(GL2.GL_LINE_SMOOTH);

            gl.glBegin(GL2.GL_TRIANGLES);
            gl.glColor3fv(color, 0);
            gl.glVertex2i(A.x, A.y);
            gl.glVertex2i(B.x, B.y);
            gl.glVertex2i(C.x, C.y);
            gl.glEnd();

            if(iteration > 1) {
                float[] newColor = (color == Color.BLACK ? Color.WHITE : Color.BLACK);

                point2i avgA = avg(B, C);
                point2i avgB = avg(C, A);
                point2i avgC = avg(A, B);

                gl.glBegin(GL2.GL_TRIANGLES);
                gl.glColor3fv(newColor, 0);
                gl.glVertex2i(avgA.x, avgA.y);
                gl.glVertex2i(avgB.x, avgB.y);
                gl.glVertex2i(avgC.x, avgC.y);
                gl.glEnd();

                drawTriangle(gl, A, avgB, avgC, color, iteration - 1);
                drawTriangle(gl, avgA, B, avgC, color, iteration - 1);
                drawTriangle(gl, avgA, avgB, C, color, iteration - 1);
            }

            gl.glDisable(GL2.GL_LINE_SMOOTH);
        }

        // Not used
        @Override
        public void dispose(GLAutoDrawable drawable) { }

        // Mot used
        @Override
        public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) { }
    }

}

