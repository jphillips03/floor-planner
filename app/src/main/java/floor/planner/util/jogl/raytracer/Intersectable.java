package floor.planner.util.jogl.raytracer;

import floor.planner.util.math.Interval;
import floor.planner.util.math.Ray;

public interface Intersectable {
    public abstract boolean intersect(
        Ray r,
        Interval rayT,
        IntersectRecord rec
    );

    public abstract Aabb boundingBox();
}
