package floor.planner.util.jogl.material;

import floor.planner.util.jogl.objects.Color;
import floor.planner.util.jogl.raytracer.IntersectRecord;
import floor.planner.util.math.Point3D;
import floor.planner.util.math.Ray;

public abstract class Material {
    public abstract ScatterRecord scatter(Ray rIn, IntersectRecord rec);

    /**
     * Color emitted from the material. This allows materials to act as lights.
     * If a material is not a light, then it emits nothing by default (i.e it
     * emits black color).
     *
     * @param u
     * @param v
     * @param p
     * @return
     */
    public Color emitted(double u, double v, Point3D p) {
        // default to black...
        return new Color(0, 0, 0);
    }

    public Color emitted(Ray rIn, IntersectRecord rec, double u, double v, Point3D p) {
        return new Color(0, 0, 0);
    }

    public double scatteringPdf(Ray rIn, IntersectRecord rec, Ray scattered) {
        return 0;
    }
}
