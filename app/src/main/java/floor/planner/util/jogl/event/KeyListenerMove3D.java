package floor.planner.util.jogl.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;

import floor.planner.models.FloorPlan;
import floor.planner.util.math.Point3D;

public class KeyListenerMove3D implements KeyListener {
    private static final Logger logger = LoggerFactory.getLogger(KeyListenerMove3D.class);

    private FloorPlan currentFloorPlan;

    public KeyListenerMove3D(FloorPlan floorPlan) {
        this.currentFloorPlan = floorPlan;
    }

    public void keyPressed(KeyEvent keyEvent) {
        Point3D cameraPosition = this.currentFloorPlan.getCameraPosition();
        // Handle i key pressed
        if(keyEvent.getKeyCode() == 73) {
            this.currentFloorPlan.setZoom(this.currentFloorPlan.getZoom() + 0.25f);
        }
        // Handle o key pressed
        if(keyEvent.getKeyCode() == 79) {
            this.currentFloorPlan.setZoom(this.currentFloorPlan.getZoom() - 0.25f);
        }
        // Handle Up key pressed
        if(keyEvent.getKeyCode() == KeyEvent.VK_UP) {
            this.currentFloorPlan.setZoom(this.currentFloorPlan.getZoom() + 0.25f);
        }
        // Handle Down key pressed
        if(keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
            this.currentFloorPlan.setZoom(this.currentFloorPlan.getZoom() - 0.25f);
        }
        // Handle Left key pressed
        if(keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
            this.currentFloorPlan.setRotateZ(this.currentFloorPlan.getRotateZ() + 5f);
        }
        // Handle Right key pressed
        if(keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
            this.currentFloorPlan.setRotateZ(this.currentFloorPlan.getRotateZ() - 5f);
        }
        if(keyEvent.getKeyChar() == 'x') {
            this.currentFloorPlan.setRotateX(this.currentFloorPlan.getRotateX() + 5f);
        }

        this.currentFloorPlan.setCameraPosition(cameraPosition);
    }
    public void keyReleased(KeyEvent arg0) {}
}
