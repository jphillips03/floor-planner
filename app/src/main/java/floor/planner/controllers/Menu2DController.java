package floor.planner.controllers;

import floor.planner.constants.ObjectType;
import floor.planner.models.Floor;
import floor.planner.models.FloorPlan;
import floor.planner.services.FloorService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Window;

public class Menu2DController {
    private int row;
    private int col;
    private FloorPlan currentFloorPlan;
    private Window window;
    private FloorService floorService = new FloorService();

    @FXML
    private ContextMenu contextMenu;

    @FXML
    private BorderPane borderPane;

    @FXML
    private void onMenuItemClick(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        String data = (String) menuItem.getUserData();
        Floor floor = this.currentFloorPlan.getFloor(this.currentFloorPlan.getCurrentFloor());
        this.floorService.setElement(floor, ObjectType.lookup(data), row, col);
    }

    public void initialize(double x, double y, int col, int row) {
        this.col = col;
        this.row = row;
        contextMenu.show(this.window, x, y);
    }

    public BorderPane getBorderPane() {
        return this.borderPane;
    }

    public void setCurrentFloorPlan(FloorPlan floorPlan) {
        this.currentFloorPlan = floorPlan;
    }

    public void setWindow(Window window) {
        this.window = window;
    }
}
