package floor.planner.util.objects.obj2d;

import com.jogamp.opengl.GL2;

import floor.planner.constants.Orientation;
import floor.planner.util.math.Point2D;

public class DiskStack extends DrawableElement2D {
    private float colorDelta;
    private float colorStart;
    private double radius;

    public DiskStack(Point2D point, Orientation orientation, double radius) {
        super(point, orientation);
        this.radius = radius;
        this.colorDelta = 0.125f;
        this.colorStart = 0.125f; 
    }

    public void initPoints() {}

    @Override
    public void draw(GL2 gl) {
        float color = this.colorStart;
        double radius = this.radius;
        for (int i = 0; i < 9; i++) {
            gl.glColor3f(color, 0f, 0f);
            Disk.draw(gl, this.point.getX(), this.point.getY(), radius);
            color += this.colorDelta;
            radius -= 0.025;
        }
    }
}
