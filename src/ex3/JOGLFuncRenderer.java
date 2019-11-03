package ex3;

import com.jogamp.graph.curve.opengl.TextRegionUtil;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.*;
import com.jogamp.opengl.util.awt.TextRenderer;

import javax.swing.*;
import java.awt.*;
import java.util.function.Function;

public class JOGLFuncRenderer {
    private static final int WINDOW_WIDTH = 1280;
    private static final int WINDOW_HEIGHT = 720;

    // Scaling value, (1 for 1 pixel = 1 coordinate)
    private static final int SCALING = 10;

    // Epsilon for draw iterations in pixels
    private static final int EPS = 20;


    // Default colors
    private static class Color {
        static final float[] WHITE = {1.0f, 1.0f, 1.0f};
        static final float[] BLACK = {0.0f, 0.0f, 0.0f};
    }

    //Interface for drawable function
    private static interface DrawableFunction extends Function<Float, Float> {
        @Override
        public Float apply(Float x);
    }

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


    private static class Renderer implements GLEventListener {

        @Override
        public void init(GLAutoDrawable drawable) {
            GL2 gl = drawable.getGL().getGL2();

            //window background color: WHITE
            final float[] CURRENT_COLOR = Color.WHITE;
            gl.glClearColor(CURRENT_COLOR[0], CURRENT_COLOR[1], CURRENT_COLOR[2], 1.0f);

            gl.glMatrixMode( GL2.GL_PROJECTION );
            gl.glLoadIdentity();

            // Now Ortho set to (1 pixel = 1 coordinate); 0, 0 = center;
            gl.glOrtho((-1 * SCALING),
                    (SCALING),
                    (-1 * SCALING),
                    (SCALING),
                    1.0, -1.0);
        }

        @Override
        public void display(GLAutoDrawable drawable) {
            final GL2 gl = drawable.getGL().getGL2();
            gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

            drawAxis(gl, drawable);

            final DrawableFunction myFunc = x -> { return (float) Math.sin(x) + (5f * (float) Math.cos(x) ); };
            drawFunction(gl, myFunc);
        }

        private static void drawAxis(GL2 gl, GLAutoDrawable drawable) {
            final float[] CURRENT_COLOR = Color.BLACK;
            final int TEXT_SIZE = 11;

            final int centerX = drawable.getSurfaceWidth() / 2;
            final int centerY = drawable.getSurfaceHeight() / 2;

            gl.glLineWidth(2f);

            // draw Y
            gl.glBegin(GL2.GL_LINES);
                //gl.glColor3fv(CURRENT_COLOR, 0);
                gl.glColor3f(0f, 0f, 1f);
                gl.glVertex2i(0, centerY);
                gl.glVertex2i(0, -1 * centerY);
            gl.glEnd();

            // draw X
            gl.glBegin(GL2.GL_LINES);
                //gl.glColor3fv(CURRENT_COLOR, 0);
                gl.glColor3f(0f, 0f, 1f);
                gl.glVertex2i(centerX, 0);
                gl.glVertex2i(-1 * centerX, 0);
            gl.glEnd();


            TextRenderer tr = new TextRenderer(new Font("Sans serif", Font.BOLD, TEXT_SIZE));

            int deltaX = drawable.getSurfaceWidth() / 2 / SCALING;
            int deltaY = drawable.getSurfaceHeight() / 2 / SCALING;

            tr.beginRendering(drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
                tr.setColor(CURRENT_COLOR[0], CURRENT_COLOR[1], CURRENT_COLOR[2], 1f);

                tr.setColor(CURRENT_COLOR[0], CURRENT_COLOR[1], CURRENT_COLOR[2], 1f);
                tr.draw("X", drawable.getSurfaceWidth() - TEXT_SIZE, centerY - TEXT_SIZE);
                tr.draw("Y", centerX - TEXT_SIZE, drawable.getSurfaceHeight() - TEXT_SIZE);

                tr.draw("0", centerX - TEXT_SIZE, centerY - TEXT_SIZE);

                for (int i = 1; i < SCALING; i++) {
                    // Y \ -Y
                    tr.draw(String.valueOf(i)       , centerX - TEXT_SIZE, centerY + (i * deltaY));
                    tr.draw(String.valueOf(-1 * i)  , centerX - TEXT_SIZE, centerY + (-1 * i * deltaY));

                    // X \ -X
                    tr.draw(String.valueOf(i)       , centerX + (i * deltaX), centerY - TEXT_SIZE);
                    tr.draw(String.valueOf(-1 * i)  , centerX + (-1 * i * deltaX), centerY - TEXT_SIZE);
                }
            tr.endRendering();



            gl.glPointSize(4f);
            gl.glEnable(GL2.GL_POINT_SMOOTH);

            gl.glBegin(GL2.GL_POINTS);
            gl.glColor3fv(CURRENT_COLOR, 0);
                for (int i = 0; i < SCALING; i++) {
                    gl.glVertex2i(0, (-1 * i));
                    gl.glVertex2i(0, i);
                    gl.glVertex2i(i, 0);
                    gl.glVertex2i((-1 * i), 0);
                }
            gl.glEnd();
            gl.glDisable(GL2.GL_POINT_SMOOTH);
        }

        private void drawFunction(GL2 gl, DrawableFunction f) {
            final int A = 0;
            final int B = 8;

            //gl.glLineWidth(1f);
            float deltaX = Math.abs(B - A) / 40f;

            gl.glBegin(GL2.GL_LINE_STRIP);
                gl.glColor3f(1f, 0f, 0f);

                for(float i = A; i <= B; i+= deltaX) {
                    gl.glVertex2f(i, f.apply(i) );
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