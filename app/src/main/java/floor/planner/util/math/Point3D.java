package floor.planner.util.math;

public final class Point3D extends Vector {

    public Point3D(double x, double y, double z) {
        super(x, y, z);
    }

    public Point3D(Vector v) {
        super(v);
    }

    public String toString() {
        return "(" + this.getX() + ", " + this.getY() + ", " + this.getZ() + ")";
    }

    public static double distanceBetween(Point3D p1, Point3D p2) {
        return Math.sqrt(
            Math.pow((p2.getX() - p1.getX()), 2) +
            Math.pow((p2.getY() - p1.getY()), 2) +
            Math.pow((p2.getZ() - p1.getZ()), 2)
        );
    }
}
