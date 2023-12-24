package floor.planner.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import floor.planner.constants.ObjectType;
import floor.planner.listeners.ObjectColorChangeListener;
import floor.planner.listeners.ObjectTypeChangeListener;
import floor.planner.models.Floor;
import floor.planner.util.math.Color;
import floor.planner.util.objects.obj2d.DrawableElement2D;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;

public class ElementController implements Initializable {

    private int col;
    private int row;
    private DrawableElement2D[] elements2D;
    private Floor floor;
    private ObjectColorChangeListener objectRedChangeListener;
    private ObjectColorChangeListener objectGreenChangeListener;
    private ObjectColorChangeListener objectBlueChangeListener;
    private ObjectTypeChangeListener objectTypeChangeListener;

    @FXML
    private ComboBox<ObjectType> objectTypeCombo;

    @FXML
    private Slider redSlider;

    @FXML
    private Slider greenSlider;

    @FXML
    private Slider blueSlider;

    public void setFloor(Floor f) {
        this.floor = f;
        this.objectTypeChangeListener.setFloor(f);
        this.objectRedChangeListener.setFloor(f);
        this.objectGreenChangeListener.setFloor(f);
        this.objectBlueChangeListener.setFloor(f);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.objectTypeCombo.getItems().setAll(ObjectType.values());

        this.objectTypeChangeListener = new ObjectTypeChangeListener(this);
        this.objectTypeCombo.getSelectionModel().selectedItemProperty().addListener(this.objectTypeChangeListener);

        this.objectRedChangeListener = new ObjectColorChangeListener(0);
        this.redSlider.valueProperty().addListener(this.objectRedChangeListener);

        this.objectGreenChangeListener = new ObjectColorChangeListener(1);
        this.greenSlider.valueProperty().addListener(this.objectGreenChangeListener);

        this.objectBlueChangeListener = new ObjectColorChangeListener(2);
        this.blueSlider.valueProperty().addListener(this.objectBlueChangeListener);
    }

    public void setElementDetails(int row, int col) {
        this.row = row;
        this.col = col;
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

        this.setElementColorDetails();
    }

    public void setElementColorDetails() {
        this.elements2D = this.floor.getElements()[row][col];
        this.objectRedChangeListener.setCol(col);
        this.objectRedChangeListener.setRow(row);
        this.objectRedChangeListener.setProgrammaticChange(true);
        this.objectGreenChangeListener.setCol(col);
        this.objectGreenChangeListener.setRow(row);
        this.objectGreenChangeListener.setProgrammaticChange(true);
        this.objectBlueChangeListener.setCol(col);
        this.objectBlueChangeListener.setRow(row);
        this.objectBlueChangeListener.setProgrammaticChange(true);

        if (this.elements2D.length > 0) {
            Color color = this.elements2D[0].getColor();
            this.redSlider.setValue(color.getRed());
            this.greenSlider.setValue(color.getGreen());
            this.blueSlider.setValue(color.getBlue());
        } else {
            this.redSlider.setValue(0);
            this.greenSlider.setValue(0);
            this.blueSlider.setValue(0);
        }

        this.objectRedChangeListener.setProgrammaticChange(false);
        this.objectGreenChangeListener.setProgrammaticChange(false);
        this.objectBlueChangeListener.setProgrammaticChange(false);
    }
}
