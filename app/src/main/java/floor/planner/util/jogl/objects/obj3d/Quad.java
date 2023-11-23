package floor.planner.util.jogl.objects.obj3d;

import floor.planner.util.jogl.material.Material;
import floor.planner.util.jogl.raytracer.Aabb;
import floor.planner.util.jogl.raytracer.IntersectRecord;
import floor.planner.util.jogl.raytracer.Intersectable;
import floor.planner.util.math.Interval;
import floor.planner.util.math.Point3D;
import floor.planner.util.math.Ray;
import floor.planner.util.math.Vector;

public class Quad implements Intersectable {
    private Aabb boundingBox;
    private Point3D Q;
    private Vector u;
    private Vector v;
    private Material mat;
    private Vector normal;
    private double D;
    private Vector w;

    public Quad(Point3D q, Vector u, Vector v, Material mat) {
        this.Q = q;
        this.u = u;
        this.v = v;
        this.mat = mat;

        Vector n = Vector.cross(u, v);
        this.normal = Vector.unit(n);
        this.D = Vector.dot(this.normal, Q.getVector());
        this.w = n.divide(Vector.dot(n, n));
        this.setBoundingBox();
    }

    private void setBoundingBox() {
        this.boundingBox = new Aabb(
            Q,
            new Point3D(
                Vector.add(
                    Vector.add(Q.getVector(), u),
                    v
                )
            )
        ).pad();
    }

    @Override
    public boolean intersect(
        Ray r,
        Interval rayT,
        IntersectRecord rec
    ) {
        double denom = Vector.dot(this.normal, r.getDirection());
        
        // no hit if ray is parallel to plane
        if (Math.abs(denom) < 1e-8) {
            return false;
        }

        // return false if hit pointer parameter t is outside ray interval
        double t = (D - Vector.dot(this.normal, r.getOrigin())) / denom;
        if (!rayT.contains(t)) {
            return false;
        }

        // determine hit point lies within planar shape using plane coordinates
        Vector intersection = r.at(t);
        Vector planar = Vector.subtract(intersection, Q.getVector());
        double alpha = Vector.dot(this.w, Vector.cross(planar, v));
        double beta = Vector.dot(this.w, Vector.cross(u, planar));

        if (!isInterior(alpha, beta, rec)) {
            return false;
        }

        // ray hits 2D shape; set rest of hit record and return true
        rec.setT(t);
        rec.setP(intersection);
        rec.setMaterial(mat);
        rec.setFaceNormal(r, this.normal);
        return true;
    }

    // Given the hit point in plane coordinates, return false if it is outside the
        // primitive, otherwise set the hit record UV coordinates and return true.
    private boolean isInterior(double a, double b, IntersectRecord rec) {
        if (a < 0 || 1 < a || b < 0 || 1 < b) {
            return false;
        }

        rec.setU(a);
        rec.setV(b);
        return true;
    }

    @Override
    public Aabb boundingBox() {
        return this.boundingBox;
    }
}
