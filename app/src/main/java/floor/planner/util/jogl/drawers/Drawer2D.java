package floor.planner.util.jogl.drawers;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import floor.planner.models.Floor;
import floor.planner.models.FloorPlan;
import floor.planner.util.math.Point2D;
import floor.planner.util.objects.obj2d.DrawableElement2D;

import java.util.Arrays;
import java.util.List;

public class Drawer2D {
    
    private int selectedCol = -1;
    private int selectedRow = -1;

    public void setSelectedCol(int c) {
        this.selectedCol = c;
    }

    public void setSelectedRow(int r) {
        this.selectedRow = r;
    }

    public void draw(GL2 gl, FloorPlan floorPlan, int floorNum) {
        Floor floor = floorPlan.getFloor(floorNum);
        // draw each of the elements on the floor
        DrawableElement2D[][][] elements2D = floor.getElements();
        for (int i = 0; i < floor.getHeight(); i++) {
            for (int j = 0; j < floor.getWidth(); j++) {
                for (DrawableElement2D element : elements2D[i][j]) {
                    element.draw(gl);
                }
            }
        }

        // draw the grid last to make sure it sits on top of elements, so cells
        // in floor matrix are easily distinguishable draw points around the
        // tile, same point used at start and end to ensure full square is drawn
        for (int i = 0; i < floorPlan.getHeight(); i++) {
            for (int j = 0; j < floorPlan.getWidth(); j++) {
                int r = floorPlan.getHeight() - i;
                this.drawEmptyTile(gl, this.initPoints(r, j));
            }
        }

        // draw highlighted tile (if user selected an element in floor) last so
        // it appears over everything else
        if (this.selectedRow >= 0 && this.selectedCol >= 0) {
            int row = floorPlan.getHeight() - this.selectedRow;
            this.drawHighlightedTile(gl, this.initPoints(row, this.selectedCol));
        }
    }

    private List<Point2D> initPoints(int row, int col) {
        return Arrays.asList(
            new Point2D(col, row),
            new Point2D(col + 1, row),
            new Point2D(col + 1, row - 1),
            new Point2D(col, row - 1),
            new Point2D(col, row)
        );
    }

    public void drawEmptyTile(GL2 gl, List<Point2D> points) {
        gl.glColor3f(1.0f, 1.0f, 1.0f);
        gl.glBegin(GL.GL_LINE_STRIP);
        for (Point2D point : points) {
            gl.glVertex2d(point.getX(), point.getY());
        }
        gl.glEnd();
    }

    // TODO see if this can be somewhat transparent (NOTE: glColor4f with low alpha doesn't work...)
    public void drawHighlightedTile(GL2 gl, List<Point2D> points) {
        gl.glColor3f(0f, 0.5f, 0.75f);
        gl.glBegin(GL.GL_LINE_STRIP);
        for (Point2D point : points) {
            gl.glVertex2d(point.getX(), point.getY());
        }
        gl.glEnd();
    }
}
