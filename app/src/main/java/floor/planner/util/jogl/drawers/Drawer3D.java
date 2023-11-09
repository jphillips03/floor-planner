package floor.planner.util.jogl.drawers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

import floor.planner.models.Floor;
import floor.planner.models.FloorPlan;
import floor.planner.util.jogl.objects.obj3d.DrawableElement3D;

public class Drawer3D {
    private static final Logger logger = LoggerFactory.getLogger(Drawer3D.class);

    public void draw(GL2 gl, GLU glu, FloorPlan floorPlan) {
        gl.glRotatef(floorPlan.getRotateX(), 1, 0, 0);
        gl.glRotatef(floorPlan.getRotateZ(), 0, 0, 1);
        gl.glTranslatef(0, 0, floorPlan.getUp());
        gl.glScalef(floorPlan.getZoom(), floorPlan.getZoom(), floorPlan.getZoom());

        // draw the floor plan in 3D
        for (Floor floor : floorPlan.getFloors()) {
            for (DrawableElement3D element : floor.getElements3D()) {
                element.draw(gl);
            }
        }
    }
}
