package floor.planner.util.objects.obj2d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jogamp.opengl.GL2;

import floor.planner.constants.Orientation;
import floor.planner.util.math.Point2D;

public class Stairs extends DrawableElement2D {
    private static final List<Orientation> eastWest = Arrays.asList(Orientation.EAST_WEST, Orientation.WEST_EAST);
    private float colorDelta;
    private float colorStart;

    public Stairs (Point2D point, Orientation orientation) {
        super(point, orientation);
        if (this.orientation.equals(Orientation.EAST_WEST) || this.orientation.equals(Orientation.NORTH_SOUTH)) {
            this.colorDelta = -0.125f;
            this.colorStart = 1f;
        } else {
            this.colorDelta = 0.125f;
            this.colorStart = 0.125f;
        }
    }

    /**
     * NOTE: the offset used for X in east-west, and y in north-south must
     * match increment used in outer for loop of draw method below; otherwise
     * stairs are drawn beyond cell they are meant to be in
     */
    public void initPoints() {
        if (eastWest.contains(this.orientation)) {
            this.points = Arrays.asList(
                new Point2D(this.point.getX() + 0.125f, this.point.getY() - 0.75f),
                new Point2D(this.point.getX() + 0.125f, this.point.getY() - 0.25f),
                new Point2D(this.point.getX(), this.point.getY() - 0.25f),
                new Point2D(this.point.getX(), this.point.getY() - 0.75f)
            );
        } else {
            this.points = Arrays.asList(
                new Point2D(this.point.getX() + 0.75f, this.point.getY() - 0.125f),
                new Point2D(this.point.getX() + 0.25f, this.point.getY() - 0.125f),
                new Point2D(this.point.getX() + 0.25f, this.point.getY()),
                new Point2D(this.point.getX() + 0.75f, this.point.getY())
            );
        }
    }

    @Override
    public void draw(GL2 gl) {
        float color = this.colorStart;
        for (float i = 0; i <= 0.875; i+= 0.125) {
            List<Point2D> points = new ArrayList<Point2D>();
            for (int j = 0; j < this.points.size(); j++) {
                Point2D point = this.points.get(j);
                if (eastWest.contains(this.orientation)) {
                    // if we are going east-west, add to X
                    points.add(new Point2D(point.getX() + i, point.getY()));
                } else {
                    // otherwise we are going north-south, add to Y
                    points.add(new Point2D(point.getX(), point.getY() - i));
                }
            }
            this.drawUtil(gl, points, color);
            color += this.colorDelta;
        }
    }
}
