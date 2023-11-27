package floor.planner.util.jogl.raytracer.texture;

import floor.planner.util.jogl.objects.Color;
import floor.planner.util.math.Point3D;

public interface Texture {
    public Color value(double u, double v, Point3D p);
}
