package floor.planner.util.jogl.raytracer.pdf;

import floor.planner.util.jogl.raytracer.IntersectableList;
import floor.planner.util.math.Point3D;
import floor.planner.util.math.Vector;

public class IntersectablePdf extends Pdf {
    private IntersectableList objects;
    private Point3D origin;

    public IntersectablePdf(IntersectableList objects, Point3D origin) {
        this.objects = objects;
        this.origin = origin;
    }

    @Override
    public double value(Vector direction) {
        return objects.pdfValue(origin, direction);
    }

    @Override
    public Vector generate() {
        return objects.random(this.origin.getVector());
    }
}
