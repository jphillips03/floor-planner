package floor.planner.util.jogl.material;

import floor.planner.util.jogl.objects.Color;
import floor.planner.util.jogl.raytracer.IntersectRecord;
import floor.planner.util.math.Ray;
import floor.planner.util.math.Vector;

public class Lambertian implements Material {
    private Color albedo;

    public Lambertian(Color a) {
        this.albedo = a;
    }

    public Color getAlbedo() {
        return this.albedo;
    }

    public ScatterAttenuation scatter(Ray rIn, IntersectRecord rec) {
        Vector scatterDirection = Vector.add(rec.getNormal(), Vector.randomUnitVector());

        // catch degenerate scatter direction
        if (scatterDirection.nearZero()) {
            scatterDirection = rec.getNormal();
        }

        Ray scattered = new Ray(rec.getP(), scatterDirection);
        return new ScatterAttenuation(albedo, scattered);
    }
}
