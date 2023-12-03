package floor.planner.util.jogl.material;

import floor.planner.util.jogl.objects.Color;
import floor.planner.util.math.Ray;

public class ScatterAttenuation {
    private Color attenuation;
    private Ray scattered;
    private double pdf;

    public ScatterAttenuation(Color att, Ray scat) {
        this.attenuation = att;
        this.scattered = scat;
    }

    public ScatterAttenuation(Color att, Ray scat, double pdf) {
        this.attenuation = att;
        this.scattered = scat;
        this.pdf = pdf;
    }

    public Color getAttenuation() {
        return this.attenuation;
    }

    public Ray getScattered() {
        return this.scattered;
    }

    public double getPdf() {
        return this.pdf;
    }
}
