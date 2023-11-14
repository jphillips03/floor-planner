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
        for (Floor floor : floorPlan.getFloors()) {
            DrawableElement3D[][][] elements = floor.getElements3D();
            for (int i = 0; i < floor.getHeight(); i++) {
                for (int j = 0; j < floor.getWidth(); j++) {
                    for (DrawableElement3D element : elements[i][j]) {
                        element.draw(gl);
                    }
                }
            }
        }
    }
}
