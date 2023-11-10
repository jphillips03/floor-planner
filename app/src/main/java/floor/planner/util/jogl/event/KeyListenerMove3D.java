package floor.planner.util.jogl.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;

import floor.planner.models.Camera;
import floor.planner.models.FloorPlan;
import floor.planner.util.math.Point3D;

public class KeyListenerMove3D implements KeyListener {
    private static final Logger logger = LoggerFactory.getLogger(KeyListenerMove3D.class);

    private FloorPlan currentFloorPlan;

    public KeyListenerMove3D(FloorPlan floorPlan) {
        this.currentFloorPlan = floorPlan;
    }

    public void keyPressed(KeyEvent keyEvent) {
        Camera camera = this.currentFloorPlan.getCamera();
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
            camera.rotateZ(5f);
            //this.currentFloorPlan.setRotateZ(this.currentFloorPlan.getRotateZ() + 5f);
        }
        // Handle Right key pressed
        if(keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
            camera.rotateZ(-5f);
            //this.currentFloorPlan.setRotateZ(this.currentFloorPlan.getRotateZ() - 5f);
        }
        if(keyEvent.getKeyChar() == 'x') {
            camera.rotateX(5f);
        }
    }
    public void keyReleased(KeyEvent arg0) {}
}
