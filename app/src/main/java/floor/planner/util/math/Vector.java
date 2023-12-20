package floor.planner.util.math;

import java.nio.DoubleBuffer;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public sealed class Vector permits Point3D, Color {
    private static final Logger logger = LoggerFactory.getLogger(Vector.class);

    private double[] values;

    public Vector() {}

    public Vector(double x, double y, double z) {
        this.values = new double[]{ x, y, z};
    }

    public Vector(double[] values) {
        this.values = values;        
    }

    public Vector(Vector v) {
        this.values = v.values;
    }

    public Vector(float[] vals) {
        this(vals[0], vals[1], vals[2]);
    }

    /**
     * Creates and returns a new Vector based on given start and end points. A
     * vector is found between 2 points by subtracting the start point from the
     * end point.
     *
     * @param start The starting point of the vector.
     * @param end The ending point of the vector.
     */
    public Vector(double[] start, double[] end) {
        Vector u = new Vector(start);
        Vector v = new Vector(end);
        this.values = Vector.subtract(v, u).values;
    }

    public DoubleBuffer getDoubleBuffer() {
        return DoubleBuffer.wrap(this.values);
    }

    public double[] getValues() {
        return this.values;
    }

    public float[] getFloatValues() {
        float[] res = new float[values.length];
        for (int i = 0; i < values.length; i++) {
            res[i] = (float) this.values[i];
        }
        return res;
    }

    public double getX() {
        return this.values[0];
    }

    public double getY() {
        return this.values[1];
    }

    public double getZ() {
        return this.values[2];
    }

    public void setValues(double[] vals) {
        this.values = vals;
    }

    public Vector add(double[] vals) {
        Vector res = copy(this);
        for (int i = 0; i < this.values.length; i++) {
            res.values[i] += vals[i];
        }

        return res;
    }

    public Vector add(Vector v) {
        return this.add(v.values);
    }

    public Vector subtract(double[] vals) {
        Vector res = copy(this);
        for (int i = 0; i < this.values.length; i++) {
            res.values[i] -= vals[i];
        }

        return res;
    }

    public Vector subtract(Vector v) {
        return this.subtract(v.values);
    }

    public Vector multiply(double x) {
        Vector res = copy(this);
        for (int i = 0; i < this.values.length; i++) {
            res.values[i] *= x;
        }

        return res;
    }

    public Vector multiply(Vector v) {
        Vector res = copy(this);
        for (int i = 0; i < this.values.length; i++) {
            res.values[i] *= v.values[i];
        }

        return res;
    }

    public Vector divide(double x) {
        return this.multiply(1 / x);
    }

    public Vector divide(Vector v) {
        Vector res = copy(this);
        for (int i = 0; i < this.values.length; i++) {
            res.values[i] /= v.values[i];
        }

        return res;
    }

    public double length() {
        return (double) Math.sqrt(this.lengthSqrd());
    }

    public double lengthSqrd() {
        double res = 0;
        for (int i = 0; i < this.values.length; i++) {
            res += this.values[i] * this.values[i];
        }

        return res;
    }

    public boolean nearZero() {
        double s = 1e-8;
        return Math.abs(this.getX()) < s &&
            Math.abs(this.getY()) < s &&
            Math.abs(this.getZ()) < s;
    }

    public String toString() {
        return String.format("[%.2f, %.2f, %.2f]", this.values[0], this.values[1], this.values[2]);
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
	public static double[] normal(double[] v1, double[] v2, double[] v3, double[] v4) {
		double[] normal = new double[3];
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
	private static double foil(double x1, double x2, double y1, double y2) {
		return (x1-x2)*(y1+y2);
	}
	
	/**
	 * Normalizes a vector to be a unit length vector.
	 * @param v The vector to normalize.
	 * @return A unit length vector.
	 */
	private static double[] normalize(double[] v) {
		for(int i = 0; i < v.length; i++) {
			if(v[i] != 0) v[i] /= v[i];
		}
		return v;
	}

    public static Vector normal(List<Vector> vectors) {
        Vector normal = new Vector(new double[]{
            normalUtil(vectors, 1, 2),
            normalUtil(vectors, 2, 0),
            normalUtil(vectors, 0, 1)
        });
        return normalize(normal);
    }
    
    private static double normalUtil(List<Vector> vectors, int i, int j) {
        return multiplyBinomials(
            new Point2D((float) vectors.get(0).values[i], (float) vectors.get(0).values[j]),
            new Point2D((float) vectors.get(1).values[i], (float) vectors.get(1).values[j])
        ) + multiplyBinomials(
            new Point2D((float) vectors.get(1).values[i], (float) vectors.get(1).values[j]),
            new Point2D((float) vectors.get(2).values[i], (float) vectors.get(2).values[j])
        ) + multiplyBinomials(
            new Point2D((float) vectors.get(2).values[i], (float) vectors.get(2).values[j]),
            new Point2D((float) vectors.get(3).values[i], (float) vectors.get(3).values[j])
        ) + multiplyBinomials(
            new Point2D((float) vectors.get(3).values[i], (float) vectors.get(3).values[j]),
            new Point2D((float) vectors.get(0).values[i], (float) vectors.get(0).values[j])
        );
    }

    private static double multiplyBinomials(Point2D b1, Point2D b2) {
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

    public static Vector add(Vector v1, Vector v2) {
        Vector v = new Vector(new double[v1.values.length]);
        for (int i = 0; i < v1.values.length; i++) {
            v.values[i] = v1.values[i] + v2.values[i];
        }
        return v;
    }

    public static Vector subtract(Vector v1, Vector v2) {
        Vector v = new Vector(new double[v1.values.length]);
        for (int i = 0; i < v1.values.length; i++) {
            v.values[i] = v1.values[i] - v2.values[i];
        }
        return v;
    }

    public static Vector multiply(Vector v1, Vector v2) {
        Vector v = new Vector(new double[v1.values.length]);
        for (int i = 0; i < v1.values.length; i++) {
            v.values[i] = v1.values[i] * v2.values[i];
        }
        return v;
    }

    public static double dot(Vector v1, Vector v2) {
        double res = 0;
        for (int i = 0; i < v1.values.length; i++) {
            res += v1.values[i] * v2.values[i];
        }
        return res;
    }

    /**
     * Returns the cross product of the given vectors. Assumes each vector is
     * a r-3 vector (which they should be anyway for purposes of this program).
     *
     * @param v1
     * @param v2
     * @return
     */
    public static Vector cross(Vector v1, Vector v2) {
        return new Vector(new double[]{
            v1.values[1] * v2.values[2] - v1.values[2] * v2.values[1],
            v1.values[2] * v2.values[0] - v1.values[0] * v2.values[2],
            v1.values[0] * v2.values[1] - v1.values[1] * v2.values[0]
        });
    }

    public static Vector unit(Vector v) {
        Vector res = copy(v);
        return res.divide(res.length());
    }

    public static Vector random() {
        return new Vector(new double[] {
            Random.randomDouble(),
            Random.randomDouble(),
            Random.randomDouble()
        });
    }

    public static Vector random(int min, int max) {
        return new Vector(new double[] {
            Random.randomDouble(min, max),
            Random.randomDouble(min, max),
            Random.randomDouble(min, max)
        });
    }

    public static Vector random(double min, double max) {
        return new Vector(new double[] {
            Random.randomDouble(min, max),
            Random.randomDouble(min, max),
            Random.randomDouble(min, max)
        });
    }

    public static Vector randomInUnitDisk() {
        // another while true...
        while (true) {
            Vector p = new Vector(Random.randomDouble(-1, 1), Random.randomDouble(-1, 1), 0);
            if (p.lengthSqrd() < 1) {
                return p;
            }
        }
    }

    public static Vector randomInUnitSphere() {
        // it feels really wrong to loop while true, but going off code from
        // https://raytracing.github.io/books/RayTracingInOneWeekend.html
        while (true) {
            Vector p = Vector.random(-1, 1);
            if (p.lengthSqrd() < 1f) {
                return p;
            }
        }
    }

    public static Vector randomOnHempisphere(Vector normal) {
        Vector onUnitSphere = randomUnitVector();
        if (dot(onUnitSphere, normal) > 0.0f) {
            // In the same hemisphere as the normal
            return onUnitSphere;
        } else {
            return onUnitSphere.multiply(-1);
        }
    }

    public static Vector randomUnitVector() {
        return unit(randomInUnitSphere());
    }

    public static Vector reflect(Vector v, Vector n) {
        return v.subtract(n.multiply(2 * dot(v, n)));
    }

    public static Vector refract(Vector v, Vector n, double etaiOverEtat) {
        double cosTheta = Math.min(dot(v.multiply(-1), n), 1);
        Vector rOutPerp = v.add(n.multiply(cosTheta)).multiply(etaiOverEtat);
        Vector rOutParallel = n.multiply(- Math.sqrt(Math.abs(1.0 - rOutPerp.lengthSqrd())));
        return rOutPerp.add(rOutParallel);
    }

    private static Vector copy(Vector v) {
        return new Vector(new double[]{ v.getValues()[0], v.getValues()[1], v.getValues()[2]});
    }

    public static Vector randomeCosineDirection() {
        double r1 = Random.randomDouble();
        double r2 = Random.randomDouble();
        double phi = 2 * MathUtil.PI * r1;
        double x = Math.cos(phi) * Math.sqrt(r2);
        double y = Math.sin(phi) * Math.sqrt(r2);
        double z = Math.sqrt(1 - r2);
        return new Vector(x, y, z);
    }
}
