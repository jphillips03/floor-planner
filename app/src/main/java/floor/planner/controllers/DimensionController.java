package floor.planner.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.newt.opengl.GLWindow;

import floor.planner.models.CurrentFloorPlan;
import floor.planner.util.jogl.event.KeyListenerMove3D;
import floor.planner.util.jogl.gleventlisteners.GLEventListener2D;
import floor.planner.util.jogl.gleventlisteners.GLEventListener3D;
import floor.planner.util.jogl.gleventlisteners.MouseListener2D;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;

public class DimensionController implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(DimensionController.class);

    private CurrentFloorPlan currentFloorPlan;
    private GLWindow glWindow;
    private GLEventListener2D eventListener2D;
    private GLEventListener3D eventListener3D;
    private KeyListenerMove3D keyListener3D;

    @FXML
    private RadioButton radioButton2D;

    @FXML
    private RadioButton radioButton3D;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.radioButton2D.setDisable(true);
        this.radioButton3D.setDisable(true);
    }

    @FXML
    private void onDimensionClick(ActionEvent event) {
        RadioButton button = (RadioButton) event.getSource();
        String data = (String) button.getUserData();
        if (data.equals("3d")) {
            this.init3D();
        } else {
            this.glWindow.removeGLEventListener(this.eventListener3D);
            this.glWindow.removeKeyListener(this.keyListener3D);
            this.init2D();
        }
    }

    public void enableControls() {
        this.radioButton2D.setDisable(false);
        this.radioButton3D.setDisable(false);

        // default to 2D
        this.radioButton2D.setSelected(true);
    }

    public void init2D() {
        this.eventListener2D = new GLEventListener2D(this.currentFloorPlan.getFloorPlan(), this.glWindow);
        this.glWindow.addGLEventListener(this.eventListener2D);
        this.glWindow.addMouseListener(
            new MouseListener2D(
                this.currentFloorPlan,
                this.glWindow,
                this.eventListener2D
            )
        );
    }

    private void init3D() {
        this.glWindow.removeGLEventListener(this.eventListener2D);
        this.eventListener3D = new GLEventListener3D(this.currentFloorPlan.getFloorPlan(), this.glWindow);
        this.glWindow.addGLEventListener(this.eventListener3D);

        this.keyListener3D = new KeyListenerMove3D(this.currentFloorPlan.getFloorPlan());
        this.glWindow.addKeyListener(this.keyListener3D);
        this.glWindow.requestFocus(); // so key events are registered and fire
    }

    public void setCurrentFloorPlan(CurrentFloorPlan fp) {
        this.currentFloorPlan = fp;
    }

    public void setGLWindow(GLWindow window) {
        this.glWindow = window;
    }
}
