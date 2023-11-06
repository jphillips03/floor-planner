package floor.planner.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.layout.BorderPane;
import javafx.stage.Window;

public class Menu2DController {
    private Window window;

    @FXML
    private ContextMenu contextMenu;

    @FXML
    private BorderPane borderPane;

    public void initialize(double x, double y) {
        contextMenu.show(this.window, x, y);
    }

    public BorderPane getBorderPane() {
        return this.borderPane;
    }

    public void setWindow(Window window) {
        this.window = window;
    }
}
