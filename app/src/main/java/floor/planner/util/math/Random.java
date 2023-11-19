package floor.planner.util.math;

public class Random {
    /**
     * Default max random value which should be next value down from 1 since
     * Math.random() returns a number in [0, 1)
     */
    private static float RAND_MAX = (float) Math.nextDown(1.0);

    public static float randomFloat() {
        return (float) Math.random() / (RAND_MAX + 1);
    }

    public static float randomFloat(int min, int max) {
        // returns a random real in [min, max)
        return (float) (min + ((max - min) * Math.random()));
    }
}
