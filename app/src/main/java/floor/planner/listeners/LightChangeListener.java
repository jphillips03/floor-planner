package floor.planner.listeners;

import floor.planner.constants.LightType;
import floor.planner.models.Light;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class LightChangeListener implements ChangeListener<Number> {
    private int colorIndex;
    private Light light;
    private LightType lightType;
    private boolean programmaticChange;

    public void setLight(Light l) {
        this.light = l;
    }

    public void setProgrammaticChange(boolean pc) {
        this.programmaticChange = pc;
    }

    public LightChangeListener(int ci, LightType lt) {
        this.colorIndex = ci;
        this.lightType = lt;
    }

    @Override public void changed(
        ObservableValue<? extends Number> selected,
        Number oldVal,
        Number newVal
    ) {
        if (!this.programmaticChange && oldVal != null && !oldVal.equals(newVal)) {
            switch(this.lightType) {
                case AMBIENT:
                    float[] a = this.light.getAmbient();
                    a[this.colorIndex] = newVal.floatValue();
                    this.light.setAmbient(a);
                    break;
                case DIFFUSE:
                    float[] d = this.light.getDiffuse();
                    d[this.colorIndex] = newVal.floatValue();
                    break;
                default:
                    float[] s = this.light.getSpecular();
                    s[this.colorIndex] = newVal.floatValue();
                    break;
            }
        }
    }
}
