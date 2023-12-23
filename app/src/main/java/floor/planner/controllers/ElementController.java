package floor.planner.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import floor.planner.constants.ObjectType;
import floor.planner.listeners.ObjectTypeChangeListener;
import floor.planner.models.Floor;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

public class ElementController implements Initializable {

    private Floor floor;
    private ObjectTypeChangeListener objectTypeChangeListener;

    @FXML
    private ComboBox<ObjectType> objectTypeCombo;

    @FXML
    private ComboBox<String> colorCombo;

    public void setFloor(Floor f) {
        this.floor = f;
        this.objectTypeChangeListener.setFloor(f);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.objectTypeCombo.getItems().setAll(ObjectType.values());
        this.colorCombo.getItems().setAll("Red", "Green", "Blue");

        this.objectTypeChangeListener = new ObjectTypeChangeListener();
        this.objectTypeCombo.getSelectionModel().selectedItemProperty().addListener(this.objectTypeChangeListener);
    }

    public void setElementDetails(int row, int col) {
        // set programmaticChange to true so change listener does not fire,
        // otherwise the previous element selected (if it exists) will change
        // based on the new element selected when the combo box value is set
        // below...
        this.objectTypeChangeListener.setProgrammaticChange(true);
        this.objectTypeChangeListener.setCol(col);
        this.objectTypeChangeListener.setRow(row);

        this.objectTypeCombo.setValue(this.floor.getElementByRowAndCol(row, col));

        // reset programmaticChange to false so user can change element at
        // selected row x col using the combo box
        this.objectTypeChangeListener.setProgrammaticChange(false);
    }
}
