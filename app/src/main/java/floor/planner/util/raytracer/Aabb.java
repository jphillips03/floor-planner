package floor.planner.util.raytracer;

import floor.planner.util.math.Interval;
import floor.planner.util.math.Point3D;
import floor.planner.util.math.Ray;

/**
 * The Aabb class represents an axis-aligned bounding box. This is used in the
 * ray tracer to create bounding volumes around objects in the scene to quicky
 * determine if a ray may hit the objects bound by the volume. If a ray misses
 * a bounding volume, then it obviously misses any objects inside it. If a ray
 * hits a bounding volume then it might hit one of the objects inside.
 */
public class Aabb {
    private Interval x;
    private Interval y;
    private Interval z;

    public Aabb() {
        this.x = new Interval(0, 0);
        this.y = new Interval(0, 0);
        this.z = new Interval(0, 0);
    }

    public Aabb(Aabb box0, Aabb box1) {
        this.x = new Interval(box0.x, box1.x);
        this.y = new Interval(box0.y, box1.y);
        this.z = new Interval(box0.z, box1.z);
    }

    public Aabb(Interval x, Interval y, Interval z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Creates a bounding box based on given points. The points are treated as
     * extrema for the bounding box, so we don't require a particular min/max
     * coordinate order.
     *
     * @param a One point to use for the bounding box.
     * @param b Another point to use for the bounding box.
     */
    public Aabb(Point3D a, Point3D b) {
        this.x = new Interval(Math.min(a.getX(), b.getX()), Math.max(a.getX(), b.getX()));
        this.y = new Interval(Math.min(a.getY(), b.getY()), Math.max(a.getY(), b.getY()));
        this.z = new Interval(Math.min(a.getZ(), b.getZ()), Math.max(a.getZ(), b.getZ()));
    }

    public Interval axis(int n) {
        if (n == 1) {
            return this.y;
        } else if (n == 2) {
            return this.z;
        } else {
            return this.x;
        }
    }

    public boolean hit(Ray r, Interval rayT) {
        for (int a = 0; a < 3; a++) {
            double invD = 1 / r.getDirection().getValues()[a];
            double orig = r.getOrigin().getValues()[a];

            double t0 = (this.axis(a).getMin() - orig) * invD;
            double t1 = (this.axis(a).getMax() - orig) * invD;

            if (invD < 0) {
                // swap t0 and t1...
                double temp = t0;
                t0 = t1;
                t1 = temp;
            }

            if (t0 > rayT.getMin()) rayT.setMin(t0);
            if (t1 < rayT.getMax()) rayT.setMax(t1);

            if (rayT.getMax() <= rayT.getMin()) {
                return false;
            }
        }
        return true;
    }

    public boolean lessThan(Aabb boxB, int axis) {
        return this.axis(axis).getMin() < boxB.axis(axis).getMin();
    }

    public boolean greaterThan(Aabb boxB, int axis) {
        return this.axis(axis).getMin() > boxB.axis(axis).getMin();
    }

    public Aabb pad() {
        double delta = 0.0001;
        Interval newX = x.size() >= delta ? x : x.expand(delta);
        Interval newY = y.size() >= delta ? y : y.expand(delta);
        Interval newZ = z.size() >= delta ? z : z.expand(delta);
        return new Aabb(newX, newY, newZ);
    }

    public String toString() {
        return String.format("%s %s %s", this.x.toString(), this.y.toString(), this.z.toString());
    }
}
