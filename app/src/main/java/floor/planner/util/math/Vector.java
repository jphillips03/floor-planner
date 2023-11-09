package floor.planner.util.math;

import java.nio.FloatBuffer;
import java.util.List;

public class Vector {
    private float[] values;

    public Vector() {}

    public Vector(float[] values) {
        this.values = values;
    }

    public FloatBuffer getFloatBuffer() {
        return FloatBuffer.wrap(this.values);
    }

    public float[] getValues() {
        return this.values;
    }

    public void setValues(float[] vals) {
        this.values = vals;
    }

    public static Vector normal(List<Vector> vectors) {
        Vector normal = new Vector(new float[]{
            normalUtil(vectors, 1, 2),
            normalUtil(vectors, 2, 0),
            normalUtil(vectors, 0, 1)
        });
        return normalize(normal);
    }
    
    private static float normalUtil(List<Vector> vectors, int i, int j) {
        return multiplyBinomials(
            new Point2D(vectors.get(0).values[i], vectors.get(0).values[j]),
            new Point2D(vectors.get(1).values[i], vectors.get(1).values[j])
        ) + multiplyBinomials(
            new Point2D(vectors.get(1).values[i], vectors.get(1).values[j]),
            new Point2D(vectors.get(2).values[i], vectors.get(2).values[j])
        ) + multiplyBinomials(
            new Point2D(vectors.get(2).values[i], vectors.get(2).values[j]),
            new Point2D(vectors.get(3).values[i], vectors.get(3).values[j])
        ) + multiplyBinomials(
            new Point2D(vectors.get(3).values[i], vectors.get(3).values[j]),
            new Point2D(vectors.get(0).values[i], vectors.get(0).values[j])
        );
    }

    private static float multiplyBinomials(Point2D b1, Point2D b2) {
        return (b1.getX() - b2.getX()) * (b1.getY() + b2.getY());
    }

    private static Vector normalize(Vector v) {
        for (int i = 0; i < v.values.length; i++) {
            if (v.values[i] != 0) {
                v.values[i] /= v.values[i];
            }
        }

        return v;
    }
}
