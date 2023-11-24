package floor.planner.util.math;

public class Point3D extends Point2D {
    private float z;
    private Vector v;

    public Point3D(float x, float y, float z) {
        super(x, y);
        this.z = z;
        this.v = new Vector(x, y, z);
    }

    public Point3D(Vector v) {
        super((float) v.getX(), (float) v.getY());
        this.z = (float) v.getZ();
        this.v = v;
    }

    public float getZ() {
        return this.z;
    }
    public void setZ(float z) {
        this.z = z;
    }

    public Vector getVector() {
        return this.v;
    }

    public String toString() {
        return "(" + this.getX() + ", " + this.getY() + ", " + this.z + ")";
    }

    public static double distanceBetween(Point3D p1, Point3D p2) {
        return Math.sqrt(
            Math.pow((p2.getX() - p1.getX()), 2) +
            Math.pow((p2.getY() - p1.getY()), 2) +
            Math.pow((p2.getZ() - p1.getZ()), 2)
        );
    }
}
