package floor.planner.listeners;

import floor.planner.constants.ObjectType;
import floor.planner.models.Floor;
import floor.planner.util.math.Color;
import floor.planner.util.objects.obj2d.DrawableElement2D;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class ObjectColorChangeListener implements ChangeListener<Number> {
    private boolean programmaticChange;
    private Floor floor;
    private int row;
    private int col;
    private int colorIndex;

    public ObjectColorChangeListener(int colorIndex) {
        this.colorIndex = colorIndex;
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

    @Override public void changed(ObservableValue<? extends Number> selected, Number oldType, Number newType) {
        if (!this.programmaticChange && oldType != null && !oldType.equals(newType)) {
            DrawableElement2D[] elements = this.floor.getElements()[this.row][this.col];
            for (int i = 0; i < elements.length; i++) {
                Color color = this.floor.getElements()[this.row][this.col][i].getColor();
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
            }
        }
    }
}
