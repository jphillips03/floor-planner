package floor.planner.util.jogl.objects.obj2d;

public class ClippingPlane {
    private float left;
    private float right;
    private float bottom;
    private float top;

    public ClippingPlane(float left, float right, float bottom, float top) {
        this.left = left;
        this.right = right;
        this.bottom = bottom;
        this.top = top;
    }

    public float getLeft() {
        return this.left;
    }

    public float getRight() {
        return this.right;
    }

    public float getBottom() {
        return this.bottom;
    }

    public float getTop() {
        return this.top;
    }

    public float getWidth() {
        return this.right - this.left;
    }

    public float getHeight() {
        return this.top - this.bottom;
    }
}
