package floor.planner.util.jogl.material;

import floor.planner.util.jogl.objects.Color;
import floor.planner.util.jogl.raytracer.IntersectRecord;
import floor.planner.util.jogl.raytracer.texture.SolidColor;
import floor.planner.util.jogl.raytracer.texture.Texture;
import floor.planner.util.math.Point3D;
import floor.planner.util.math.Ray;
import floor.planner.util.math.Vector;

public class Metal extends Material {
    private Texture albedo;

    /**
     * A value between 0 and 1 that determines fuzziness of the metal (0 not
     * fuzzy, 1 the most fuzzy).
     */
    private double fuzz;

    public Metal(Color c, double fuzz) {
        this.albedo = new SolidColor(c);
        this.fuzz = fuzz < 1 ? fuzz : 1;
    }

    public Texture getAlbedo() {
        return this.albedo;
    }

    public ScatterAttenuation scatter(Ray rIn, IntersectRecord rec) {
        Vector reflected = Vector.reflect(Vector.unit(rIn.getDirection()), rec.getNormal());
        Ray scattered = new Ray(rec.getP(), Vector.add(reflected, Vector.randomUnitVector().multiply(fuzz)));
        if (Vector.dot(scattered.getDirection(), rec.getNormal()) > 0) {
            return new ScatterAttenuation(albedo.value(rec.getU(), rec.getV(), new Point3D(rec.getP())), scattered);
        } else {
            return null;
        }
    }
}
