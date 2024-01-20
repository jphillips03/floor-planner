package floor.planner.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class CurrentFloorPlan {

    private ObjectProperty<Floor> floor;
    private ObjectProperty<FloorPlan> floorPlan;
    private IntegerProperty column;
    private IntegerProperty row;

    public CurrentFloorPlan() {
        this.floor = new SimpleObjectProperty<Floor>();
        this.floorPlan = new SimpleObjectProperty<FloorPlan>();
        this.column = new SimpleIntegerProperty(-1);
        this.row = new SimpleIntegerProperty(-1);
    }

    public final Floor getFloor() {
        return this.floor.get();
    }
    public final void setFloor(Floor f) {
        this.floor.set(f);
    }
    public ObjectProperty<Floor> floorProperty() {
        return this.floor;
    }

    public final FloorPlan getFloorPlan() {
        return this.floorPlan.get();
    }
    public final void setFloorPlan(FloorPlan fp) {
        this.floorPlan.set(fp);
    }
    public ObjectProperty<FloorPlan> floorPlanProperty() {
        return this.floorPlan;
    }

    public final int getColumn() {
        return this.column.get();
    }
    public final void setColumn(int col) {
        this.column.set(col);
    }
    public IntegerProperty columnProperty() {
        return this.column;
    }

    public final int getRow() {
        return this.row.get();
    }
    public final void setRow(int row) {
        this.row.set(row);
    }
    public IntegerProperty rowProperty() {
        return this.row;
    }
}
