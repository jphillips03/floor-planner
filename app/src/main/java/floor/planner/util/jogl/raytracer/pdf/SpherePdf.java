package floor.planner.util.jogl.raytracer.pdf;

import floor.planner.util.math.MathUtil;
import floor.planner.util.math.Vector;

public class SpherePdf extends Pdf {
    public double value(Vector direction) {
        return 1 / (4 * MathUtil.PI);
    }
    public Vector generate() {
        return Vector.randomUnitVector();
    }
}
