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
        // draw the floor plan in 3D
        for (Floor floor : floorPlan.getFloors()) {
            for (DrawableElement3D element : floor.getElements3D()) {
                element.draw(gl);
            }
        }
    }
}
