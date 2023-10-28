package floor.planner.abstractClasses;

import java.util.List;

import com.jogamp.opengl.GL2;

import floor.planner.constants.Orientation;
import floor.planner.models.Point2D;

/**
 * 
 */
public abstract class DrawableElement {
    protected Orientation orientation;
    protected Point2D point;
    protected List<Point2D> points;

    public DrawableElement(Point2D point, Orientation orientation) {
        this.point = point;
        this.orientation = orientation;
        this.initPoints();
    }

    /**
     * Draws the points for the element as a GL_POLYGON.
     *
     * @param gl JOGL object used to do the drawing.
     */
    public void draw(GL2 gl) {
        this.drawUtil(gl, this.points);
    }

    /**
     * Utility method to handle drawing a GL_POLYGON with the given set of
     * points. Some drawable elements consist of multiple drawable elements or
     * polygons. This is included as a convenience for those elements that need
     * to override the main draw method.
     *
     * @param gl
     * @param points
     */
    protected void drawUtil(GL2 gl, List<Point2D> points) {
        this.drawUtil(gl, points, 1);
    }

    protected void drawUtil(GL2 gl, List<Point2D> points, float color) {
        gl.glColor3f(color, 0f, 0f);
        gl.glBegin(GL2.GL_POLYGON);
        for (Point2D p : points) {
            gl.glVertex2d(p.getX(), p.getY());
        }
        gl.glEnd();
        gl.glFlush();
    }

    /**
     * Initializes the points for the element representing a polygon.
     */
    public abstract void initPoints();
}
