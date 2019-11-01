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

    // Epsilon for draw iterations
    private static final int EPS = 100;


    // Default colors
    private static class Color {
        static final float[] WHITE = {1.0f, 1.0f, 1.0f};
        static final float[] BLACK = {0.0f, 0.0f, 0.0f};
    }

    // Interface for drawable function
    // x * 2
    private static interface DrawableFunction extends Function<Integer, Integer> {
        @Override
        public Integer apply(Integer x);
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
            gl.glOrtho((int)(-1 * WINDOW_WIDTH / 2), (int)(WINDOW_WIDTH / 2), (int)(-1 * WINDOW_HEIGHT / 2), (int)(WINDOW_HEIGHT / 2), 1.0, -1.0);
        }

        @Override
        public void display(GLAutoDrawable drawable) {
            final GL2 gl = drawable.getGL().getGL2();
            gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

            drawAxis(gl, drawable);

            //final DrawableFunction myFunc = x -> x * 2;
            //drawFunction(gl, myFunc);
        }

        /*
         *
         *
         */
        private static void drawAxis(GL2 gl, GLAutoDrawable drawable) {
            final float[] CURRENT_COLOR = Color.BLACK;
            final int centerX = drawable.getSurfaceWidth() / 2;
            final int centerY = drawable.getSurfaceHeight() / 2;
            final int TEXT_SIZE = 11;

            gl.glLineWidth(2f);

            // draw Y
            gl.glBegin(GL2.GL_LINES);
                gl.glColor3fv(CURRENT_COLOR, 0);
                gl.glVertex2i(0, centerY);
                gl.glVertex2i(0, -1 * centerY);
            gl.glEnd();

            // draw X
            gl.glBegin(GL2.GL_LINES);
                gl.glColor3fv(CURRENT_COLOR, 0);
                gl.glVertex2i(centerX, 0);
                gl.glVertex2i(-1 * centerX, 0);
            gl.glEnd();

            TextRenderer tr = new TextRenderer(new Font("Sans serif", Font.BOLD, TEXT_SIZE));

            tr.beginRendering(drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
                tr.setColor(CURRENT_COLOR[0], CURRENT_COLOR[1], CURRENT_COLOR[2], 1f);
                tr.draw("X", drawable.getSurfaceWidth() - TEXT_SIZE, centerY - TEXT_SIZE);
                tr.draw("Y", centerX - TEXT_SIZE, drawable.getSurfaceHeight() - TEXT_SIZE);
            tr.endRendering();

            final int deltaX = centerX / SCALING;
            final int deltaY = centerY / SCALING;

            final int offset = TEXT_SIZE * 2;
            // add second offset

            tr.beginRendering(drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
                tr.setColor(CURRENT_COLOR[0], CURRENT_COLOR[1], CURRENT_COLOR[2], 1f);

                tr.draw("0", centerX - offset, centerY - offset);
                for (int i = 1; i < SCALING; i++) {

                    // Y
                    tr.draw(String.valueOf(i), centerX - offset, centerY + (i * deltaY) - offset);
                    // -Y
                    tr.draw(String.valueOf(-1 * i), centerX - offset, centerY + (-1 * i * deltaY) - offset);

                    // X
                    tr.draw(String.valueOf(i), centerX + (i * deltaX) - offset, centerY - offset);
                    // -X
                    tr.draw(String.valueOf(-1 * i), centerX + (-1 * i * deltaX) - offset , centerY - offset);
                }
            tr.endRendering();

            gl.glPointSize(4f);
            gl.glBegin(GL2.GL_POINTS);
            gl.glColor3fv(CURRENT_COLOR, 0);
                for (int i = 0; i < SCALING; i++) {
                    gl.glVertex2i(0, (-1 * i * deltaY));
                    gl.glVertex2i(0, i * deltaY);
                    gl.glVertex2i(i * deltaX, 0);
                    gl.glVertex2i((-1 * i * deltaX), 0);
                }
            gl.glEnd();
        }

        private void drawFunction(GL2 gl, DrawableFunction f) {
            gl.glEnable(GL2.GL_LINE_SMOOTH);

            final int A = 0;
            final int B = 100;
            final float delta = (float)(B - A) / EPS;

            gl.glBegin(GL2.GL_LINES);
                gl.glColor3f(1f, 0f,0f);

                for(int i = 0; i < EPS; i++) {
                    gl.glVertex2i((int)(i * delta * SCALING), f.apply(i) * SCALING);
                    gl.glVertex2i((int)(i * delta * SCALING) + (int)delta, f.apply(i) * SCALING + (int)delta);
                }
            gl.glEnd();

            gl.glDisable(GL2.GL_LINE_SMOOTH);
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