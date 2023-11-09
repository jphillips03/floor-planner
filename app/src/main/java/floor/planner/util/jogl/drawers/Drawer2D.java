package floor.planner.util.jogl.drawers;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

import floor.planner.models.Floor;
import floor.planner.models.FloorPlan;
import floor.planner.util.jogl.objects.obj2d.DrawableElement2D;
import floor.planner.util.math.Point2D;

import java.util.Arrays;
import java.util.List;

public class Drawer2D {
    
    public void draw(GL2 gl, FloorPlan floorPlan, int floorNum) {
        Floor floor = floorPlan.getFloor(floorNum);
        // draw each of the elements on the floor
        for (DrawableElement2D element : floor.getElements()) {
            element.draw(gl);
        }

        // draw the grid last to make sure it sits on top of elements, so cells
        // in floor matrix are easily distinguishable draw points around the
        // tile, same point used at start and end to ensure full square is drawn
        for (int i = 0; i < floorPlan.getHeight(); i++) {
            for (int j = 0; j < floorPlan.getWidth(); j++) {
                int r = floorPlan.getHeight() - i;
                this.drawEmptyTile(gl, Arrays.asList(
                    new Point2D(j, r),
                    new Point2D(j + 1, r),
                    new Point2D(j + 1, r - 1),
                    new Point2D(j, r - 1),
                    new Point2D(j, r)
                ));
            }
        }
    }

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
