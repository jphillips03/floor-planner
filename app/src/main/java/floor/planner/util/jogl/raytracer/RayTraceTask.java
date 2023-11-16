package floor.planner.util.jogl.raytracer;

import java.io.File;

import floor.planner.util.FileUtil;
import floor.planner.util.jogl.objects.Color;
import javafx.concurrent.Task;

/**
 * The RayTraceTask defines a Runnable task to be executed in a background
 * thread (separate from the JavaFX application thread), to track progress of
 * generating a ray traced image from the current viewpoint. This must be done
 * in its own thread so the UI does not become unresponsive. Code based on
 * following stackoverflow response: https://stackoverflow.com/a/29628430.
 */
public class RayTraceTask extends Task<Void> {
    /** The amount to change progress by when it is incremented. */
    private double delta;
    private int height;
    private int width;

    public RayTraceTask(double delta, int height, int width) {
        this.delta = delta;
        this.height = height;
        this.width = width;
    }

    @Override
    public Void call() throws InterruptedException {
        double progress = 0.0;

        // generate a matrix of colors for the image; currently just a
        // placeholder for the actual ray trace algorithm...
        Color[][] image = new Color[this.height][this.width];
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                image[i][j] = new Color(j / 255f, i / 255f, 0);
                updateProgress(progress += delta, 101f);
                Thread.sleep(1);
            }
        }

        // store the image as a PPM file
        String imageStr = PPMImageFormatter.write(image);
        File file = new File("image.ppm");
        FileUtil.save(file, imageStr);
        updateProgress(101f, 101f);
        return null;
    }
}
