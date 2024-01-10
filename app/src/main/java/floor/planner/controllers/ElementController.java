package floor.planner.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import floor.planner.constants.MaterialType;
import floor.planner.constants.ObjectType;
import floor.planner.listeners.ObjectColorChangeListener;
import floor.planner.listeners.ObjectMaterialChangeListener;
import floor.planner.listeners.ObjectTypeChangeListener;
import floor.planner.listeners.SliderColorListener;
import floor.planner.models.Floor;
import floor.planner.util.math.Color;
import floor.planner.util.objects.obj2d.DrawableElement2D;
import floor.planner.util.objects.obj3d.DrawableElement3D;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
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
    private ComboBox<MaterialType> materialTypeCombo;

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
        this.objectMaterialChangeListener.setFloor(f);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.materialTypeCombo.getItems().setAll(MaterialType.values());

        this.objectMaterialChangeListener = new ObjectMaterialChangeListener();
        this.materialTypeCombo.getSelectionModel().selectedItemProperty().addListener(objectMaterialChangeListener);
        // below ensures promptText rendered when combobox reset
        this.materialTypeCombo.setButtonCell(new ListCellCustom<MaterialType>("Select Material"));

        this.objectTypeCombo.getItems().setAll(ObjectType.values());
        // below ensures promptText rendered when combobox reset
        this.objectTypeCombo.setButtonCell(new ListCellCustom<ObjectType>("Select Type"));

        this.objectTypeChangeListener = new ObjectTypeChangeListener(this);
        this.objectTypeCombo.getSelectionModel().selectedItemProperty().addListener(this.objectTypeChangeListener);

        this.objectRedChangeListener = new ObjectColorChangeListener(0);
        this.redSliderAmbientDiffuse.valueProperty().addListener(this.objectRedChangeListener);
        this.redSliderAmbientDiffuse.valueProperty().addListener(new SliderColorListener(this.redSliderAmbientDiffuse));

        this.objectGreenChangeListener = new ObjectColorChangeListener(1);
        this.greenSliderAmbientDiffuse.valueProperty().addListener(this.objectGreenChangeListener);
        this.greenSliderAmbientDiffuse.valueProperty().addListener(new SliderColorListener(this.greenSliderAmbientDiffuse));

        this.objectBlueChangeListener = new ObjectColorChangeListener(2);
        this.blueSliderAmbientDiffuse.valueProperty().addListener(this.objectBlueChangeListener);
        this.blueSliderAmbientDiffuse.valueProperty().addListener(new SliderColorListener(this.blueSliderAmbientDiffuse));

        this.objectRedSpecularChangeListener = new ObjectColorChangeListener(0, true);
        this.redSliderSpecular.valueProperty().addListener(this.objectRedSpecularChangeListener);
        this.redSliderSpecular.valueProperty().addListener(new SliderColorListener(this.redSliderSpecular));

        this.objectGreenSpecularChangeListener = new ObjectColorChangeListener(1, true);
        this.greenSliderSpecular.valueProperty().addListener(this.objectGreenSpecularChangeListener);
        this.greenSliderSpecular.valueProperty().addListener(new SliderColorListener(this.greenSliderSpecular));

        this.objectBlueSpecularChangeListener = new ObjectColorChangeListener(2, true);
        this.blueSliderSpecular.valueProperty().addListener(this.objectBlueSpecularChangeListener);
        this.blueSliderSpecular.valueProperty().addListener(new SliderColorListener(this.blueSliderSpecular));

        this.setDisableControls(true);
    }

    public void setDisableControls(boolean disable) {
        this.materialTypeCombo.setDisable(disable);
        this.objectTypeCombo.setDisable(disable);
        this.redSliderAmbientDiffuse.setDisable(disable);
        this.greenSliderAmbientDiffuse.setDisable(disable);
        this.blueSliderAmbientDiffuse.setDisable(disable);
        this.redSliderSpecular.setDisable(disable);
        this.greenSliderSpecular.setDisable(disable);
        this.blueSliderSpecular.setDisable(disable);
    }

    public void resetElementDetails() {
        this.row = -1;
        this.col = -1;

        this.setProgrammaticChange(true);
        this.setChangeListenerProperties(-1, -1);
        this.objectTypeCombo.setValue(null);
        this.objectTypeCombo.getSelectionModel().clearSelection();
        
        this.materialTypeCombo.setValue(null);
        this.materialTypeCombo.getSelectionModel().clearSelection();

        this.redSliderAmbientDiffuse.setValue(0);
        this.greenSliderAmbientDiffuse.setValue(0);
        this.blueSliderAmbientDiffuse.setValue(0);

        this.redSliderSpecular.setValue(0);
        this.greenSliderSpecular.setValue(0);
        this.blueSliderSpecular.setValue(0);
    }

    public void setElementDetails(int row, int col) {
        this.row = row;
        this.col = col;
        // set programmaticChange to true so change listener does not fire,
        // otherwise the previous element selected (if it exists) will change
        // based on the new element selected when the combo box value is set
        // below...
        this.setProgrammaticChange(true);
        this.setChangeListenerProperties(row, col);

        this.objectTypeCombo.setValue(this.floor.getElementByRowAndCol(row, col));
        this.materialTypeCombo.setValue(this.floor.getMaterialTypeByRowAndCol(row, col));
        this.setAmbientDiffuseProperties();
        this.setSpecularProperties();

        // reset programmaticChange to false so user can change element
        // properties using controls
        this.setProgrammaticChange(false);
    }

    private void setChangeListenerProperties(int row, int col) {
        this.objectMaterialChangeListener.setCol(col);
        this.objectMaterialChangeListener.setRow(row);

        this.objectTypeChangeListener.setCol(col);
        this.objectTypeChangeListener.setRow(row);

        this.setColorChangeListenerProperties(row, col);
    }

    private void setColorChangeListenerProperties(int row, int col) {
        this.objectRedSpecularChangeListener.setCol(col);
        this.objectRedSpecularChangeListener.setRow(row);

        this.objectGreenSpecularChangeListener.setCol(col);
        this.objectGreenSpecularChangeListener.setRow(row);

        this.objectBlueSpecularChangeListener.setCol(col);
        this.objectBlueSpecularChangeListener.setRow(row);

        this.objectRedChangeListener.setCol(col);
        this.objectRedChangeListener.setRow(row);

        this.objectGreenChangeListener.setCol(col);
        this.objectGreenChangeListener.setRow(row);

        this.objectBlueChangeListener.setCol(col);
        this.objectBlueChangeListener.setRow(row);
    }

    public void setElementColorDetails() {
        this.setColorsProgrammaticChange(true);
        this.setColorChangeListenerProperties(row, col);

        this.setAmbientDiffuseProperties();
        this.setSpecularProperties();

        this.setColorsProgrammaticChange(false);
    }

    private void setProgrammaticChange(boolean change) {
        this.objectMaterialChangeListener.setProgrammaticChange(change);
        this.objectTypeChangeListener.setProgrammaticChange(change);
        this.setColorsProgrammaticChange(change);
    }

    private void setColorsProgrammaticChange(boolean change) {
        this.objectRedChangeListener.setProgrammaticChange(change);
        this.objectGreenChangeListener.setProgrammaticChange(change);
        this.objectBlueChangeListener.setProgrammaticChange(change);

        this.objectRedSpecularChangeListener.setProgrammaticChange(change);
        this.objectGreenSpecularChangeListener.setProgrammaticChange(change);
        this.objectBlueSpecularChangeListener.setProgrammaticChange(change);
    }

    private void setAmbientDiffuseProperties() {
        this.elements2D = this.floor.getElements()[row][col];
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
    }

    private void setSpecularProperties() {
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
    }

    private class ListCellCustom<T> extends ListCell<T> {
        private String promptText;

        public ListCellCustom(String promptText) {
            this.promptText = promptText;
        }

        @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(this.promptText);
                } else {
                    setText(item.toString());
                }
            }
    }
}
