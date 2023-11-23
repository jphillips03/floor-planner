package floor.planner.util.jogl.raytracer;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import floor.planner.util.math.Interval;
import floor.planner.util.math.Ray;

public class IntersectableList implements Intersectable {
    private static final Logger logger = LoggerFactory.getLogger(IntersectableList.class);

    private Aabb boundingBox;
    private List<Intersectable> elements;
    private RayTraceTask task;

    public IntersectableList() {
        this.elements = new ArrayList<Intersectable>();
        this.boundingBox = new Aabb();
    }

    public IntersectableList(Intersectable element) {
        this.elements = new ArrayList<Intersectable>();
        this.elements.add(element);
        this.boundingBox = new Aabb();
    }

    public IntersectableList(List<Intersectable> elements, Aabb boundingBox) {
        this.elements = elements;
        this.boundingBox = boundingBox;
    }

    public Aabb getBoundingBox() {
        return this.boundingBox;
    }

    public List<Intersectable> getElements() {
        return this.elements;
    }

    public void setTask(RayTraceTask task) {
        this.task = task;
    }

    public void add(Intersectable element) {
        this.elements.add(element);
        this.boundingBox = new Aabb(boundingBox, element.boundingBox());
    }

    public IntersectRecord intersect(
        Ray r,
        Interval rayT
    ) {
        IntersectRecord rec = new IntersectRecord();
        boolean hitAnything = false;
        double closestSoFar = rayT.getMax();

        for (Intersectable element : this.elements) {
            if (element.intersect(r, new Interval(rayT.getMin(), closestSoFar), rec)) {
                hitAnything = true;
                closestSoFar = rec.getT();
            }
        }

        return hitAnything ? rec : null;
    }

    @Override
    public boolean intersect(
        Ray r,
        Interval rayT,
        IntersectRecord rec
    ) {
        return false;
    }

    @Override
    public Aabb boundingBox() {
        return this.boundingBox;
    }
}
