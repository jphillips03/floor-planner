package floor.planner.constants;

/**
 * The AspectRatio enum defines the default aspect ratio to use when rendering
 * objects in OpenGL. Currently the aspect ratio is 16:9, but the ratio is
 * divided by 4 so there isn't too much extra padding added around objects.
 */
public enum AspectRatio {
    X(4f),
    Y(2.25f);
    
    public float value;

    private AspectRatio(float value) {
        this.value = value;
    }
}
