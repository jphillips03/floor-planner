package floor.planner.util.jogl.objects.obj3d;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.opengl.GL2;

import floor.planner.util.jogl.raytracer.IntersectRecord;
import floor.planner.util.math.Interval;
import floor.planner.util.math.Ray;
import floor.planner.util.math.Vector;

public class Sphere extends DrawableElement3D {
    private static final Logger logger = LoggerFactory.getLogger(Sphere.class);
    //private static float radius = 0.5f; // 0.0625f;

    // private float x;
    // private float y;
    // private float z;
    private float radius;
    private Vector center;

    public Sphere(float x, float y, float z, float radius) {
        // this.x = x; // + 0.5f; // move to middle of tile
        // this.y = y; // + 0.5f; // move to middle of tile
        // this.z = z;
        this.radius = radius;
        center = new Vector(new float[]{ x, y, z });
    }

    public Vector getCenter() {
        return this.center;
    }

    @Override
    public void draw(GL2 gl) {}

    @Override
    public boolean intersect(
        Ray r,
        Interval rayT,
        IntersectRecord rec
    ) {
        Vector oc = Vector.subtract(r.getOrigin(), this.center);
        float a = r.getDirection().lengthSqrd();
        float halfB = Vector.dot(oc, r.getDirection());
        float c = oc.lengthSqrd() - radius * radius;
        float discriminant = halfB * halfB - a * c;

        if (discriminant < 0) {
            return false;
        }

        // find nearest root that lies in acceptable range
        float sqrtD = (float) Math.sqrt((double) discriminant);
        float root = (-halfB - sqrtD) / a;
        if (!rayT.surrounds(root)) {
            root = (-halfB + sqrtD) / a;
            if (!rayT.surrounds(root)) {
                return false;
            }
        }

        rec.setT(root);
        rec.setP(r.at(rec.getT()));
        Vector outwardNormal = Vector.subtract(rec.getP(), this.center).divide(radius);
        rec.setFaceNormal(r, outwardNormal);
        return true;
    }
}
