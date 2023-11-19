package floor.planner.util.jogl.raytracer;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import floor.planner.models.Camera;
import floor.planner.models.FloorPlan;
import floor.planner.util.FileUtil;
import floor.planner.util.jogl.objects.Color;
import floor.planner.util.jogl.objects.obj3d.Sphere;
import floor.planner.util.math.Random;
import floor.planner.util.math.Ray;
import floor.planner.util.math.Vector;

public class RayTracer {
    private static final Logger logger = LoggerFactory.getLogger(RayTracer.class);

    private int samplesPerPixel = 100; // Count of random samples for each pixel

    // image
    private int imageWidth;
    private int imageHeight;

    // camera
    private Camera camera;
    private float focalLength = 1f;
    private float viewportHeight = 2f;
    private float viewportWidth;
    private Vector cameraCenter = new Vector(new float[]{0, 0, 0});

    // vectors across horizontal and down vertical viewport edges
    private Vector viewportU;
    private Vector viewportV;

    private Vector pixelDeltaU;
    private Vector pixelDeltaV;

    private Vector viewportUpperLeft;
    private Vector pixel00Loc;

    private IntersectableList world;
    private FloorPlan floorPlan;

    public RayTracer(FloorPlan floorPlan, int imageHeight, int imageWidth) {
        this.floorPlan = floorPlan;
        this.camera = floorPlan.getCamera();
        this.imageHeight = imageHeight;
        this.imageWidth = imageWidth;

        this.initialize();
    }

    public RayTracer(int imageHeight, int imageWidth) {
        this.imageHeight = imageHeight;
        this.imageWidth = imageWidth;

        this.world = new IntersectableList();
        world.add(new Sphere(0, 0, -1, 0.5f));
        world.add(new Sphere(0, -100.5f, -1f, 100f));

        this.initialize();
    }

    private void initialize() {
        this.viewportWidth = this.viewportHeight * ((float) imageWidth / (float) imageHeight);

        this.viewportU = new Vector(new float[]{ this.viewportWidth, 0, 0});
        this.viewportV = new Vector(new float[]{ 0, -this.viewportHeight, 0});

        this.pixelDeltaU = this.viewportU.divide((float) imageWidth);
        this.pixelDeltaV = this.viewportV.divide((float) imageHeight);

        // calculate location of upper left pixel
        Vector v1 = Vector.subtract(cameraCenter, new Vector(new float[]{ 0, 0, this.focalLength}));
        Vector v2 = Vector.subtract(v1, this.viewportU.divide(2f));
        this.viewportUpperLeft = Vector.subtract(v2, this.viewportV.divide(2f));

        Vector pixelDelta = Vector.add(this.pixelDeltaU, this.pixelDeltaV);
        this.pixel00Loc = Vector.add(this.viewportUpperLeft, pixelDelta.multiply(0.5f));
    }

    public Vector getCameraCenter() {
        return this.cameraCenter;
    }

    public Vector getPixel00Loc() {
        return this.pixel00Loc;
    }

    public Vector getPixelDeltaU() {
        return this.pixelDeltaU;
    }

    public Vector getPixelDeltaV() {
        return this.pixelDeltaV;
    }

    public void render(RayTraceTask task) {
        this.world.setTask(task); // so we can track progress in intersect()...

        // generate a matrix of colors for the image; currently just a
        // placeholder for the actual ray trace algorithm...
        Color[][] image = new Color[this.imageHeight][this.imageWidth];
        for (int i = 0; i < this.imageHeight; i++) {
            for (int j = 0; j < this.imageWidth; j++) {
                Color pixelColor = new Color(0, 0, 0);
                for (int sample = 0; sample < samplesPerPixel; sample++) {
                    Ray r = this.getRay(j, i);
                    Color rayColor = this.rayColor(r, world);
                    pixelColor.setColor(
                        Vector.add(pixelColor.getColor(), rayColor.getColor())
                    );
                    task.updateProgress(task.workDone++);
                }
                image[i][j] = pixelColor;
            }
        }

        // store the image as a PPM file
        String imageStr = PPMImageFormatter.write(image, task, samplesPerPixel);
        File file = new File("image.ppm");
        FileUtil.save(file, imageStr);
        task.updateProgress(task.workDone++);
    }

    private Ray getRay(int i, int j) {
        Vector pc1 = Vector.add(this.pixelDeltaU.multiply(i), this.pixelDeltaV.multiply(j));
        Vector pixelCenter = Vector.add(this.pixel00Loc, pc1);
        Vector pixelSample = Vector.add(pixelCenter, this.pixelSampleSquare());

        Vector rayOrigin = this.cameraCenter;
        Vector rayDirection = Vector.subtract(pixelSample, rayOrigin);
        return new Ray(rayOrigin, rayDirection);
    }

    private Vector pixelSampleSquare() {
        float px = -0.5f + Random.randomFloat();
        float py = -0.5f + Random.randomFloat();
        return Vector.add(
            pixelDeltaU.multiply(px),
            pixelDeltaV.multiply(py)
        );
    }

    public Color rayColor(Ray r, IntersectableList world) {
        IntersectRecord rec = world.intersect(r, 0, Float.POSITIVE_INFINITY);
        if (rec != null) {
            return new Color(
                Vector.add(
                    rec.getNormal(),
                    new Vector(new float[]{1, 1, 1})
                ).multiply(0.5f)
            );
        }

        Vector unitDirection = Vector.unit(r.getDirection());
        float a = 0.5f * (unitDirection.getY() + 1f);
        Vector c1 = new Vector(new float[]{ 1f, 1f, 1f });
        Vector c2 = new Vector(new float[]{ 0.5f, 0.7f, 1f });
        Vector color = Vector.add(c1.multiply(1 - a), c2.multiply(a));
        return new Color(color.getValues());
    }
}
