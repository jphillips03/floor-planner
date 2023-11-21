package floor.planner.util.jogl.material;

import floor.planner.util.jogl.objects.Color;
import floor.planner.util.jogl.raytracer.IntersectRecord;
import floor.planner.util.math.Ray;
import floor.planner.util.math.Vector;

public class Dielectric implements Material {
    /** Index of refraction. */
    private double ir;

    public Dielectric(double ir) {
        this.ir = ir;
    }

    public ScatterAttenuation scatter(Ray rIn, IntersectRecord rec) {
        Color attenuation = new Color(1f, 1f, 1f);
        double refractionRatio = rec.isFrontFace() ? (1 / this.ir) : this.ir;

        Vector unitDirection = Vector.unit(rIn.getDirection());
        Vector refracted = Vector.refract(unitDirection, rec.getNormal(), refractionRatio);

        Ray scattered = new Ray(rec.getP(), refracted);
        return new ScatterAttenuation(attenuation, scattered);
    }
}
