package floor.planner.listeners;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Slider;

/**
 * Listener for updating styles on slider to ensure fill color changes. Based
 * on stackoverflow response below here https://stackoverflow.com/a/74266529.
 */
public class SliderColorListener implements ChangeListener<Number> {
    private Slider slider;

    public SliderColorListener(Slider slider) {
        this.slider = slider;
    }

    @Override public void changed(
        ObservableValue<? extends Number> selected,
        Number oldVal,
        Number newVal
    ) {
        double percentage = 100.0 * newVal.doubleValue() / slider.getMax();
        String style = String.format(
                // in the String format, 
                // %1$.1f%% gives the first format argument ("1$"),
                // i.e. percentage, formatted to 1 decimal place (".1f").
                // Note literal % signs must be escaped ("%%")
                "-track-color: linear-gradient(to right, " +
                        "-fx-accent 0%%, " +
                        "-fx-accent %1$.1f%%, " +
                        "-default-track-color %1$.1f%%, " +
                        "-default-track-color 100%%);",
                percentage);
        slider.setStyle(style);
    }
}
