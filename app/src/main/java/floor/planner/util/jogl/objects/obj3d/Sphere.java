package floor.planner.util.jogl.objects.obj3d;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.opengl.GL2;

import floor.planner.util.jogl.material.Material;
import floor.planner.util.jogl.raytracer.Aabb;
import floor.planner.util.jogl.raytracer.IntersectRecord;
import floor.planner.util.math.Interval;
import floor.planner.util.math.MathUtil;
import floor.planner.util.math.Point3D;
import floor.planner.util.math.Ray;
import floor.planner.util.math.Vector;

public class Sphere extends DrawableElement3D {
    private static final Logger logger = LoggerFactory.getLogger(Sphere.class);
    //private static double radius = 0.5f; // 0.0625f;

    // private double x;
    // private double y;
    // private double z;
    private double radius;
    private Vector center;
    private Material mat;

    public Sphere(Vector pos, double radius, Material mat) {
        this.radius = radius;
        this.center = pos;
        this.mat = mat;

        Vector rvec = new Vector(radius, radius, radius);
        this.boundingBox = new Aabb(
            new Point3D(Vector.subtract(pos, rvec)),
            new Point3D(Vector.add(pos, rvec))
        );
    }

    public Sphere(double x, double y, double z, double radius, Material mat) {
        // this.x = x; // + 0.5f; // move to middle of tile
        // this.y = y; // + 0.5f; // move to middle of tile
        // this.z = z;
        this.radius = radius;
        this.center = new Vector(new double[]{ x, y, z });
        this.mat = mat;

        Vector rvec = new Vector(radius, radius, radius);
        this.boundingBox = new Aabb(
            new Point3D(Vector.subtract(this.center, rvec)),
            new Point3D(Vector.add(this.center, rvec))
        );
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
        double a = r.getDirection().lengthSqrd();
        double halfB = Vector.dot(oc, r.getDirection());
        double c = oc.lengthSqrd() - radius * radius;
        double discriminant = halfB * halfB - a * c;

        if (discriminant < 0) {
            return false;
        }

        // find nearest root that lies in acceptable range
        double sqrtD = (double) Math.sqrt((double) discriminant);
        double root = (-halfB - sqrtD) / a;
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
        rec.setMaterial(this.mat);
        return true;
    }

    public Point3D getMidPoint() {
        return new Point3D(this.center);
    }
}
