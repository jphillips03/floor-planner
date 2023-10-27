package floor.planner.models.twodelements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jogamp.opengl.GL2;

import floor.planner.constants.EastWestWallOffset2D;
import floor.planner.constants.NorthSouthWallOffset2D;
import floor.planner.constants.Orientation;
import floor.planner.interfaces.DrawableElement;
import floor.planner.models.Point2D;

public class Wall implements DrawableElement {
    private Orientation orientation;
    private Point2D point;
    private List<Point2D> points;

    public Wall(Point2D point, Orientation orientation) {
        this.point = point;
        this.orientation = orientation;
        this.initPoints();
    }

    private void initPoints() {
        if (this.orientation == Orientation.EAST_WEST) {
            this.points = Arrays.asList(
                new Point2D(
                    this.point.getX() + 1,
                    this.point.getY() + EastWestWallOffset2D.TOP.value
                ),
                new Point2D(
                    this.point.getX() + 1,
                    this.point.getY() + EastWestWallOffset2D.BOTTOM.value
                ),
                new Point2D(
                    this.point.getX(),
                    this.point.getY() + EastWestWallOffset2D.BOTTOM.value
                ),
                new Point2D(
                    this.point.getX(),
                    this.point.getY() + EastWestWallOffset2D.TOP.value
                )
            );
        } else if (this.orientation == Orientation.NORTH_SOUTH) {
            this.points = Arrays.asList(
                new Point2D(
                    this.point.getX() + NorthSouthWallOffset2D.LEFT.value,
                    this.point.getY() + 1
                ),
                new Point2D(
                    this.point.getX() + NorthSouthWallOffset2D.RIGHT.value,
                    this.point.getY() + 1
                ),
                new Point2D(
                    this.point.getX() + NorthSouthWallOffset2D.RIGHT.value,
                    this.point.getY()
                ),
                new Point2D(
                    this.point.getX() + NorthSouthWallOffset2D.LEFT.value,
                    this.point.getY()
                )
            );
        } else if (this.orientation == Orientation.COLUMN) {
            // basically the intersection point of corner walls, so it's a
            // small but mighty column...
            this.points = Arrays.asList(
                new Point2D(
                    this.point.getX() + NorthSouthWallOffset2D.LEFT.value,
                    this.point.getY() + EastWestWallOffset2D.TOP.value
                ),
                new Point2D(
                    this.point.getX() + NorthSouthWallOffset2D.RIGHT.value,
                    this.point.getY() + EastWestWallOffset2D.TOP.value
                ),
                new Point2D(
                    this.point.getX() + NorthSouthWallOffset2D.RIGHT.value,
                    this.point.getY() + EastWestWallOffset2D.BOTTOM.value
                ),
                new Point2D(
                    this.point.getX() + NorthSouthWallOffset2D.LEFT.value,
                    this.point.getY() + EastWestWallOffset2D.BOTTOM.value
                )
            );
        }
    }

    public void draw(GL2 gl) {
        gl.glColor3f(0f, 0f, 1f);
        gl.glBegin(GL2.GL_POLYGON);
        for (Point2D p : this.points) {
            gl.glVertex2d(p.getX(), p.getY());
        }
        gl.glEnd();
        gl.glFlush();
    }
}
