package floor.planner.listeners;

import floor.planner.constants.ObjectType;
import floor.planner.controllers.ElementController;
import floor.planner.models.Floor;
import floor.planner.services.FloorService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class ObjectTypeChangeListener implements ChangeListener<ObjectType> {
    private ElementController elementController;
    private FloorService floorService;
    private boolean programmaticChange;
    private Floor floor;
    private int row;
    private int col;

    public ObjectTypeChangeListener(ElementController eController) {
        this.elementController = eController;
        this.floorService = new FloorService();
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setProgrammaticChange(boolean val) {
        this.programmaticChange = val;
    }

    public ObjectTypeChangeListener() {
        super();
        this.floorService = new FloorService();
    }

    @Override public void changed(ObservableValue<? extends ObjectType> selected, ObjectType oldType, ObjectType newType) {
        if (!this.programmaticChange && oldType != null && !oldType.equals(newType)) {
            floorService.setElement(floor, newType, row, col);
            this.elementController.setElementColorDetails();
        }
    }
}
