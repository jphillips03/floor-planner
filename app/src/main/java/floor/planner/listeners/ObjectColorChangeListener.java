package floor.planner.listeners;

import floor.planner.util.math.Color;
import floor.planner.util.objects.obj2d.DrawableElement2D;
import floor.planner.util.objects.obj3d.DrawableElement3D;
import floor.planner.util.objects.obj3d.FloorTile;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class ObjectColorChangeListener
    extends ObjectChangeListener
    implements ChangeListener<Number>
{
    private int colorIndex;
    private boolean isSpecular;

    public ObjectColorChangeListener(int colorIndex) {
        super();
        this.colorIndex = colorIndex;
        this.isSpecular = false;
    }

    public ObjectColorChangeListener(int colorIndex, boolean isSpecular) {
        super();
        this.colorIndex = colorIndex;
        this.isSpecular = true;
    }

    @Override public void changed(
        ObservableValue<? extends Number> selected,
        Number oldType,
        Number newType
    ) {
        if (!this.programmaticChange && oldType != null && !oldType.equals(newType)) {
            if (this.isSpecular) {
                this.setSpecular(newType);
            } else {
                this.setAmbientDiffuse(newType);
            }
        }
    }

    private void setAmbientDiffuse(Number newType) {
        DrawableElement2D[] elements = this.floor.getElements()[this.row][this.col];
        for (int i = 0; i < elements.length; i++) {
            DrawableElement2D element = elements[i];
            Color color = element.getColor();
            switch(this.colorIndex) {
                case 0:
                    color.setRed(newType.floatValue());
                    break;
                case 1:
                    color.setGreen(newType.floatValue());
                    break;
                default:
                    color.setBlue(newType.floatValue());
                    break;
            }
            element.setColor(color);
        }
    }

    private void setSpecular(Number newType) {
        DrawableElement3D[] elements = this.floor.getElements3D()[this.row][this.col];
        for (int i = 0; i < elements.length; i++) {
            DrawableElement3D element = elements[i];
            if (!(element instanceof FloorTile)) {
                float[] color = element.getSpecular();
                color[this.colorIndex] = newType.floatValue();
                element.setSpecular(color);
            }
        }
    }
}
