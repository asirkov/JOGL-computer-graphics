package ex1;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.*;
import javax.swing.*;

public class JOGLPointRenderer {

    private static final int WINDOW_WIDTH = 640;
    private static final int WINDOW_HEIGHT = 480;
    // Not used
    //private static final float scaling = 1.0f;

    public static void main(String[] args) {
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        final GLCapabilities capabilities = new GLCapabilities(profile);

        GLCanvas canvas = new GLCanvas(capabilities);
        canvas.addGLEventListener( new Renderer() );

        JFrame window = new JFrame("EX1. Point renderer.");
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
        // params for line 1
        //(-W/5, -H/2.5) (W/5, -H/2.5), 5, GRAY
        private final float startX1 = -1.0f / 5.0f,
                            startY1 = 1.0f / 2.5f,
                            endX1 = 1.0f / 5.0f,
                            endY1 = -1.0f / 2.5f;
        private final float lineWidth1 = 5.0f;
        private final float[] pointColor1 = {0.5f, 0.5f, 0.5f};

        //params for line 2
        //(-W/4, -H/4) (W/4, H/4), 3, RED
        private final float startX2 = -1.0f / 4.0f,
                            startY2 = -1.0f / 4.0f,
                            endX2 = 1.0f / 4.0f,
                            endY2 = 1.0f / 4.0f;
        private final float lineWidth2 = 3.0f;
        private final float[] pointColor2 = {1.0f, 0.0f, 0.0f};

        // 00011111 in 10-base number
        private final short lineMask = 31;

        @Override
        public void init(GLAutoDrawable drawable) {
            GL2 gl = drawable.getGL().getGL2();

            //window background color: WHITE
            gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

            gl.glMatrixMode( GL2.GL_PROJECTION );
            gl.glLoadIdentity();
        }

        @Override
        public void display(GLAutoDrawable drawable) {
            GL2 gl = drawable.getGL().getGL2();
            gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

            drawLine(gl, startX1, startY1, endX1, endY1, lineWidth1);
            drawLine(gl, startX2, startY2, endX2, endY2, lineWidth2);

            int pointsCount = 10;

            float deltaX1 = (endX1 - startX1) / pointsCount;
            float deltaY1 = (endY1 - startY1) / pointsCount;
            float deltaX2 = (endX2 - startX2) / pointsCount;
            float deltaY2 = (endY2 - startY2) / pointsCount;

            for(int i = 0; i < pointsCount; i++) {
                drawPoint(gl, startX1 + deltaX1 * i, startY1 + deltaY1 * i, 1.0f + i, pointColor1, false);
                drawPoint(gl, startX2 + deltaX2 * i, startY2 + deltaY2 * i, 1.0f + i, pointColor2, true);
            }
        }

        /**
         * @param gl GL2 library for drawing
         * @param x X coords of point
         * @param y Y coords of point
         * @param size Point size
         * @param color Array for point color
         * @param smooth Flag of enable point smooth
         */
        private void drawPoint(GL2 gl, float x, float y, float size, float[] color, boolean smooth) {
            if(smooth) gl.glEnable(GL2.GL_POINT_SMOOTH);

            gl.glPointSize(size);
            gl.glBegin(GL2.GL_POINTS);
                gl.glColor3fv(color, 0);
                gl.glVertex2f(x, y);
            gl.glEnd();

            if(smooth) gl.glDisable(GL2.GL_POINT_SMOOTH);
        }

        /**
         * @param gl GL2 library for drawing
         * @param startX X coords for line start
         * @param startY Y coords for line start
         * @param endX X coords for end of line
         * @param endY Y coords for end of line
         * @param width Width of line
         * //@param color May be to change line color
         */
        private void drawLine(GL2 gl, float startX, float startY, float endX, float endY, float width) { //, float[] color) {
            gl.glEnable(GL2.GL_LINE_SMOOTH);
            gl.glEnable(GL2.GL_LINE_STIPPLE);
            gl.glLineStipple(1, lineMask);  // 00011111

            gl.glLineWidth(width);

            gl.glBegin(GL2.GL_LINES);
                //gl.glColor3fv(color, 0);
                gl.glColor3f(0.5f, 0.5f, 0.9f);

                gl.glVertex2f(startX, startY);
                gl.glVertex2f(endX, endY);

            gl.glEnd();

            gl.glDisable(GL2.GL_LINE_SMOOTH);
            gl.glDisable(GL2.GL_LINE_STIPPLE);
        }

        // Not used
        @Override
        public void dispose(GLAutoDrawable drawable) { }

        // Mot used
        @Override
        public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) { }
    }

}

