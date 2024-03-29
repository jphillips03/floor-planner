package floor.planner.util.raytracer.intersectable;

import floor.planner.util.math.Interval;
import floor.planner.util.math.Point3D;
import floor.planner.util.math.Ray;
import floor.planner.util.math.Vector;
import floor.planner.util.raytracer.Aabb;

public abstract class Intersectable {
    public abstract boolean intersect(
        Ray r,
        Interval rayT,
        IntersectRecord rec
    );

    public abstract Aabb boundingBox();

    public double pdfValue(Point3D o, Vector v) {
        return 0;
    }

    public Vector random(Vector o) {
        return new Vector(1, 0, 0);
    }
}
