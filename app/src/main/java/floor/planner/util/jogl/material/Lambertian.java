package floor.planner.util.jogl.material;

import floor.planner.util.jogl.objects.Color;
import floor.planner.util.jogl.raytracer.IntersectRecord;
import floor.planner.util.jogl.raytracer.texture.SolidColor;
import floor.planner.util.jogl.raytracer.texture.Texture;
import floor.planner.util.math.Point3D;
import floor.planner.util.math.Ray;
import floor.planner.util.math.Vector;

public class Lambertian extends Material {
    private Texture albedo;

    public Lambertian(Texture t) {
        this.albedo = t;
    }

    public Lambertian(Color c) {
        this.albedo = new SolidColor(c);
    }

    public Texture getAlbedo() {
        return this.albedo;
    }

    public ScatterAttenuation scatter(Ray rIn, IntersectRecord rec) {
        Vector scatterDirection = Vector.add(rec.getNormal(), Vector.randomUnitVector());

        // catch degenerate scatter direction
        if (scatterDirection.nearZero()) {
            scatterDirection = rec.getNormal();
        }

        Ray scattered = new Ray(rec.getP(), scatterDirection);
        return new ScatterAttenuation(albedo.value(rec.getU(), rec.getV(), new Point3D(rec.getP())), scattered);
    }
}
