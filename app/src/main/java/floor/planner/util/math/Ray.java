package floor.planner.util.math;

/**
 * A Ray is defined as a function P(t) = A + tb. P is a 3D position along a
 * line in 3D. A is the origin and b is the ray direction. The ray parameter t
 * is a real number used to determine the position along the ray.
 */
public class Ray {
    /** The direction of the line in 3D. */
    private Vector direction;
    /** The origin of the line. */
    private Vector origin;
    private double time;

    public Ray(Vector origin, Vector direction) {
        this.origin = origin;
        this.direction = direction;
    }

    public Ray(Vector origin, Vector direction, double time) {
        this.origin = origin;
        this.direction = direction;
        this.time = time;
    }

    /**
     * Returns the direction of the ray.
     *
     * @return The direction of the ray.
     */
    public Vector getDirection() {
        return this.direction;
    }

    /**
     * Returns the origin of the ray.
     *
     * @return The origin of the ray.
     */
    public Vector getOrigin() {
        return this.origin;
    }

    public double getTime() {
        return this.time;
    }

    /**
     * Returns the position along this ray at given t value. Negative t values
     * return positions behind the origin. Positive t values return positions
     * in front of origin.
     *
     * @param t Value to use to calculate point on ray.
     * @return Vector at position along ray at given t value.
     */
    public Vector at(double t) {
        return this.origin.add(this.direction.multiply(t));
    }
}
