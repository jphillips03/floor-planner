package floor.planner.util.raytracer.material;

import floor.planner.util.math.Ray;
import floor.planner.util.raytracer.intersectable.IntersectRecord;

public class SimpleMaterial extends Material {
    public ScatterRecord scatter(Ray rIn, IntersectRecord rec) {
        return null;
    }
}
