package floor.planner.util.raytracer.intersectable;

public class IntersectableWorld {
    private IntersectableList elements;
    private IntersectableList lights;

    public IntersectableWorld(IntersectableList elements) {
        this.elements = elements;
    }

    public IntersectableWorld(IntersectableList elements, IntersectableList lights) {
        this.elements = elements;
        this.lights = lights;
    }

    public IntersectableList getElements() {
        return this.elements;
    }

    public IntersectableList getLights() {
        return this.lights;
    }
}
