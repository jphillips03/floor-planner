package floor.planner.util.jogl.gleventlisteners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.newt.opengl.GLWindow;

import floor.planner.models.CurrentFloorPlan;
import floor.planner.util.objects.obj2d.ClippingPlane;

public class MouseListener2D implements MouseListener {
    private static final Logger logger = LoggerFactory.getLogger(MouseListener2D.class);

    private CurrentFloorPlan currentFloorPlan;
    private GLWindow glWindow;
    private GLEventListener2D eventListener2D;

    public MouseListener2D(CurrentFloorPlan cfp, GLWindow glWindow, GLEventListener2D listener) {
        this.currentFloorPlan = cfp;
        this.glWindow = glWindow;
        this.eventListener2D = listener;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        logger.info("Window Click: " + mouseX + " " + mouseY);

        ClippingPlane plane = this.currentFloorPlan.getFloorPlan().getClippingPlane();
        float tileWidth = (float) this.glWindow.getWidth() / plane.getWidth();
        float tileHeight = (float) this.glWindow.getHeight() / plane.getHeight();
        logger.info("Tile Width x Height: " + tileWidth + " " + tileHeight);

        float totalTilesX = this.glWindow.getWidth() / tileWidth;
        float totalTilesY = this.glWindow.getHeight() / tileHeight;
        logger.info("Total Tiles (x, y): " + totalTilesX + ", " + totalTilesY);

        float spaceAroundX = totalTilesX - this.currentFloorPlan.getFloorPlan().getWidth();
        float spaceAroundY = totalTilesY - this.currentFloorPlan.getFloorPlan().getHeight();
        logger.info("Space Around (x, y): " + spaceAroundX + ", " + spaceAroundY);

        float planLeft = tileWidth * (spaceAroundX / 2);
        float planRight = planLeft + (tileWidth * this.currentFloorPlan.getFloorPlan().getWidth());
        float planBottom = tileHeight * (spaceAroundY / 2);
        float planTop = planBottom + (tileHeight * this.currentFloorPlan.getFloorPlan().getHeight());
        logger.info("Plan Starts Left x Bottom: " + planLeft + " " + planBottom);
        logger.info("Plan Ends Right x Top: " + planRight + " " + planTop);

        int col = this.getIndexFromCoordinate(mouseX, planLeft, planRight, tileWidth);
        int row = this.getIndexFromCoordinate(mouseY, planBottom, planTop, tileHeight);
        logger.info("Floor Plan Row x Col: " + row + " x " + col);

        if (col >= 0 && row >= 0) {
            this.currentFloorPlan.setRow(row);
            this.currentFloorPlan.setColumn(col);
            this.eventListener2D.setSelectedCol(col);
            this.eventListener2D.setSelectedRow(row);
        } else {
            this.currentFloorPlan.setRow(row);
            this.currentFloorPlan.setColumn(col);
            this.eventListener2D.setSelectedCol(-1);
            this.eventListener2D.setSelectedRow(-1);
        }
    }

    private int getIndexFromCoordinate(int coord, float start, float end, float increment) {
        int current = (int) start;
        int index = 0;
        for (int i = (int) start; i <= end; i += increment) {
            if (coord >= current && coord <= i) {
                return index - 1;
            }
            current = i;
            index++;
        }
        return -1;
    }

    // Other mouse events aren't used, but still need to be implemented...
    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseWheelMoved(MouseEvent e) {}
}
