package floor.planner.util.jogl.raytracer.pdf;

import floor.planner.util.jogl.raytracer.Onb;
import floor.planner.util.math.MathUtil;
import floor.planner.util.math.Vector;

public class CosPdf extends Pdf{
    private Onb uvw;

    public CosPdf(Vector w) {
        this.uvw = new Onb();
        this.uvw.buildFromW(w);
    }

    public double value(Vector direction) {
        double cosTheta = Vector.dot(Vector.unit(direction), uvw.w());
        return Math.max(0, cosTheta / MathUtil.PI);
    }

    public Vector generate() {
        return uvw.local(Vector.randomeCosineDirection());
    }
}
