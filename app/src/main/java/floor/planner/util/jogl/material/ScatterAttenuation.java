package floor.planner.util.jogl.material;

import floor.planner.util.jogl.objects.Color;
import floor.planner.util.math.Ray;

public class ScatterAttenuation {
    private Color attenuation;
    private Ray scattered;

    public ScatterAttenuation(Color att, Ray scat) {
        this.attenuation = att;
        this.scattered = scat;
    }

    public Color getAttenuation() {
        return this.attenuation;
    }

    public Ray getScattered() {
        return this.scattered;
    }
}
