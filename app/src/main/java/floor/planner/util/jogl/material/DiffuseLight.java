package floor.planner.util.jogl.material;

import floor.planner.util.jogl.objects.Color;
import floor.planner.util.jogl.raytracer.IntersectRecord;
import floor.planner.util.jogl.raytracer.texture.SolidColor;
import floor.planner.util.jogl.raytracer.texture.Texture;
import floor.planner.util.math.Point3D;
import floor.planner.util.math.Ray;

public class DiffuseLight extends Material {
    private Texture emit;

    public DiffuseLight(Texture t) {
        this.emit = t;
    }

    public DiffuseLight(Color c) {
        this(new SolidColor(c));
    }

    public ScatterAttenuation scatter(Ray rIn, IntersectRecord rec) {
        return null;
    }

    public Color emitted(double u, double v, Point3D p) {
        return emit.value(u, v, p);
    }
}
