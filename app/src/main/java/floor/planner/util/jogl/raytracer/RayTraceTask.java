package floor.planner.util.jogl.raytracer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import floor.planner.constants.RayTraceTaskType;
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

    public double workDone = 0;
    private FloorPlan floorPlan;
    private int height;
    private int width;
    private int maxDepth;
    private double max;
    private RayTraceTaskType type;

    public RayTraceTask(int height, int width, int max, int maxDepth) {
        this.height = height;
        this.width = width;
        this.max = max;
        this.maxDepth = maxDepth;
    }

    public RayTraceTask(FloorPlan floorPlan, int height, int width, int max, int maxDepth) {
        this(height, width, max, maxDepth);
        this.floorPlan = floorPlan;
    }

    public RayTraceTask(int height, int width, int max, int maxDepth, RayTraceTaskType type) {
        this(height, width, max, maxDepth);
        this.type = type;
    }

    @Override
    public Void call() throws InterruptedException {
        try {
            logger.info("Initializing ray tracer");
            RayTracer rayTracer;
            if (this.floorPlan != null) {
                rayTracer = new RayTracer(this.floorPlan, this.height, this.width, this.maxDepth);
            } else {
                rayTracer = new RayTracer(this.height, this.width, this.maxDepth, this.type);
            }

            logger.info("Ray tracer initialized successfully");
            logger.info("Ray trace starting...");
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
    public void updateProgress(double workDone) {
        updateProgress((double) workDone, (double) this.max);
    }
}
