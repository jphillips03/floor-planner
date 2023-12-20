package floor.planner.util.raytracer.intersectable;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import floor.planner.util.math.Interval;
import floor.planner.util.math.Point3D;
import floor.planner.util.math.Random;
import floor.planner.util.math.Ray;
import floor.planner.util.math.Vector;
import floor.planner.util.raytracer.Aabb;

public class IntersectableList extends Intersectable {
    private static final Logger logger = LoggerFactory.getLogger(IntersectableList.class);

    private Aabb boundingBox;
    private List<Intersectable> elements;

    public IntersectableList() {
        this.elements = new ArrayList<Intersectable>();
        this.boundingBox = new Aabb();
    }

    public IntersectableList(Intersectable element) {
        this.elements = new ArrayList<Intersectable>();
        this.boundingBox = new Aabb();
        this.add(element);
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

    public void add(Intersectable element) {
        this.elements.add(element);
        this.boundingBox = new Aabb(boundingBox, element.boundingBox());
    }

    public void addAll(IntersectableList elements) {
        List<Intersectable> intersectableElements = elements.getElements();
        this.elements.addAll(intersectableElements);
        for (Intersectable element : intersectableElements) {
            this.boundingBox = new Aabb(boundingBox, element.boundingBox());
        }
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

    @Override
    public double pdfValue(Point3D o, Vector v) {
        double weight = 1.0 / this.elements.size();
        double sum = 0;
        for (Intersectable object : this.elements) {
            sum += weight * object.pdfValue(o, v);
        }

        return sum;
    }

    @Override
    public Vector random(Vector o) {
        return this.elements.get(Random.randomInt(0, elements.size() - 1)).random(o);
    }
}
