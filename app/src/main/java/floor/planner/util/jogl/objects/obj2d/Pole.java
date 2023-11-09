package floor.planner.util.jogl.objects.obj2d;

import com.jogamp.opengl.GL2;

import floor.planner.constants.Orientation;
import floor.planner.util.math.Point2D;

public class Pole extends DrawableElement2D {
    
    public Pole(Point2D point, Orientation orientation) {
        super(point, orientation);
    }

    public void initPoints() {}

    @Override
    public void draw(GL2 gl) {
        gl.glColor3f(1f, 0f, 0f);
        gl.glBegin(GL2.GL_POLYGON);

        double r = 0.15;
        for(int i = 0; i < 360; i++) {
			double radians = i*Math.PI/180;
			gl.glVertex2d(
                Math.cos(radians)*r + this.point.getX() + 0.5,
                Math.sin(radians)*r + this.point.getY() - 0.5
            );
		}
        gl.glEnd();
        gl.glFlush();
    }
}
