package floor.planner.util.math;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Utility class for generating random number values. We use ThreadLocalRandom
 * since the ray tracer runs in it's own thread. Otherwise Math.random() does
 * not produce sufficiently random values needed.
 */
public class Random {

    /**
     * Returns a random real in [0, 1).
     *
     * @return A random real in [0, 1).
     */
    public static double randomDouble() {
        return ThreadLocalRandom.current().nextDouble();
    }

    /**
     * Returns a random real in [min, max).
     *
     * @param min The least value that can be returned.
     * @param max The upper bound (exclusive) for the returned value.
     * @return A random real in [min, max).
     */
    public static double randomDouble(int min, int max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    /**
     * Returns a random real in [min, max).
     *
     * @param min The least value that can be returned.
     * @param max The upper bound (exclusive) for the returned value.
     * @return A random real in [min, max).
     */
    public static double randomDouble(double min, double max) {
        return ThreadLocalRandom.current().nextDouble();
    }

    public static int randomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }
}
