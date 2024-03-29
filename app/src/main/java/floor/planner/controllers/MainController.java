package floor.planner.controllers;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.FileChooser.ExtensionFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.newt.opengl.GLWindow;

import floor.planner.constants.RayTraceTaskType;
import floor.planner.models.CurrentFloorPlan;
import floor.planner.models.Floor;
import floor.planner.models.FloorPlan;
import floor.planner.services.FloorPlanService;
import floor.planner.util.FileUtil;
import floor.planner.util.raytracer.RayTraceTask;

public class MainController implements Initializable {
    /** The logger for the class. */
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    private CurrentFloorPlan currentFloorPlan;
    private File currentFile;
    private FloorPlan floorPlan;
    private GLWindow glWindow;
    private Stage stage;
    private Window window;
    private FloorPlanService floorPlanService = new FloorPlanService();

    @FXML
    DimensionController dimensionController;

    @FXML
    ElementController elementController;

    @FXML
    FloorController floorController;

    @FXML
    LightController lightController;

    @FXML
    Button center3D;

    @FXML
    BorderPane mainBox;

    @FXML
    VBox openGLPane;

    @FXML
    MenuBar menuBar;

    @FXML
    MenuItem rayTraceFloorPlanMenuItem;

    @FXML
    MenuItem saveAsMenuItem;

    @FXML
    MenuItem saveMenuItem;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.center3D.setDisable(true);
        this.rayTraceFloorPlanMenuItem.setDisable(true);
        this.saveAsMenuItem.setDisable(true);
        this.saveMenuItem.setDisable(true);
    }

    /**
     * Initializes menus; enables each disabled menu (in case they were
     * disabled), and adds the number of floors based on given value.
     *
     * @param floors The number of floors to add to menu.
     */
    private void initializeMenus(int floors) {
        this.center3D.setDisable(false);
        this.rayTraceFloorPlanMenuItem.setDisable(false);
        this.saveAsMenuItem.setDisable(false);
        this.saveMenuItem.setDisable(false);
    }

    @FXML
    private void onCenter3D(ActionEvent event) {
        logger.info("Re-center image");
        this.floorPlan.getCamera().reset();
    }

    /**
     * Handler for when user clicks "New File" MenuItem.
     *
     * @param event
     */
    @FXML
    private void onMenuNewFile(ActionEvent event) {
        logger.info("Create New File");
        NewDialogController newDialog = new NewDialogController(this.stage);
        Optional<FloorPlan> floorPlan = newDialog.showAndWait();
        if (floorPlan.isPresent()) {
            this.initFloorPlan(floorPlan.get());
        }
    }

    /**
     * Handler for when user clicks "Open File" MenuItem.
     *
     * @param event
     */
    @FXML
    private void onMenuOpenFile(ActionEvent event) {
        logger.info("Open Existing File");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
            new ExtensionFilter("Text Files", "*.txt")
        );
        currentFile = fileChooser.showOpenDialog(this.window);
        if (currentFile != null) {
            String contents = FileUtil.read(currentFile);
            this.initFloorPlan(this.floorPlanService.create(contents));
        }
    }

    private void initFloorPlan(FloorPlan floorPlan) {
        this.floorPlan = floorPlan;
        this.currentFloorPlan.setFloorPlan(floorPlan);
        this.currentFloorPlan.setFloor(this.floorPlan.getFloor(0));
        this.lightController.setDisableControls(false);
        this.lightController.setLight(this.floorPlan.getLight());
        this.initializeMenus(this.floorPlan.getFloorNumbers());
        this.dimensionController.init2D();
        this.dimensionController.enableControls();
    }

    @FXML
    private void onMenuSave(ActionEvent event) {
        if (this.currentFile != null) {
            this.floorPlanService.save(currentFile, this.floorPlan);
        }
    }

    @FXML
    private void onRayTrace(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        String data = (String) menuItem.getUserData();
        RayTraceTaskType type = RayTraceTaskType.valueOf(data);
        List<RayTraceTaskType> cornellBoxes = Arrays.asList(
            RayTraceTaskType.CORNELL_BOX,
            RayTraceTaskType.CORNELL_BOX_GLASS,
            RayTraceTaskType.CORNELL_BOX_METAL
        );

        int height = cornellBoxes.contains(type) ? 
            600 : this.glWindow.getHeight();
        int width = cornellBoxes.contains(type) ?
            600 : this.glWindow.getWidth();
        int samplesPerPixel = 100;
        int sqrtSpp = (int) Math.sqrt(samplesPerPixel);
        int maxDepth = 50;

        // height * width rays are sent through screen, we do this 
        // samplesPerPixel times for each ray and run through each element each
        // time...
        int max = height * width * (sqrtSpp * sqrtSpp);

        RayTraceTask task;
        if (type.equals(RayTraceTaskType.THREE_D)) {
            task = new RayTraceTask(this.floorPlan, height, width, max, maxDepth, samplesPerPixel);
        } else {
            task = new RayTraceTask(height, width, max, maxDepth, type, samplesPerPixel);
        }

        ProgressBarDialogController progressBar = new ProgressBarDialogController(this.stage);
        progressBar.activateProgressBar(task);

        task.setOnSucceeded(e -> {
            progressBar.getDialogStage().close();
        });

        progressBar.getDialogStage().show();
        Thread thread = new Thread(task);
        thread.start();
    }

    public void setCurrentFloorPlan(CurrentFloorPlan cfp) {
        this.currentFloorPlan = cfp;
        this.dimensionController.setCurrentFloorPlan(cfp);
        this.addCurrentFloorPlanListeners();
    }

    private void addCurrentFloorPlanListeners() {
        // add FloorPlan property listener
        FloorController floorController = this.floorController;
        this.currentFloorPlan.floorPlanProperty().addListener(
            new ChangeListener<FloorPlan>() {
                @Override public void changed(
                    ObservableValue<? extends FloorPlan> o,
                    FloorPlan oldVal,
                    FloorPlan newVal
                ) {
                    floorController.setCurrentFloorPlan(newVal);
                    floorController.initializeFloorOptions(newVal.getFloorNumbers());
                    floorController.setFloorComboDisable(false);
                }
            }
        );

        // add Floor property listener
        ElementController elementController = this.elementController;
        this.currentFloorPlan.floorProperty().addListener(
            new ChangeListener<Floor>() {
                @Override public void changed(
                    ObservableValue<? extends Floor> o,
                    Floor oldVal,
                    Floor newVal
                ) {
                    elementController.setFloor(newVal);
                }
            }
        );

        // add row and col property listeners
        this.currentFloorPlan.columnProperty().addListener(
            new ChangeListener<Number>() {
                @Override public void changed(
                    ObservableValue<? extends Number> o,
                    Number oldVal,
                    Number newVal
                ) {
                    elementController.setElementDetails(
                        currentFloorPlan.getRow(),
                        newVal.intValue()
                    );
                }
            }
        );
        this.currentFloorPlan.rowProperty().addListener(
            new ChangeListener<Number>() {
                @Override public void changed(
                    ObservableValue<? extends Number> o,
                    Number oldVal,
                    Number newVal
                ) {
                    elementController.setElementDetails(
                        newVal.intValue(),
                        currentFloorPlan.getColumn()
                    );
                }
            }
        );
    }

    public void setGLWindow(GLWindow window) {
        this.glWindow = window;
        this.dimensionController.setGLWindow(window);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public MenuBar getMenuBar() {
        return this.menuBar;
    }

    public VBox getOpenGLPane() {
        return this.openGLPane;
    }

    public Window getWindow() {
        return this.window;
    }
    public void setWindow(Window window) {
        this.window = window;
    }
}
