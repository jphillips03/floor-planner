package floor.planner.util.objects.obj2d;

import java.util.Arrays;
import java.util.List;

import com.jogamp.opengl.GL2;

import floor.planner.constants.Orientation;
import floor.planner.util.math.Point2D;

/**
 * A window consists of 2 triangles facing away from each other and a wall
 * where the tips meet. The wall helps visually orient the window to be
 * EAST_WEST or NORTH_SOUTH.
 */
public class Window extends DrawableElement2D {
    /** The set of points for one of the triangles. */
    private List<Point2D> triangle1Points;
    /** The set of points for the other triangle. */
    private List<Point2D> triangle2Points;
    /** The wall that helps orient the window. */
    private Wall wall;

    public Window(Point2D point, Orientation orientation, Wall wall) {
        super(point, orientation);
        this.wall = wall;
    }

    public void initPoints() {
        if (this.orientation == Orientation.EAST_WEST) {
            this.triangle1Points = Arrays.asList(
                new Point2D(this.point.getX(), this.point.getY() - 1),
                new Point2D(this.point.getX() + 1, this.point.getY() - 1),
                new Point2D(this.point.getX() + 0.5f, this.point.getY() - 0.5f)
            );
            this.triangle2Points = Arrays.asList(
                new Point2D(this.point.getX(), this.point.getY()),
                new Point2D(this.point.getX() + 1, this.point.getY()),
                new Point2D(this.point.getX() + 0.5f, this.point.getY() - 0.5f)
            );
        } else if (this.orientation == Orientation.NORTH_SOUTH) {
            this.triangle1Points = Arrays.asList(
                new Point2D(this.point.getX(), this.point.getY() - 1),
                new Point2D(this.point.getX(), this.point.getY()),
                new Point2D(this.point.getX() + 0.5f, this.point.getY() - 0.5f)
            );
            this.triangle2Points = Arrays.asList(
                new Point2D(this.point.getX() + 1, this.point.getY() - 1),
                new Point2D(this.point.getX() + 1, this.point.getY()),
                new Point2D(this.point.getX() + 0.5f, this.point.getY() - 0.5f)
            );
        }
    }

    @Override
    public void draw(GL2 gl) {
        this.drawUtil(gl, this.triangle1Points);
        this.drawUtil(gl, this.triangle2Points);
        this.wall.draw(gl);
    }
}
