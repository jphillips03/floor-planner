package floor.planner.util.raytracer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import floor.planner.util.math.Interval;
import floor.planner.util.math.Random;
import floor.planner.util.math.Ray;

public class BvhNode extends Intersectable {
    private Intersectable left;
    private Intersectable right;
    private Aabb boundingBox;
    private int axis;

    public BvhNode(List<Intersectable> objects) {
        this(objects, 0, objects.size());
    }

    public BvhNode(List<Intersectable> srcObjects, int start, int end) {
        // create a modifiable array of source objects
        List<Intersectable> objects = new ArrayList<Intersectable>();
        // add null src objects to list to prevent IndexOutOfBoundsException in
        // Collections.copy (which apparently needs elements in list to work i.e.
        // it does work on list capacity alone)... 
        for (int i = 0; i < srcObjects.size(); i++) {
            objects.add(null);
        }
        Collections.copy(objects, srcObjects);

        axis = Random.randomInt(0, 2);
        int objectSpan = end - start;
        if (objectSpan == 1) {
            this.left = this.right = objects.get(start);
        } else if (objectSpan == 2) {
            if (axis == 0) {
                if (boxXCompare(objects.get(start), objects.get(start + 1))) {
                    this.left = objects.get(start);
                    this.right = objects.get(start + 1);
                } else {
                    this.left = objects.get(start + 1);
                    this.right = objects.get(start);
                }
            } else if (axis == 1) {
                if (boxYCompare(objects.get(start), objects.get(start + 1))) {
                    this.left = objects.get(start);
                    this.right = objects.get(start + 1);
                } else {
                    this.left = objects.get(start + 1);
                    this.right = objects.get(start);
                }
            } else {
                if (boxZCompare(objects.get(start), objects.get(start + 1))) {
                    this.left = objects.get(start);
                    this.right = objects.get(start + 1);
                } else {
                    this.left = objects.get(start + 1);
                    this.right = objects.get(start);
                }
            }
        } else {
            objects.sort(new BoundingBoxComparator());

            int mid = objectSpan / 2;
            this.left = new BvhNode(objects.subList(0, mid));
            this.right = new BvhNode(objects.subList(mid, objectSpan));
        }

        this.boundingBox = new Aabb(left.boundingBox(), right.boundingBox());
    }

    public Aabb boundingBox() {
        return this.boundingBox;
    }

    public boolean intersect(Ray r, Interval rayT, IntersectRecord rec) {
        if (!boundingBox.hit(r, rayT)) {
            return false;
        }

        boolean hitLeft = left.intersect(r, rayT, rec);
        boolean hitRight = right.intersect(r, new Interval(rayT.getMin(), hitLeft ? rec.getT() : rayT.getMax()), rec);
        return hitLeft || hitRight;
    }

    private static Boolean boxCompare(Intersectable a, Intersectable b, int axisIndex) {
        return a.boundingBox().axis(axisIndex).getMin() < b.boundingBox().axis(axisIndex).getMin();
    }

    private static Boolean boxXCompare(Intersectable a, Intersectable b) {
        return boxCompare(a, b, 0);
    }

    private static Boolean boxYCompare(Intersectable a, Intersectable b) {
        return boxCompare(a, b, 1);
    }

    private static Boolean boxZCompare(Intersectable a, Intersectable b) {
        return boxCompare(a, b, 2);
    }

    private class BoundingBoxComparator implements Comparator<Intersectable> {
        @Override
        public int compare(Intersectable a, Intersectable b) {
            Aabb boxA = a.boundingBox();
            Aabb boxB = b.boundingBox();
            if (boxA.lessThan(boxB, axis)) {
                return 1;
            } else if (boxB.greaterThan(boxB, axis)) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}
