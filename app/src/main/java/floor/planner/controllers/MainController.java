package floor.planner.controllers;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.FileChooser.ExtensionFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.newt.opengl.GLWindow;

import floor.planner.constants.RayTraceTaskType;
import floor.planner.models.FloorPlan;
import floor.planner.services.FloorPlanService;
import floor.planner.util.FileUtil;
import floor.planner.util.jogl.event.KeyListenerMove3D;
import floor.planner.util.jogl.gleventlisteners.GLEventListener2D;
import floor.planner.util.jogl.gleventlisteners.GLEventListener3D;
import floor.planner.util.jogl.gleventlisteners.MouseListener2D;
import floor.planner.util.raytracer.RayTraceTask;

public class MainController implements Initializable {
    /** The logger for the class. */
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    private File currentFile;
    private FloorPlan currentFloorPlan;
    private GLWindow glWindow;
    private Stage stage;
    private Window window;
    private FloorPlanService floorPlanService = new FloorPlanService();
    private GLEventListener2D eventListener2D;
    private GLEventListener3D eventListener3D;
    private KeyListenerMove3D keyListener3D;

    @FXML
    ElementController elementController;

    @FXML
    Button center3D;

    @FXML
    BorderPane mainBox;

    @FXML
    StackPane openGLPane;

    /** The menu for switching between dimensions. */
    @FXML
    Menu dimensionMenu;

    /** The menu for selecting Floor (used in 2D view). */
    @FXML
    Menu floorMenu;

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
        this.dimensionMenu.setDisable(true);
        this.floorMenu.setDisable(true);
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
        this.dimensionMenu.setDisable(false);
        this.initializeFloorsMenu(floors);

        this.center3D.setDisable(false);
        this.rayTraceFloorPlanMenuItem.setDisable(false);
        this.saveAsMenuItem.setDisable(false);
        this.saveMenuItem.setDisable(false);
    }

    /**
     * Initializes the "Floors" menu with number of floors for the file.
     *
     * @param floors The number of floors to add to menu.
     */
    private void initializeFloorsMenu(int floors) {
        // enable the menu (in case it was disabled) and clear existing items
        this.floorMenu.setDisable(false);
        this.floorMenu.getItems().clear();

        // update current floor for ElementController
        this.elementController.setFloor(this.currentFloorPlan.getFloor(0));

        // add a MenuItem for each floor
        for (int i = 0; i < floors; i++) {
            int floor = i;
            MenuItem menuItem = new MenuItem("Floor " + (i + 1));
            this.floorMenu.getItems().add(menuItem);

            // add handler for when menuItem is clicked
            menuItem.setOnAction(e -> {
                e.consume(); // stop further propagation of event

                // just update the current floor so the GLEventListener2D
                // display method renders the correct floor (since it uses the
                // currentFloor property from FloorPlan)
                this.currentFloorPlan.setCurrentFloor(floor);
                this.elementController.setFloor(this.currentFloorPlan.getFloor(floor));
            });
        }
    }

    @FXML
    private void onCenter3D(ActionEvent event) {
        logger.info("Re-center image");
        this.currentFloorPlan.getCamera().reset();
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
            this.currentFloorPlan = floorPlan.get();
            this.initializeMenus(this.currentFloorPlan.getFloorNumbers());
            this.init2D();
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
            this.currentFloorPlan = this.floorPlanService.create(contents);
            this.initializeMenus(this.currentFloorPlan.getFloorNumbers());
            this.init2D();
        }
    }

    @FXML
    private void onMenu2D(ActionEvent event) {
        this.glWindow.removeGLEventListener(this.eventListener3D);
        this.glWindow.removeKeyListener(this.keyListener3D);
        this.init2D();
    }

    @FXML
    private void onMenuSave(ActionEvent event) {
        if (this.currentFile != null) {
            this.floorPlanService.save(currentFile, this.currentFloorPlan);
        }
    }

    private void init2D() {
        this.eventListener2D = new GLEventListener2D(this.currentFloorPlan, this.glWindow);
        this.glWindow.addGLEventListener(this.eventListener2D);
        this.glWindow.addMouseListener(new MouseListener2D(this.currentFloorPlan, this.glWindow, this.elementController));
    }

    @FXML
    private void onMenu3D(ActionEvent event) {
        this.glWindow.removeGLEventListener(this.eventListener2D);
        this.eventListener3D = new GLEventListener3D(this.currentFloorPlan, this.glWindow);
        this.glWindow.addGLEventListener(this.eventListener3D);

        this.keyListener3D = new KeyListenerMove3D(this.currentFloorPlan);
        this.glWindow.addKeyListener(this.keyListener3D);
        this.glWindow.requestFocus(); // so key events are registered and fire
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
            600 : type.equals(RayTraceTaskType.THREE_D) ?
            450 : 225; // this.glWindow.getHeight()
        int width = cornellBoxes.contains(type) ?
            600 : type.equals(RayTraceTaskType.THREE_D) ?
            800 : 400; // this.glWindow.getWidth()
        int samplesPerPixel = 10;
        int sqrtSpp = (int) Math.sqrt(samplesPerPixel);
        int maxDepth = 50;

        // height * width rays are sent through screen, we do this 
        // samplesPerPixel times for each ray and run through each element each
        // time...
        int max = height * width * (sqrtSpp * sqrtSpp);

        RayTraceTask task;
        if (type.equals(RayTraceTaskType.THREE_D)) {
            task = new RayTraceTask(this.currentFloorPlan, height, width, max, maxDepth, samplesPerPixel);
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

    public void setGLWindow(GLWindow window) {
        this.glWindow = window;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public MenuBar getMenuBar() {
        return this.menuBar;
    }

    public StackPane getOpenGLPane() {
        return this.openGLPane;
    }

    public Window getWindow() {
        return this.window;
    }
    public void setWindow(Window window) {
        this.window = window;
    }
}
