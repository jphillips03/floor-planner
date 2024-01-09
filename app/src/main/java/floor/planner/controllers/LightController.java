package floor.planner.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import floor.planner.constants.LightType;
import floor.planner.listeners.LightChangeListener;
import floor.planner.listeners.SliderColorListener;
import floor.planner.models.Light;

public class LightController implements Initializable {
    private Light light;
    private LightChangeListener ambientRedChangeListener;
    private LightChangeListener ambientGreenChangeListener;
    private LightChangeListener ambientBlueChangeListener;
    private LightChangeListener diffuseRedChangeListener;
    private LightChangeListener diffuseGreenChangeListener;
    private LightChangeListener diffuseBlueChangeListener;
    private LightChangeListener specularRedChangeListener;
    private LightChangeListener specularGreenChangeListener;
    private LightChangeListener specularBlueChangeListener;

    @FXML
    private Slider redSliderAmbient;

    @FXML
    private Slider greenSliderAmbient;

    @FXML
    private Slider blueSliderAmbient;

    @FXML
    private Slider redSliderDiffuse;

    @FXML
    private Slider greenSliderDiffuse;

    @FXML
    private Slider blueSliderDiffuse;

    @FXML
    private Slider redSliderSpecular;

    @FXML
    private Slider greenSliderSpecular;

    @FXML
    private Slider blueSliderSpecular;

    public void setLight(Light l) {
        this.light = l;
        this.ambientRedChangeListener.setLight(l);
        this.ambientGreenChangeListener.setLight(l);
        this.ambientBlueChangeListener.setLight(l);
        this.diffuseRedChangeListener.setLight(l);
        this.diffuseGreenChangeListener.setLight(l);
        this.diffuseBlueChangeListener.setLight(l);
        this.specularRedChangeListener.setLight(l);
        this.specularGreenChangeListener.setLight(l);
        this.specularBlueChangeListener.setLight(l);
        this.setLightDetails();
    }

    private void setLightDetails() {
        this.ambientRedChangeListener.setProgrammaticChange(true);
        this.ambientGreenChangeListener.setProgrammaticChange(true);
        this.ambientBlueChangeListener.setProgrammaticChange(true);
        this.diffuseRedChangeListener.setProgrammaticChange(true);
        this.diffuseGreenChangeListener.setProgrammaticChange(true);
        this.diffuseBlueChangeListener.setProgrammaticChange(true);
        this.specularRedChangeListener.setProgrammaticChange(true);
        this.specularGreenChangeListener.setProgrammaticChange(true);
        this.specularBlueChangeListener.setProgrammaticChange(true);

        this.redSliderAmbient.setValue(this.light.getAmbient()[0]);
        this.greenSliderAmbient.setValue(this.light.getAmbient()[1]);
        this.blueSliderAmbient.setValue(this.light.getAmbient()[2]);

        this.redSliderDiffuse.setValue(this.light.getDiffuse()[0]);
        this.greenSliderDiffuse.setValue(this.light.getDiffuse()[1]);
        this.blueSliderDiffuse.setValue(this.light.getDiffuse()[2]);

        this.redSliderSpecular.setValue(this.light.getSpecular()[0]);
        this.greenSliderSpecular.setValue(this.light.getSpecular()[1]);
        this.blueSliderSpecular.setValue(this.light.getSpecular()[2]);

        this.ambientRedChangeListener.setProgrammaticChange(false);
        this.ambientGreenChangeListener.setProgrammaticChange(false);
        this.ambientBlueChangeListener.setProgrammaticChange(false);
        this.diffuseRedChangeListener.setProgrammaticChange(false);
        this.diffuseGreenChangeListener.setProgrammaticChange(false);
        this.diffuseBlueChangeListener.setProgrammaticChange(false);
        this.specularRedChangeListener.setProgrammaticChange(false);
        this.specularGreenChangeListener.setProgrammaticChange(false);
        this.specularBlueChangeListener.setProgrammaticChange(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.ambientRedChangeListener = new LightChangeListener(0, LightType.AMBIENT);
        this.redSliderAmbient.valueProperty().addListener(this.ambientRedChangeListener);
        this.redSliderAmbient.valueProperty().addListener(new SliderColorListener(this.redSliderAmbient));

        this.ambientGreenChangeListener = new LightChangeListener(1, LightType.AMBIENT);
        this.greenSliderAmbient.valueProperty().addListener(this.ambientGreenChangeListener);
        this.greenSliderAmbient.valueProperty().addListener(new SliderColorListener(this.greenSliderAmbient));

        this.ambientBlueChangeListener = new LightChangeListener(2, LightType.AMBIENT);
        this.blueSliderAmbient.valueProperty().addListener(this.ambientBlueChangeListener);
        this.blueSliderAmbient.valueProperty().addListener(new SliderColorListener(this.blueSliderAmbient));

        this.diffuseRedChangeListener = new LightChangeListener(0, LightType.DIFFUSE);
        this.redSliderDiffuse.valueProperty().addListener(this.diffuseRedChangeListener);
        this.redSliderDiffuse.valueProperty().addListener(new SliderColorListener(this.redSliderDiffuse));

        this.diffuseGreenChangeListener = new LightChangeListener(1, LightType.DIFFUSE);
        this.greenSliderDiffuse.valueProperty().addListener(this.diffuseGreenChangeListener);
        this.greenSliderDiffuse.valueProperty().addListener(new SliderColorListener(this.greenSliderDiffuse));

        this.diffuseBlueChangeListener = new LightChangeListener(2, LightType.DIFFUSE);
        this.blueSliderDiffuse.valueProperty().addListener(this.diffuseBlueChangeListener);
        this.blueSliderDiffuse.valueProperty().addListener(new SliderColorListener(this.blueSliderDiffuse));

        this.specularRedChangeListener = new LightChangeListener(0, LightType.SPECULAR);
        this.redSliderSpecular.valueProperty().addListener(this.specularRedChangeListener);
        this.redSliderSpecular.valueProperty().addListener(new SliderColorListener(this.redSliderSpecular));

        this.specularGreenChangeListener = new LightChangeListener(1, LightType.SPECULAR);
        this.greenSliderSpecular.valueProperty().addListener(this.specularGreenChangeListener);
        this.greenSliderSpecular.valueProperty().addListener(new SliderColorListener(this.greenSliderSpecular));

        this.specularBlueChangeListener = new LightChangeListener(2, LightType.SPECULAR);
        this.blueSliderSpecular.valueProperty().addListener(this.specularBlueChangeListener);
        this.blueSliderSpecular.valueProperty().addListener(new SliderColorListener(this.blueSliderSpecular));

        this.setDisableControls(true);
    }

    public void setDisableControls(boolean disable) {
        this.redSliderAmbient.setDisable(disable);
        this.greenSliderAmbient.setDisable(disable);
        this.blueSliderAmbient.setDisable(disable);
        this.redSliderDiffuse.setDisable(disable);
        this.greenSliderDiffuse.setDisable(disable);
        this.blueSliderDiffuse.setDisable(disable);
        this.redSliderSpecular.setDisable(disable);
        this.greenSliderSpecular.setDisable(disable);
        this.blueSliderSpecular.setDisable(disable);
    }
}
