package floor.planner.util.jogl.raytracer;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import floor.planner.util.jogl.objects.obj3d.DrawableElement3D;
import floor.planner.util.math.Interval;
import floor.planner.util.math.Ray;

public class IntersectableList {
    private static final Logger logger = LoggerFactory.getLogger(IntersectableList.class);

    private List<DrawableElement3D> elements;
    private RayTraceTask task;

    public IntersectableList() {
        this.elements = new ArrayList<DrawableElement3D>();
    }

    public List<DrawableElement3D> getElements() {
        return this.elements;
    }

    public void setTask(RayTraceTask task) {
        this.task = task;
    }

    public void add(DrawableElement3D element) {
        this.elements.add(element);
    }

    public IntersectRecord intersect(
        Ray r,
        Interval rayT
    ) {
        IntersectRecord rec = new IntersectRecord();
        boolean hitAnything = false;
        float closestSoFar = rayT.getMax();

        for (DrawableElement3D element : this.elements) {
            if (element.intersect(r, new Interval(rayT.getMin(), closestSoFar), rec)) {
                hitAnything = true;
                closestSoFar = rec.getT();
            }
            task.updateProgress(task.workDone++);
        }

        return hitAnything ? rec : null;
    }
}
