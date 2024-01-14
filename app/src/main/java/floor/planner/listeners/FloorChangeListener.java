package floor.planner.listeners;

import floor.planner.controllers.ElementController;
import floor.planner.models.FloorOption;
import floor.planner.models.FloorPlan;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class FloorChangeListener implements ChangeListener<FloorOption> {
    private ElementController elementController;
    private FloorPlan currentFloorPlan;

    public FloorChangeListener(ElementController eController) {
        this.elementController = eController;
    }

    public void changed(
        ObservableValue<? extends FloorOption> selected,
        FloorOption oldFloor,
        FloorOption newFloor
    ) {
        if (oldFloor == null || !oldFloor.equals(newFloor)) {
            this.currentFloorPlan.setCurrentFloor(newFloor.getValue());
            this.elementController.setFloor(this.currentFloorPlan.getFloor(newFloor.getValue()));
        }
    }

    public void setCurrentFloorPlan(FloorPlan cfp) {
        this.currentFloorPlan = cfp;
    }
}
