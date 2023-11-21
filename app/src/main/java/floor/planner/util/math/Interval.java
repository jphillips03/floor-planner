package floor.planner.util.math;

public class Interval {
    public final static Interval EMPTY = new Interval();
    public final static Interval UNIVERSE = new Interval(
        Double.NEGATIVE_INFINITY,
        Double.POSITIVE_INFINITY
    );

    private double min;
    private double max;

    // Default interval is empty
    public Interval() {
        this.min = Double.POSITIVE_INFINITY;
        this.max = Double.NEGATIVE_INFINITY;
    }

    public Interval(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public double getMin() {
        return this.min;
    }

    public double getMax() {
        return this.max;
    }

    public double clamp(double x) {
        if (x < this.min) return this.min;
        if (x > this.max) return this.max;
        return x;
    }

    public boolean contains(double x) {
        return this.min <= x && x <= this.max;
    }

    public boolean surrounds(double x) {
        return this.min < x && x < this.max;
    }
}
