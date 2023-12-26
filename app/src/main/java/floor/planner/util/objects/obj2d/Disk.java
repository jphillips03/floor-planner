package floor.planner.util.objects.obj2d;

import com.jogamp.opengl.GL2;

import floor.planner.constants.Orientation;
import floor.planner.util.math.MathUtil;
import floor.planner.util.math.Point2D;

public class Disk extends DrawableElement2D {
    private double radius = 0.15;

    public Disk(Point2D point, Orientation orientation) {
        super(point, orientation);
    }

    public Disk(Point2D point, Orientation orientation, double radius) {
        super(point, orientation);
        this.radius = radius;
    }

    public void initPoints() {}

    @Override
    public void draw(GL2 gl) {
        gl.glColor3f(this.color.getRed(), this.color.getGreen(), this.color.getBlue());
        this.drawUtil(gl, this.radius);
    }

    public void drawUtil(GL2 gl, double radius) {
        draw(gl, this.point.getX(), this.point.getY(), radius);
    }

    public static void draw(GL2 gl, double x, double y, double radius) {
        gl.glBegin(GL2.GL_POLYGON);

        for(int i = 0; i < 360; i++) {
            double radians = i*MathUtil.PI/180;
            gl.glVertex2d(
                Math.cos(radians) * radius + x + 0.5,
                Math.sin(radians) * radius + y - 0.5
            );
        }
        gl.glEnd();
    }
}
