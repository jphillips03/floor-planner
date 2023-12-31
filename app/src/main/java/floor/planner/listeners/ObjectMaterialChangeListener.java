package floor.planner.listeners;

import floor.planner.constants.MaterialType;
import floor.planner.util.objects.obj3d.DrawableElement3D;
import floor.planner.util.objects.obj3d.FloorTile;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class ObjectMaterialChangeListener
    extends ObjectChangeListener
    implements ChangeListener<MaterialType>
{
    @Override public void changed(
        ObservableValue<? extends MaterialType> selected,
        MaterialType oldType,
        MaterialType newType
    ) {
        if (!this.programmaticChange && oldType != null && !oldType.equals(newType)) {
            DrawableElement3D[] elements = this.floor.getElements3D()[this.row][this.col];
            for (DrawableElement3D element : elements) {
                if (!(element instanceof FloorTile)) {
                    element.setMaterialType(newType);
                }
            }
        }
    }
}
