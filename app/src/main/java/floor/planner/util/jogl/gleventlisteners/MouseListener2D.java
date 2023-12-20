package floor.planner.util.jogl.gleventlisteners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.newt.opengl.GLWindow;

import floor.planner.controllers.Menu2DController;
import floor.planner.models.FloorPlan;
import floor.planner.util.objects.obj2d.ClippingPlane;

public class MouseListener2D implements MouseListener {
    private static final Logger logger = LoggerFactory.getLogger(MouseListener2D.class);

    private FloorPlan floorPlan;
    private GLWindow glWindow;
    private Menu2DController menu2D;

    public MouseListener2D(FloorPlan floorPlan, GLWindow glWindow, Menu2DController menu2D) {
        this.floorPlan = floorPlan;
        this.glWindow = glWindow;
        this.menu2D = menu2D;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        logger.info("Window Click: " + mouseX + " " + mouseY);

        ClippingPlane plane = this.floorPlan.getClippingPlane();
        float tileWidth = (float) this.glWindow.getWidth() / plane.getWidth();
        float tileHeight = (float) this.glWindow.getHeight() / plane.getHeight();
        logger.info("Tile Width x Height: " + tileWidth + " " + tileHeight);

        float totalTilesX = this.glWindow.getWidth() / tileWidth;
        float totalTilesY = this.glWindow.getHeight() / tileHeight;
        logger.info("Total Tiles (x, y): " + totalTilesX + ", " + totalTilesY);

        float spaceAroundX = totalTilesX - this.floorPlan.getWidth();
        float spaceAroundY = totalTilesY - this.floorPlan.getHeight();
        logger.info("Space Around (x, y): " + spaceAroundX + ", " + spaceAroundY);

        float planLeft = tileWidth * (spaceAroundX / 2);
        float planRight = planLeft + (tileWidth * this.floorPlan.getWidth());
        float planBottom = tileHeight * (spaceAroundY / 2);
        float planTop = planBottom + (tileHeight * this.floorPlan.getHeight());
        logger.info("Plan Starts Left x Bottom: " + planLeft + " " + planBottom);
        logger.info("Plan Ends Right x Top: " + planRight + " " + planTop);

        int col = this.getIndexFromCoordinate(mouseX, planLeft, planRight, tileWidth);
        int row = this.getIndexFromCoordinate(mouseY, planBottom, planTop, tileHeight);
        logger.info("Floor Plan Row x Col: " + row + " x " + col);

        if (col >= 0 && row >= 0) {
            this.menu2D.initialize((double) mouseX, (double) mouseY, col, row);
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
