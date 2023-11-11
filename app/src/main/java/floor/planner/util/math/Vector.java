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

    /**
	 * Computes the normal vector to a surface represented by 3 arrays. Arrays
	 * representing the surface are converted to 2 perpendicular arrays. Then 
	 * the cross product of those arrays can be computed.
	 * @param v1 The first array representing the surface.
	 * @param v2 The second array representing the surface.
	 * @param v3 The third array representing the surface.
	 * @return The normal to the surface.
	 */
	public static float[] normal(float[] v1, float[] v2, float[] v3, float[] v4) {
		float[] normal = new float[3];
		normal[0] = foil(v1[1],v2[1],v1[2],v2[2]) + foil(v2[1],v3[1],v2[2],v3[2]) +
			foil(v3[1],v4[1],v3[2],v4[2]) + foil(v4[1],v1[1],v4[2],v1[2]);
		normal[1] = foil(v1[2],v2[2],v1[0],v2[0]) + foil(v2[2],v3[2],v2[0],v3[0]) +
			foil(v3[2],v4[2],v3[0],v4[0]) + foil(v4[2],v1[2],v4[0],v1[0]);
		normal[2] = foil(v1[0],v2[0],v1[1],v2[1]) + foil(v2[0],v3[0],v2[1],v3[1]) +
			foil(v3[0],v4[0],v3[1],v4[1]) + foil(v4[0],v1[0],v4[1],v1[1]);
		return normalize(normal);
	}

    /**
	 * 
	 */
	private static float foil(float x1, float x2, float y1, float y2) {
		return (x1-x2)*(y1+y2);
	}
	
	/**
	 * Normalizes a vector to be a unit length vector.
	 * @param v The vector to normalize.
	 * @return A unit length vector.
	 */
	private static float[] normalize(float[] v) {
		for(int i = 0; i < v.length; i++) {
			if(v[i] != 0) v[i] /= v[i];
		}
		return v;
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
