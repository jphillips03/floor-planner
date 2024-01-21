package floor.planner.util.jogl.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;

import floor.planner.models.Camera;
import floor.planner.models.FloorPlan;

public class KeyListenerMove3D implements KeyListener {
    private static final Logger logger = LoggerFactory.getLogger(KeyListenerMove3D.class);

    private FloorPlan currentFloorPlan;

    public KeyListenerMove3D(FloorPlan floorPlan) {
        this.currentFloorPlan = floorPlan;
    }

    public void keyPressed(KeyEvent keyEvent) {
        logger.debug("Key pressed in 3D view, checking KeyEvent...");

        Camera camera = this.currentFloorPlan.getCamera();

        // look up/down
        if(keyEvent.getKeyCode() == KeyEvent.VK_PAGE_UP) {
            camera.lookUp();
        }
        if(keyEvent.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
            camera.lookDown();
        }

        // move in/out
        if(keyEvent.getKeyCode() == KeyEvent.VK_UP) {
            camera.moveIn();
        }
        if(keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
            camera.moveOut();
        }

        // turn left/right
        if(keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
            camera.turnLeft();
        }
        if(keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
            camera.turnRight();
        }

        logger.debug("Key pressed event action complete");
    }
    public void keyReleased(KeyEvent arg0) {}
}
