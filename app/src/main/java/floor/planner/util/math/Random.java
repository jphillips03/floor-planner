package floor.planner.util.math;

public class Random {
    /**
     * Default max random value which should be next value down from 1 since
     * Math.random() returns a number in [0, 1)
     */
    private static double RAND_MAX = (double) Math.nextDown(1.0);

    public static double randomDouble() {
        return (double) Math.random() / (RAND_MAX + 1);
    }

    public static double randomDouble(int min, int max) {
        // returns a random real in [min, max)
        return (double) (min + ((max - min) * Math.random()));
    }

    public static double randomDouble(double min, double max) {
        // returns a random real in [min, max)
        return (double) (min + ((max - min) * Math.random()));
    }
}
