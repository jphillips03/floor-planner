package floor.planner.util.jogl.objects.obj3d;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import floor.planner.util.jogl.material.Material;
import floor.planner.util.jogl.raytracer.Aabb;
import floor.planner.util.jogl.raytracer.IntersectRecord;
import floor.planner.util.jogl.raytracer.Intersectable;
import floor.planner.util.math.Interval;
import floor.planner.util.math.MathUtil;
import floor.planner.util.math.Point3D;
import floor.planner.util.math.Random;
import floor.planner.util.math.Ray;
import floor.planner.util.math.Vector;

public class Quad extends Intersectable {
    private static final Logger logger = LoggerFactory.getLogger(Quad.class);
    private Aabb boundingBox;
    private Point3D Q;
    private Vector u;
    private Vector v;
    private Material mat;
    private Vector normal;
    private double D;
    private Vector w;
    private double area;

    public Quad(Point3D q, Vector u, Vector v, Material mat) {
        this.Q = q;
        this.u = u;
        this.v = v;
        this.mat = mat;

        this.init();
    }

    public Quad(float[] originf, float[] uf, float[] vf, Material mat) {
        this(
            new Point3D(originf[0], originf[1], originf[2]),
            new Vector(
                MathUtil.floatToDoubleArray(originf),
                MathUtil.floatToDoubleArray(uf)
            ),
            new Vector(
                MathUtil.floatToDoubleArray(originf),
                MathUtil.floatToDoubleArray(vf)
            ),
            mat
        );
    }

    private void init() {
        Vector n = Vector.cross(u, v);
        this.normal = Vector.unit(n);
        this.D = Vector.dot(this.normal, Q.getVector());
        this.w = n.divide(Vector.dot(n, n));
        this.area = n.length();
        this.setBoundingBox();
    }

    private void setBoundingBox() {
        this.boundingBox = new Aabb(
            Q,
            new Point3D(Q.getVector().add(u).add(v))
        ).pad();
    }

    public Vector getNormal() {
        return this.normal;
    }
    public void setNormal(Vector n) {
        this.normal = n;
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
        Vector planar = intersection.subtract(Q.getVector());
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

    @Override
    public double pdfValue(Point3D origin, Vector v) {
        IntersectRecord rec = new IntersectRecord();
        if (!this.intersect(new Ray(origin.getVector(), v), new Interval(0.001, Double.POSITIVE_INFINITY), rec)) {
            return 0;
        }

        double distanceSquared = rec.getT() * rec.getT() * v.lengthSqrd();
        double cos = Math.abs(Vector.dot(v, rec.getNormal()) / v.length());
        return distanceSquared / (cos * this.area);
    }

    public Vector random(Point3D origin) {
        return this.random(origin.getVector());
    }

    public Vector random(Vector origin) {
        Vector p = this.Q.getVector().add(this.u.multiply(Random.randomDouble())).add(
            this.v.multiply(Random.randomDouble())
        );
        return p.subtract(origin);
    }

    public Point3D getMidPoint() {
        Point3D diagPoint = new Point3D(
            this.Q.getVector().add(this.u).add(
                this.v
            )
        );

        return new Point3D(
            (this.Q.getX() + diagPoint.getX()) / 2,
            (this.Q.getY() + diagPoint.getY()) / 2,
            (this.Q.getZ() + diagPoint.getZ()) / 2
        );
    }
}
