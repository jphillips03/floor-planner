package floor.planner.services;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.opengl.GL2;

import floor.planner.models.DrawableElement;
import floor.planner.models.Floor;
import floor.planner.models.FloorPlan;
import floor.planner.models.Point2D;
import floor.planner.util.jogl.drawers.TwoDDrawer;

public class FloorPlan2DDrawerService {
    private static final Logger logger = LoggerFactory.getLogger(FloorPlan2DDrawerService.class);
    private TwoDDrawer drawer = new TwoDDrawer();

    public void drawFloor(GL2 gl, FloorPlan floorPlan, int floorNum) {
        Floor floor = floorPlan.getFloor(floorNum);
        // draw each of the elements on the floor
        for (DrawableElement element : floor.getElements()) {
            element.draw(gl);
        }

        // draw the grid last to make sure it sits on top of elements, so cells
        // in floor matrix are easily distinguishable draw points around the
        // tile, same point used at start and end to ensure full square is drawn
        for (int i = 0; i < floorPlan.getHeight(); i++) {
            for (int j = 0; j < floorPlan.getWidth(); j++) {
                int r = floorPlan.getHeight() - i;
                this.drawer.drawEmptyTile(gl, Arrays.asList(
                    new Point2D(j, r),
                    new Point2D(j + 1, r),
                    new Point2D(j + 1, r - 1),
                    new Point2D(j, r - 1),
                    new Point2D(j, r)
                ));
            }
        }
    }
}
