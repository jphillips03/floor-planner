package floor.planner.listeners;

import floor.planner.constants.ObjectType;
import floor.planner.controllers.ElementController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class ObjectTypeChangeListener 
    extends ObjectChangeListener 
    implements ChangeListener<ObjectType>
{
    private ElementController elementController;

    public ObjectTypeChangeListener(ElementController eController) {
        super();
        this.elementController = eController;
    }

    @Override public void changed(
        ObservableValue<? extends ObjectType> selected,
        ObjectType oldType,
        ObjectType newType
    ) {
        if (!this.programmaticChange && oldType != null && !oldType.equals(newType)) {
            floorService.setElement(floor, newType, row, col);
            this.elementController.setElementColorDetails();
        }
    }
}
