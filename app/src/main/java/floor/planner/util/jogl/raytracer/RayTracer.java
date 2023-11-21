package floor.planner.util.jogl.raytracer;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import floor.planner.models.Camera;
import floor.planner.models.FloorPlan;
import floor.planner.util.FileUtil;
import floor.planner.util.jogl.material.Lambertian;
import floor.planner.util.jogl.material.Material;
import floor.planner.util.jogl.material.Metal;
import floor.planner.util.jogl.material.ScatterAttenuation;
import floor.planner.util.jogl.objects.Color;
import floor.planner.util.jogl.objects.obj3d.Sphere;
import floor.planner.util.math.Interval;
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
    private double focalLength = 1;
    private double viewportHeight = 2;
    private double viewportWidth;
    private Vector cameraCenter = new Vector(new double[]{0, 0, 0});
    private int maxDepth; // Maximum number of ray bounces into scene

    // vectors across horizontal and down vertical viewport edges
    private Vector viewportU;
    private Vector viewportV;

    private Vector pixelDeltaU;
    private Vector pixelDeltaV;

    private Vector viewportUpperLeft;
    private Vector pixel00Loc;

    private IntersectableList world;
    private FloorPlan floorPlan;

    public RayTracer(FloorPlan floorPlan, int imageHeight, int imageWidth, int maxDepth) {
        this.floorPlan = floorPlan;
        this.camera = floorPlan.getCamera();
        this.imageHeight = imageHeight;
        this.imageWidth = imageWidth;
        this.maxDepth = maxDepth;

        this.initialize();
    }

    public RayTracer(int imageHeight, int imageWidth, int maxDepth) {
        this.imageHeight = imageHeight;
        this.imageWidth = imageWidth;
        this.maxDepth = maxDepth;

        Material ground = new Lambertian(new Color(0.8f, 0.8f, 0.0f));
        Material center = new Lambertian(new Color(0.7f, 0.3f, 0.3f));
        Material left = new Metal(new Color(0.8f, 0.8f, 0.8f), 0.3);
        Material right = new Metal(new Color(0.8f, 0.6f, 0.2f), 1.0);

        this.world = new IntersectableList();
        world.add(new Sphere(0, -100.5f, -1f, 100, ground));
        world.add(new Sphere(0, 0, -1, 0.5, center));
        world.add(new Sphere(-1, 0, -1, 0.5, left));
        world.add(new Sphere(1, 0, -1, 0.5, right));

        this.initialize();
    }

    private void initialize() {
        this.viewportWidth = this.viewportHeight * ((double) imageWidth / (double) imageHeight);

        this.viewportU = new Vector(new double[]{ this.viewportWidth, 0, 0});
        this.viewportV = new Vector(new double[]{ 0, -this.viewportHeight, 0});

        this.pixelDeltaU = this.viewportU.divide((double) imageWidth);
        this.pixelDeltaV = this.viewportV.divide((double) imageHeight);

        // calculate location of upper left pixel
        Vector v1 = Vector.subtract(cameraCenter, new Vector(new double[]{ 0, 0, this.focalLength}));
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
                    Color rayColor = this.rayColor(r, maxDepth, world);
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
        double px = -0.5 + Random.randomDouble();
        double py = -0.5 + Random.randomDouble();
        return Vector.add(
            pixelDeltaU.multiply(px),
            pixelDeltaV.multiply(py)
        );
    }

    public Color rayColor(Ray r, int depth, IntersectableList world) {
        // If we've exceeded the ray bounce limit, no more light is gathered.
        if (depth <= 0) {
            return new Color(0,0,0);
        }

        IntersectRecord rec = world.intersect(r, new Interval(0.001, Double.POSITIVE_INFINITY));
        if (rec != null) {
            ScatterAttenuation sa = rec.mat.scatter(r, rec);
            if (sa != null) {
                return new Color(
                    Vector.multiply(
                        sa.getAttenuation().getColor(),
                        rayColor(sa.getScattered(), depth - 1, world)
                            .getColor()
                    )
                );
            } else {
                return new Color(0, 0, 0);
            }
        }

        Vector unitDirection = Vector.unit(r.getDirection());
        double a = 0.5f * (unitDirection.getY() + 1);
        Vector c1 = new Vector(new double[]{ 1, 1, 1 });
        Vector c2 = new Vector(new double[]{ 0.5, 0.7, 1 });
        Vector color = Vector.add(c1.multiply(1 - a), c2.multiply(a));
        return new Color(color.getFloatValues());
    }
}
