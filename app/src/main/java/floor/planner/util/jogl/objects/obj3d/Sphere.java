package floor.planner.util.jogl.objects.obj3d;

import com.jogamp.opengl.GL2;

import floor.planner.util.math.Ray;
import floor.planner.util.math.Vector;

public class Sphere extends DrawableElement3D {
    private static float radius = 0.5f; // 0.0625f;

    private float x;
    private float y;
    private float z;
    private Vector center;
    
    public Sphere(float x, float y, float z) {
        this.x = x; // + 0.5f; // move to middle of tile where cylinder drawn
        this.y = y; // + 0.5f; // move to middle of tile where cylinder drawn
        this.z = z;

        center = new Vector(new float[]{ x, y, z });
    }

    public Vector getCenter() {
        return this.center;
    }

    public void draw(GL2 gl) {}

    public float intersect(Ray r) {
        Vector oc = Vector.subtract(r.getOrigin(), this.center);
        float a = Vector.dot(r.getDirection(), r.getDirection());
        float b = 2f * Vector.dot(oc, r.getDirection());
        float c = Vector.dot(oc, oc) - radius * radius;
        float discriminant = b * b - 4 * a * c;

        if (discriminant < 0) {
            return -1f;
        } else {
            return (float)((-b - Math.sqrt((double) discriminant)) / (2f * a));
        }
    }
}
