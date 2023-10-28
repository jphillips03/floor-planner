package floor.planner.models.twodelements;

import com.jogamp.opengl.GL2;

import floor.planner.abstractClasses.DrawableElement;
import floor.planner.constants.Orientation;
import floor.planner.models.Point2D;

public class Pole extends DrawableElement {
    
    public Pole(Point2D point, Orientation orientation) {
        super(point, orientation);
    }

    public void initPoints() {}

    @Override
    public void draw(GL2 gl) {
        gl.glColor3f(1f, 0f, 0f);
        gl.glBegin(GL2.GL_POLYGON);

        double r = 0.25;
        for(int i = 0; i < 360; i++) {
			double radians = i*Math.PI/180;
			gl.glVertex2d(Math.cos(radians)*r + this.point.getX() + 0.5, Math.sin(radians)*r + this.point.getY() + 0.5);
		}
        gl.glEnd();
        gl.glFlush();
    }
}
