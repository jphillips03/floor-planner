package floor.planner.util.objects.obj2d;

import com.jogamp.opengl.GL2;

import floor.planner.constants.Orientation;
import floor.planner.util.math.MathUtil;
import floor.planner.util.math.Point2D;

public class Pole extends DrawableElement2D {
    private double radius = 0.15;

    public Pole(Point2D point, Orientation orientation) {
        super(point, orientation);
    }

    public Pole(Point2D point, Orientation orientation, double radius) {
        super(point, orientation);
        this.radius = radius;
    }

    public void initPoints() {}

    @Override
    public void draw(GL2 gl) {
        gl.glColor3f(1f, 0f, 0f);
        gl.glBegin(GL2.GL_POLYGON);

        for(int i = 0; i < 360; i++) {
			double radians = i*MathUtil.PI/180;
			gl.glVertex2d(
                Math.cos(radians) * this.radius + this.point.getX() + 0.5,
                Math.sin(radians) * this.radius + this.point.getY() - 0.5
            );
		}
        gl.glEnd();
    }
}
