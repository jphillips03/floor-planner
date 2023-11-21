package floor.planner.util.jogl.material;

import floor.planner.util.jogl.objects.Color;
import floor.planner.util.jogl.raytracer.IntersectRecord;
import floor.planner.util.math.Ray;
import floor.planner.util.math.Vector;

public class Metal implements Material {
    private Color albedo;

    public Metal(Color a) {
        this.albedo = a;
    }

    public Color getAlbedo() {
        return this.albedo;
    }

    public ScatterAttenuation scatter(Ray rIn, IntersectRecord rec) {
        Vector reflected = Vector.reflect(Vector.unit(rIn.getDirection()), rec.getNormal());
        Ray scattered = new Ray(rec.getP(), reflected);
        return new ScatterAttenuation(albedo, scattered);
    }
}
