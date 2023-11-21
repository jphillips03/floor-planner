package floor.planner.util.math;

public class MathUtil {
    public static double[] floatToDoubleArray(float[] a) {
        double[] res = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            res[i] = a[i];
        }
        return res;
    }
}
