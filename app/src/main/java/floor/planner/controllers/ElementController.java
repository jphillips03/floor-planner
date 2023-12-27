package floor.planner.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import floor.planner.constants.ObjectType;
import floor.planner.listeners.ObjectColorChangeListener;
import floor.planner.listeners.ObjectTypeChangeListener;
import floor.planner.models.Floor;
import floor.planner.util.math.Color;
import floor.planner.util.objects.obj2d.DrawableElement2D;
import floor.planner.util.objects.obj3d.DrawableElement3D;
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
    private ObjectColorChangeListener objectRedSpecularChangeListener;
    private ObjectColorChangeListener objectGreenSpecularChangeListener;
    private ObjectColorChangeListener objectBlueSpecularChangeListener;
    private ObjectMaterialChangeListener objectMaterialChangeListener;
    private ObjectTypeChangeListener objectTypeChangeListener;

    @FXML
    private ComboBox<ObjectType> objectTypeCombo;

    @FXML
    private Slider redSliderAmbientDiffuse;

    @FXML
    private Slider greenSliderAmbientDiffuse;

    @FXML
    private Slider blueSliderAmbientDiffuse;

    @FXML
    private Slider redSliderSpecular;

    @FXML
    private Slider greenSliderSpecular;

    @FXML
    private Slider blueSliderSpecular;

    public void setFloor(Floor f) {
        this.floor = f;
        this.objectTypeChangeListener.setFloor(f);
        this.objectRedChangeListener.setFloor(f);
        this.objectGreenChangeListener.setFloor(f);
        this.objectBlueChangeListener.setFloor(f);
        this.objectRedSpecularChangeListener.setFloor(f);
        this.objectGreenSpecularChangeListener.setFloor(f);
        this.objectBlueSpecularChangeListener.setFloor(f);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.objectTypeCombo.getItems().setAll(ObjectType.values());

        this.objectTypeChangeListener = new ObjectTypeChangeListener(this);
        this.objectTypeCombo.getSelectionModel().selectedItemProperty().addListener(this.objectTypeChangeListener);

        this.objectRedChangeListener = new ObjectColorChangeListener(0);
        this.redSliderAmbientDiffuse.valueProperty().addListener(this.objectRedChangeListener);

        this.objectGreenChangeListener = new ObjectColorChangeListener(1);
        this.greenSliderAmbientDiffuse.valueProperty().addListener(this.objectGreenChangeListener);

        this.objectBlueChangeListener = new ObjectColorChangeListener(2);
        this.blueSliderAmbientDiffuse.valueProperty().addListener(this.objectBlueChangeListener);

        this.objectRedSpecularChangeListener = new ObjectColorChangeListener(0, true);
        this.redSliderSpecular.valueProperty().addListener(this.objectRedSpecularChangeListener);

        this.objectGreenSpecularChangeListener = new ObjectColorChangeListener(1, true);
        this.greenSliderSpecular.valueProperty().addListener(this.objectGreenSpecularChangeListener);

        this.objectBlueSpecularChangeListener = new ObjectColorChangeListener(2, true);
        this.blueSliderSpecular.valueProperty().addListener(this.objectBlueSpecularChangeListener);
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
            this.redSliderAmbientDiffuse.setValue(color.getRed());
            this.greenSliderAmbientDiffuse.setValue(color.getGreen());
            this.blueSliderAmbientDiffuse.setValue(color.getBlue());
        } else {
            this.redSliderAmbientDiffuse.setValue(0);
            this.greenSliderAmbientDiffuse.setValue(0);
            this.blueSliderAmbientDiffuse.setValue(0);
        }

        this.objectRedChangeListener.setProgrammaticChange(false);
        this.objectGreenChangeListener.setProgrammaticChange(false);
        this.objectBlueChangeListener.setProgrammaticChange(false);

        this.setSpecularProperties();
    }

    private void setSpecularProperties() {
        this.objectRedSpecularChangeListener.setCol(col);
        this.objectRedSpecularChangeListener.setRow(row);
        this.objectRedSpecularChangeListener.setProgrammaticChange(true);
        this.objectGreenSpecularChangeListener.setCol(col);
        this.objectGreenSpecularChangeListener.setRow(row);
        this.objectGreenSpecularChangeListener.setProgrammaticChange(true);
        this.objectBlueSpecularChangeListener.setCol(col);
        this.objectBlueSpecularChangeListener.setRow(row);
        this.objectBlueSpecularChangeListener.setProgrammaticChange(true);

        DrawableElement3D[] elements = this.floor.getElements3D()[row][col];
        if (elements.length > 0) {
            float[] color = elements[0].getSpecular();
            this.redSliderSpecular.setValue(color[0]);
            this.greenSliderSpecular.setValue(color[1]);
            this.blueSliderSpecular.setValue(color[2]);
        } else {
            this.redSliderSpecular.setValue(0);
            this.greenSliderSpecular.setValue(0);
            this.blueSliderSpecular.setValue(0);
        }

        this.objectRedSpecularChangeListener.setProgrammaticChange(false);
        this.objectGreenSpecularChangeListener.setProgrammaticChange(false);
        this.objectBlueSpecularChangeListener.setProgrammaticChange(false);
    }
}
