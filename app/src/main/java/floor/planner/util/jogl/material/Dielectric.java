package floor.planner.util.jogl.material;

import floor.planner.util.jogl.objects.Color;
import floor.planner.util.jogl.raytracer.IntersectRecord;
import floor.planner.util.math.Random;
import floor.planner.util.math.Ray;
import floor.planner.util.math.Vector;

public class Dielectric extends Material {
    /** Index of refraction. */
    private double ir;

    public Dielectric(double ir) {
        this.ir = ir;
    }

    public ScatterRecord scatter(Ray rIn, IntersectRecord rec) {
        Color attenuation = new Color(1f, 1f, 1f);
        double refractionRatio = rec.isFrontFace() ? (1.0 / this.ir) : this.ir;

        Vector unitDirection = Vector.unit(rIn.getDirection());
        double cosTheta = Math.min(Vector.dot(unitDirection.multiply(-1), rec.getNormal()), 1);
        double sinTheta = Math.sqrt(1 - cosTheta * cosTheta);
        boolean cannotRefract = refractionRatio * sinTheta > 1;
        Vector direction;

        if (cannotRefract || reflectance(cosTheta, refractionRatio) > Random.randomDouble()) {
            direction = Vector.reflect(unitDirection, rec.getNormal());
        } else {
            direction = Vector.refract(unitDirection, rec.getNormal(), refractionRatio);
        }

        Ray scattered = new Ray(rec.getP(), direction, rIn.getTime());
        return new ScatterRecord(
            attenuation,
            null,
            true,
            scattered
        );
        //return new ScatterAttenuation(attenuation, scattered);
    }

    /**
     * Use Schlick's approximation for reflectance
     *
     * @param cos
     * @param refIdx
     * @return
     */
    private static double reflectance(double cos, double refIdx) {
        double r0 = (1 - refIdx) / (1 + refIdx);
        r0 = r0 * r0;
        return r0 * (1 - r0) * Math.pow((1 - cos), 5);
    }
}
