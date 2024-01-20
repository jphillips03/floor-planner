package floor.planner.models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class CurrentFloorPlan {

    private ObjectProperty<Floor> floor;
    private ObjectProperty<FloorPlan> floorPlan;

    public CurrentFloorPlan() {
        this.floor = new SimpleObjectProperty<Floor>();
        this.floorPlan = new SimpleObjectProperty<FloorPlan>();
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
}
