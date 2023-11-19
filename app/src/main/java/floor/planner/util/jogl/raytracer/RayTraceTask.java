package floor.planner.util.jogl.raytracer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import floor.planner.models.Camera;
import floor.planner.models.FloorPlan;
import javafx.concurrent.Task;

/**
 * The RayTraceTask defines a Runnable task to be executed in a background
 * thread (separate from the JavaFX application thread), to track progress of
 * generating a ray traced image from the current viewpoint. This must be done
 * in its own thread so the UI does not become unresponsive. Code based on
 * following stackoverflow response: https://stackoverflow.com/a/29628430.
 */
public class RayTraceTask extends Task<Void> {
    private static final Logger logger = LoggerFactory.getLogger(RayTraceTask.class);

    public float workDone = 0f;
    private FloorPlan floorPlan;
    private int height;
    private int width;
    private float max;

    public RayTraceTask(FloorPlan floorPlan) {
        this.floorPlan = floorPlan;
    }

    public RayTraceTask(int height, int width, int max) {
        this.height = height;
        this.width = width;
        this.max = max;
    }

    @Override
    public Void call() throws InterruptedException {
        try {
            RayTracer rayTracer = new RayTracer(height, width);
            rayTracer.render(this);
            return null;
        } catch (Exception e) {
            if (e instanceof InterruptedException) {
                throw e;
            }

            // we hit a snag, log the error and cancel the thread...
            logger.error(e.getMessage(), e);
            this.cancel(isRunning());
            return null;
        }
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
