package floor.planner.util.jogl.material;

import floor.planner.util.jogl.raytracer.IntersectRecord;
import floor.planner.util.math.Ray;

public interface Material {
    public ScatterAttenuation scatter(Ray rIn, IntersectRecord rec);
}
