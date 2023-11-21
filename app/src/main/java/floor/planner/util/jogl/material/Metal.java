package floor.planner.util.jogl.material;

import floor.planner.util.jogl.objects.Color;
import floor.planner.util.jogl.raytracer.IntersectRecord;
import floor.planner.util.math.Ray;
import floor.planner.util.math.Vector;

public class Metal implements Material {
    private Color albedo;
    /**
     * A value between 0 and 1 that determines fuzziness of the metal (0 not
     * fuzzy, 1 the most fuzzy).
     */
    private double fuzz;

    public Metal(Color a, double fuzz) {
        this.albedo = a;
        this.fuzz = fuzz < 1 ? fuzz : 1;
    }

    public Color getAlbedo() {
        return this.albedo;
    }

    public ScatterAttenuation scatter(Ray rIn, IntersectRecord rec) {
        Vector reflected = Vector.reflect(Vector.unit(rIn.getDirection()), rec.getNormal());
        Ray scattered = new Ray(rec.getP(), Vector.add(reflected, Vector.randomUnitVector().multiply(fuzz)));
        if (Vector.dot(scattered.getDirection(), rec.getNormal()) > 0) {
            return new ScatterAttenuation(albedo, scattered);
        } else {
            return null;
        }
    }
}
