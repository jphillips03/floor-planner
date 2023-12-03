package floor.planner.util.jogl.raytracer;

import floor.planner.util.jogl.material.Material;
import floor.planner.util.jogl.material.ScatterAttenuation;
import floor.planner.util.jogl.objects.Color;
import floor.planner.util.jogl.raytracer.texture.SolidColor;
import floor.planner.util.jogl.raytracer.texture.Texture;
import floor.planner.util.math.Point3D;
import floor.planner.util.math.Ray;
import floor.planner.util.math.Vector;

public class Isotropic extends Material {
    private Texture albedo;
    
    public Isotropic(Color c) {
        this(new SolidColor(c));
    }

    public Isotropic(Texture t) {
        this.albedo = t;
    }

    public ScatterAttenuation scatter(Ray rIn, IntersectRecord rec) {
        Ray scattered = new Ray(rec.getP(), Vector.randomUnitVector(), rIn.getTime());
        return new ScatterAttenuation(this.albedo.value(rec.getU(), rec.getV(), new Point3D(rec.getP())), scattered, 1 / (4 * Math.PI));
    }

    public double scatteringPdf(Ray rIn, IntersectRecord rec, Ray scattered) {
        return 1 / (4 * Math.PI);
    }
}
