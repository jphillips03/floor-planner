package floor.planner.util.jogl.raytracer;

import javafx.concurrent.Task;

/**
 * The RayTraceTask defines a Runnable task to be executed in a background
 * thread (separate from the JavaFX application thread), to track progress of
 * generating a ray traced image from the current viewpoint. This must be done
 * in its own thread so the UI does not become unresponsive. Code based on
 * following stackoverflow response: https://stackoverflow.com/a/29628430.
 */
public class RayTraceTask extends Task<Void> {
    public float workDone = 0f;
    private int height;
    private int width;
    private float max;

    public RayTraceTask(int height, int width, int max) {
        this.height = height;
        this.width = width;
        this.max = max;
    }

    @Override
    public Void call() throws InterruptedException {
        RayTracer rayTracer = new RayTracer(height, width);
        rayTracer.render(this);
        return null;
    }

    /**
     * Exposes the updateProgress function so we can call it from outside the
     * class.
     *
     * @param workDone
     * @param max
     */
    public void updateProgress(float workDone) {
        updateProgress((double) workDone, (double) this.max);
    }
}
