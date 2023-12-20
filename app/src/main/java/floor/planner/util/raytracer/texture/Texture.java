package floor.planner.util.raytracer.texture;

import floor.planner.util.math.Point3D;
import floor.planner.util.objects.Color;

public interface Texture {
    public Color value(double u, double v, Point3D p);
}
