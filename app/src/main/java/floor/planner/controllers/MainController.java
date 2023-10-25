package floor.planner.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainController implements Initializable {
    /** The logger for the class. */
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @FXML
    BorderPane rootPane;

    /** The menu for switching between dimensions. */
    @FXML
    Menu dimensionMenu;

    /** The menu for selecting Floor (used in 2D view). */
    @FXML
    Menu floorMenu;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.dimensionMenu.setDisable(true);
        this.floorMenu.setDisable(true);
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
        this.initializeMenus(1);
    }
}
