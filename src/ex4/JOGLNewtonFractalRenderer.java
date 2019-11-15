package ex4;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.*;
import utils.Complex;

import javax.swing.*;
import java.util.Arrays;

public class JOGLNewtonFractalRenderer {
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 500;

    public static void main(String[] args) {
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        final GLCapabilities capabilities = new GLCapabilities(profile);

        GLCanvas canvas = new GLCanvas(capabilities);
        canvas.addGLEventListener( new ex4.JOGLNewtonFractalRenderer.Renderer() );

        JFrame window = new JFrame("EX4. Newton fractal drawing.");
        window.getContentPane().add(canvas);

        window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.setResizable(false);

        canvas.requestFocusInWindow();
    }


    private static class Renderer implements GLEventListener {

        @Override
        public void init(GLAutoDrawable drawable) {
            GL2 gl = drawable.getGL().getGL2();

            //window background color: WHITE
            final float[] CURRENT_COLOR = utils.Color.WHITE;
            gl.glClearColor(CURRENT_COLOR[0], CURRENT_COLOR[1], CURRENT_COLOR[2], 1.0f);

            gl.glMatrixMode( GL2.GL_PROJECTION );
            gl.glLoadIdentity();

            // Now Ortho set to (1 pixel = 1 coordinate); 0, 0 = center;
            gl.glOrtho(-0,
                    (int)(WINDOW_WIDTH),
                    0,
                    (int)(WINDOW_HEIGHT),
                    1.0, -1.0);
        }

        @Override
        public void display(GLAutoDrawable drawable) {
            final GL2 gl = drawable.getGL().getGL2();
            gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

            drawFractal(WINDOW_WIDTH, WINDOW_HEIGHT, gl);
        }

        private static float[] toFloat(int r, int g, int b) {
            return new float[]{(r / 255.0f), (g / 255.0f), (b / 255.0f)};
        }

        private void drawFractal(int weith, int height, GL2 gl) {
            final int iter = 50;
            final double min = 1e-6;
            final double max = 1e+6;


            int n, mx, my;
            double p;
            Complex z = new Complex();
            Complex t = new Complex();
            Complex d = new Complex();

            mx = weith / 2;
            my = height / 2;

            gl.glPointSize(1.0f);
            gl.glBegin(GL2.GL_POINTS);
            for(int y = -1 * my; y < my; y++) {
                for(int x = -1 * mx; x < mx; x++) {
                    n = 0;

                    z.x = x * 0.005d;
                    z.y = y * 0.005d;

                    d = new Complex(z);

                    while(((Math.pow(z.x, 2) + Math.pow(z.y, 2)) < max) && ((Math.pow(d.x, 2) + Math.pow(d.y, 2)) > min) && (n < iter)) {
                        t = new Complex(z);
                        p = Math.pow(Math.pow(t.x, 2) + Math.pow(t.y, 2), 2);

                        z.x    = 2.0d / 3.0d * t.x + ( Math.pow(t.x, 2) - Math.pow(t.y, 2) ) / (3.0d * p);
                        z.y    = 2.0d / 3.0d * t.y * (1.0d - t.x / p);
                        d.x    = Math.abs(t.x - z.x);
                        d.y    = Math.abs(t.y - z.y);

                        ++n;
                    }

                    int col1 = (n * 9) % 255;
                    int col2 = (n * 9) % 255;

                    gl.glColor3fv(toFloat(col1, 0, col2), 0);
                    gl.glVertex2i(x + mx, y + my);
                }
            }
            gl.glEnd();
        }

        // Not used
        @Override
        public void dispose(GLAutoDrawable drawable) { }

        // Mot used
        @Override
        public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) { }
    }

}