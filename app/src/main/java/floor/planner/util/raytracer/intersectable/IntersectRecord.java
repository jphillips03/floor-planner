package floor.planner.util.raytracer.intersectable;

import floor.planner.util.math.Ray;
import floor.planner.util.math.Vector;
import floor.planner.util.raytracer.material.Material;

public class IntersectRecord {
    private Vector p;
    private Vector normal;
    private double t;
    private double u; 
    private double v;

    public boolean frontFace;
    public Material mat;
    
    public IntersectRecord() {}

    public IntersectRecord(Vector p, Vector normal, double t) {
        this.p = p;
        this.normal = normal;
        this.t = t;
    }

    public boolean isFrontFace() {
        return this.frontFace;
    }

    public Vector getP() {
        return this.p;
    }
    public void setP(Vector p) {
        this.p = p;
    }

    public Vector getNormal() {
        return this.normal;
    }
    public void setNormal(Vector n) {
        this.normal = n;
    }

    public double getT() {
        return this.t;
    }
    public void setT(double t) {
        this.t = t;
    }

    public double getU() {
        return this.u;
    }
    public void setU(double u) {
        this.u = u;
    }

    public double getV() {
        return this.v;
    }
    public void setV(double v) {
        this.v = v;
    }

    public void setFaceNormal(Ray r, Vector outwardNormal) {
        // Sets the hit record normal vector.
        // NOTE: the parameter `outwardNormal` is assumed to have unit length.

        this.frontFace = Vector.dot(r.getDirection(), outwardNormal) < 0;
        this.normal = this.frontFace ? outwardNormal : outwardNormal.multiply(-1);
    }

    public Material getMaterial() {
        return this.mat;
    }
    public void setMaterial(Material mat) {
        this.mat = mat;
    }
}
