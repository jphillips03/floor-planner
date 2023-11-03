package floor.planner.controllers;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.stage.FileChooser.ExtensionFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.newt.opengl.GLWindow;

import floor.planner.models.FloorPlan;
import floor.planner.services.FloorPlanService;
import floor.planner.util.FileUtil;
import floor.planner.util.jogl.gleventlisteners.TwoDGLEventListener;
import floor.planner.util.jogl.gleventlisteners.TwoDMouseListener;

public class MainController implements Initializable {
    /** The logger for the class. */
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    private GLWindow glWindow;
    private Scene scene;
    private Window window;
    private FloorPlanService floorPlanService = new FloorPlanService();

    @FXML
    VBox mainBox;

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.dimensionMenu.setDisable(true);
        this.floorMenu.setDisable(true);

        // wait until initialization is complete to run the following otherwise
        // scene will be null
        Platform.runLater(() -> {
            this.scene = this.mainBox.getScene();
            this.window = this.scene.getWindow();
        });
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

        // add a MenuItem for each floor
        for (int i = 0; i < floors; i++) {
            this.floorMenu.getItems().add(new MenuItem("Floor " + (i + 1)));
        }
    }

    /**
     * Handler for when user clicks "New File" MenuItem.
     *
     * @param event
     */
    @FXML
    private void onMenuNewFile(ActionEvent event) {
        logger.info("Create New File");
        this.initializeMenus(1);
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
        File selectedFile = fileChooser.showOpenDialog(this.window);
        if (selectedFile != null) {
            String contents = FileUtil.read(selectedFile);
            FloorPlan plan = this.floorPlanService.create(contents);
            this.initializeMenus(plan.getFloorNumbers());

            this.glWindow.addGLEventListener(new TwoDGLEventListener(plan, this.glWindow));
        }
    }

    public void setGLWindow(GLWindow window) {
        this.glWindow = window;
    }

    public MenuBar getMenuBar() {
        return this.menuBar;
    }

    public StackPane getOpenGLPane() {
        return this.openGLPane;
    }
}
