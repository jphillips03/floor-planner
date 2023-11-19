package floor.planner.util.math;

public class Interval {
    public final static Interval EMPTY = new Interval();
    public final static Interval UNIVERSE = new Interval(
        Float.NEGATIVE_INFINITY,
        Float.POSITIVE_INFINITY
    );

    private float min;
    private float max;

    // Default interval is empty
    public Interval() {
        this.min = Float.POSITIVE_INFINITY;
        this.max = Float.NEGATIVE_INFINITY;
    }

    public Interval(float min, float max) {
        this.min = min;
        this.max = max;
    }

    public float getMin() {
        return this.min;
    }

    public float getMax() {
        return this.max;
    }

    public float clamp(float x) {
        if (x < this.min) return this.min;
        if (x > this.max) return this.max;
        return x;
    }

    public boolean contains(float x) {
        return this.min <= x && x <= this.max;
    }

    public boolean surrounds(float x) {
        return this.min < x && x < this.max;
    }
}
