package floor.planner.util.raytracer.material;

import floor.planner.util.math.MathUtil;
import floor.planner.util.math.Point3D;
import floor.planner.util.math.Ray;
import floor.planner.util.math.Vector;
import floor.planner.util.objects.Color;
import floor.planner.util.raytracer.IntersectRecord;
import floor.planner.util.raytracer.Onb;
import floor.planner.util.raytracer.pdf.CosPdf;
import floor.planner.util.raytracer.texture.SolidColor;
import floor.planner.util.raytracer.texture.Texture;

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

    public ScatterRecord scatter(Ray rIn, IntersectRecord rec) {
        // calculate scattered ray for scenes without lighting... 
        Onb uvw = new Onb();
        uvw.buildFromW(rec.getNormal());
        Vector scatterDirection = uvw.local(Vector.randomeCosineDirection());
        Ray scattered = new Ray(rec.getP(), Vector.unit(scatterDirection), rIn.getTime());

        return new ScatterRecord(
            albedo.value(rec.getU(), rec.getV(), new Point3D(rec.getP())),
            new CosPdf(rec.getNormal()),
            scattered
        );
    }

    public double scatteringPdf(Ray rIn, IntersectRecord rec, Ray scattered) {
        double cosTheta = Vector.dot(rec.getNormal(), Vector.unit(scattered.getDirection()));
        return cosTheta < 0 ? 0 : cosTheta / MathUtil.PI;
    }
}
