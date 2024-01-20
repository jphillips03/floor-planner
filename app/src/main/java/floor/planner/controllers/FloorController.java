package floor.planner.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import floor.planner.listeners.FloorChangeListener;
import floor.planner.models.FloorOption;
import floor.planner.models.FloorPlan;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

public class FloorController implements Initializable {

    private FloorChangeListener floorChangeListener;

    @FXML
    private ComboBox<FloorOption> floorCombo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.floorCombo.setDisable(true);
        this.floorChangeListener = new FloorChangeListener();
        this.floorCombo.getSelectionModel().selectedItemProperty().addListener(this.floorChangeListener);
    }

    public void setFloorComboDisable(boolean val) {
        if (this.floorCombo.isDisabled()) {
            this.floorCombo.setDisable(false);
        }
    }

    /**
     * Initializes the "Floors" combo with number of floors for the file.
     *
     * @param floors The number of floors to add to the combo box.
     */
    public void initializeFloorOptions(int floors) {
        // generate options
        FloorOption[] options = new FloorOption[floors];
        for (int i = 0; i < floors; i++) {
            options[i] = new FloorOption(i + 1);
        }

        this.floorCombo.getItems().setAll(options);
        this.floorCombo.setValue(options[0]);
    }

    public void setCurrentFloorPlan(FloorPlan cfp) {
        this.floorChangeListener.setCurrentFloorPlan(cfp);
    }
}
