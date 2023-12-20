package floor.planner.util.raytracer.pdf;

import floor.planner.util.math.Vector;

public abstract class Pdf {
    public abstract double value(Vector direction);
    public abstract Vector generate();
}
