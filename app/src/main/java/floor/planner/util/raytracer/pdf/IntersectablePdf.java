package floor.planner.util.raytracer.pdf;

import floor.planner.util.math.Point3D;
import floor.planner.util.math.Vector;
import floor.planner.util.raytracer.IntersectableList;

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
