package floor.planner.listeners;

import floor.planner.models.FloorOption;
import floor.planner.models.FloorPlan;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class FloorChangeListener implements ChangeListener<FloorOption> {
    private FloorPlan currentFloorPlan;

    public void changed(
        ObservableValue<? extends FloorOption> selected,
        FloorOption oldFloor,
        FloorOption newFloor
    ) {
        if (newFloor != null && (oldFloor == null || !oldFloor.equals(newFloor))) {
            this.currentFloorPlan.setCurrentFloor(newFloor.getValue());
        }
    }

    public void setCurrentFloorPlan(FloorPlan cfp) {
        this.currentFloorPlan = cfp;
    }
}
