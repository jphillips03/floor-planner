package floor.planner.util.math;

public class Point3D extends Point2D {
    private float z;

    public Point3D(float x, float y, float z) {
        super(x, y);
        this.z = z;
    }

    public Point3D(Vector v) {
        super((float) v.getX(), (float) v.getY());
        this.z = (float) v.getZ();
    }

    public float getZ() {
        return this.z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public String toString() {
        return "(" + this.getX() + ", " + this.getY() + ", " + this.z + ")";
    }
}
