package floor.planner.util.jogl.drawers;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

import floor.planner.models.Floor;
import floor.planner.models.FloorPlan;
import floor.planner.util.jogl.objects.obj3d.Cube;

public class Drawer3D {
    private static final Logger logger = LoggerFactory.getLogger(Drawer3D.class);

    public void draw(GL2 gl, GLU glu, FloorPlan floorPlan) {
        gl.glRotatef(floorPlan.getRotateX(), 1, 0, 0);
        gl.glRotatef(floorPlan.getRotateZ(), 0, 0, 1);
        gl.glTranslatef(0, 0, floorPlan.getUp());
        gl.glScalef(floorPlan.getZoom(), floorPlan.getZoom(), floorPlan.getZoom());

        // draw the floor plan in 3D
        this.drawFloorTiles3D(gl, floorPlan);
    }

    public void drawFloorTiles3D(GL2 gl, FloorPlan plan) {
        for (Floor floor : plan.getFloors()) {
            for (float[][] vertices : floor.getFloorTileVertices()) {
                this.drawCube(gl, vertices);
            }
        }
    }

    private void drawCube(GL2 gl, float[][] vertices) {
        int[][] verticesOrder = Cube.VERTICES_ORDER;
        for (int i = 0; i < verticesOrder.length; i++) {
            int[] order = verticesOrder[i];
            this.drawPolygon(
                gl,
                Arrays.asList(
                    vertices[order[0]],
                    vertices[order[1]],
                    vertices[order[2]],
                    vertices[order[3]]
                )
            );
        }
    }

    private void drawPolygon(GL2 gl, List<float[]> points) {
        gl.glColor3f(1.0f, 0.0f, 0.0f);
        gl.glBegin(GL2.GL_POLYGON);
        for (float[] point : points) {
            gl.glVertex3f(point[0], point[1], point[2]);
        }
        gl.glEnd();
    }
}
