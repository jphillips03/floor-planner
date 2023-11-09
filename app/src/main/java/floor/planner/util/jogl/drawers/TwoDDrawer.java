package floor.planner.util.jogl.drawers;

import java.util.List;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

import floor.planner.util.math.Point2D;

/**
 * The TwoDDrawer class defines functions useful for rendering 2D elements.
 */
public class TwoDDrawer {

    public void drawEmptyTile(GL2 gl, List<Point2D> points) {
        gl.glColor3f(1.0f, 1.0f, 1.0f);
        gl.glBegin(GL.GL_LINE_STRIP);
        for (Point2D point : points) {
            gl.glVertex2d(point.getX(), point.getY());
        }
        gl.glEnd();
        gl.glFlush();
    }
}
