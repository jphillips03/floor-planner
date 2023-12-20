package floor.planner.util.raytracer.material;

import floor.planner.util.math.Color;
import floor.planner.util.math.Point3D;
import floor.planner.util.math.Ray;
import floor.planner.util.math.Vector;
import floor.planner.util.raytracer.IntersectRecord;
import floor.planner.util.raytracer.texture.SolidColor;
import floor.planner.util.raytracer.texture.Texture;

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

    public ScatterRecord scatter(Ray rIn, IntersectRecord rec) {
        ScatterRecord res = new ScatterRecord(albedo.value(rec.getU(), rec.getV(), new Point3D(rec.getP())));
        Vector reflected = Vector.reflect(Vector.unit(rIn.getDirection()), rec.getNormal());
        Ray scattered = new Ray(rec.getP(), reflected.add(Vector.randomInUnitSphere().multiply(fuzz)), rIn.getTime());
        res.pdf = null;
        res.skipPdf = true;
        res.skipPdfRay = scattered;
        return res;
        // if (Vector.dot(scattered.getDirection(), rec.getNormal()) > 0) {
        //     return res;
        // } else {
        //     return null;
        // }
        
        /*
         * srec.attenuation = albedo;
        srec.pdf_ptr = nullptr;
        srec.skip_pdf = true;
        vec3 reflected = reflect(unit_vector(r_in.direction()), rec.normal);
        srec.skip_pdf_ray =
            ray(rec.p, reflected + fuzz*random_in_unit_sphere(), r_in.time());
        return true;
         */
    }
}
