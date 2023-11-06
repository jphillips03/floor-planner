package floor.planner.constants;

/**
 * The AspectRatio enum defines the default aspect ratio to use when rendering
 * objects in OpenGL. Aspect ratio defined as 16:9 converted to 1.78:1 (since
 * 16/9 = 1.777). Ratio values are multiplied by 2.75 to ensure there is padding
 * around floor plan (*2 seems too small, but *2.75 seems ok).
 */
public enum AspectRatio {
    X(1.78f),
    Y(1f);
    
    public float value;

    /**
     * Sets the aspect ratio constant value with the given value. The value is
     * multiplied by 2.75 to ensure padding around floor plan.
     *
     * @param value Value to set for constant.
     */
    private AspectRatio(float value) {
        this.value = value * 2.75f;
    }
}
