package floor.planner.util.jogl.material;

import floor.planner.util.jogl.objects.Color;
import floor.planner.util.jogl.raytracer.IntersectRecord;
import floor.planner.util.jogl.raytracer.Onb;
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
        Onb uvw = new Onb();
        uvw.buildFromW(rec.getNormal());
        Vector scatterDirection = uvw.local(Vector.randomeCosineDirection());
        Ray scattered = new Ray(rec.getP(), Vector.unit(scatterDirection), rIn.getTime());
        double pdf = Vector.dot(uvw.w(), scattered.getDirection()) / Math.PI;
        return new ScatterAttenuation(albedo.value(rec.getU(), rec.getV(), new Point3D(rec.getP())), scattered, pdf);
    }

    public double scatteringPdf(Ray rIn, IntersectRecord rec, Ray scattered) {
        return 1 / (2 * Math.PI);
    }
}
