package floor.planner.util.math;

public class MathUtil {
    /** A more precise PI (compared to Math.PI...). */
    public static final double PI = 3.1415926535897932385;

    public static double[] floatToDoubleArray(float[] a) {
        double[] res = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            res[i] = a[i];
        }
        return res;
    }
}
