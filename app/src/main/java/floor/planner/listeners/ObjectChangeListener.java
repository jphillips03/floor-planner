package floor.planner.listeners;

import floor.planner.models.Floor;
import floor.planner.services.FloorService;

public class ObjectChangeListener {
    protected FloorService floorService;
    protected boolean programmaticChange;
    protected Floor floor;
    protected int row;
    protected int col;

    public ObjectChangeListener() {
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
}
