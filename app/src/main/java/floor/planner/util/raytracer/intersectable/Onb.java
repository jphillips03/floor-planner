package floor.planner.util.raytracer.intersectable;

import floor.planner.util.math.Vector;

public class Onb {
    private Vector[] axis;
    
    public Onb() {
        this.axis = new Vector[3];
    }

    public Vector operator(int i) {
        return this.axis[i];
    }

    public Vector u() {
        return this.axis[0];
    }

    public Vector v() {
        return this.axis[1];
    }

    public Vector w() {
        return this.axis[2];
    }

    public Vector local(double a, double b, double c) {
        return this.u().multiply(a).add(
            this.v().multiply(b)
        ).add(
            this.w().multiply(c)
        );
    }

    public Vector local(Vector a) {
        return this.local(a.getX(), a.getY(), a.getZ());
    }

    public void buildFromW(Vector w) {
        Vector unitW = Vector.unit(w);
        Vector a = Math.abs(unitW.getX()) > 0.9 ? new Vector(0, 1, 0) : new Vector(1, 0, 0);
        Vector v = Vector.unit(Vector.cross(unitW, a));
        Vector u = Vector.cross(unitW, v);
        this.axis[0] = u;
        this.axis[1] = v;
        this.axis[2] = unitW;
    }
}
