package floor.planner.util.jogl.raytracer;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import floor.planner.constants.RayTraceTaskType;
import floor.planner.models.Camera;
import floor.planner.models.FloorPlan;
import floor.planner.services.RTIOWSeriesService;
import floor.planner.util.FileUtil;
import floor.planner.util.jogl.material.DiffuseLight;
import floor.planner.util.jogl.material.Material;
import floor.planner.util.jogl.material.ScatterAttenuation;
import floor.planner.util.jogl.objects.Color;
import floor.planner.util.jogl.objects.obj3d.Sphere;
import floor.planner.util.math.Interval;
import floor.planner.util.math.Point3D;
import floor.planner.util.math.Random;
import floor.planner.util.math.Ray;
import floor.planner.util.math.Vector;

public class RayTracer {
    private static final Logger logger = LoggerFactory.getLogger(RayTracer.class);
    private final RTIOWSeriesService service = new RTIOWSeriesService();

    private int samplesPerPixel = 100; // Count of random samples for each pixel

    // image
    private int imageWidth;
    private int imageHeight;

    // camera
    private Camera camera;
    private int maxDepth; // Maximum number of ray bounces into scene
    private Color background;

    private IntersectableList world;

    /**
     * Initializes the ray tracer for the current view of the 3D floor plan.
     */
    public RayTracer(FloorPlan floorPlan, int imageHeight, int imageWidth, int maxDepth) {
        this.imageHeight = imageHeight;
        this.imageWidth = imageWidth;
        this.maxDepth = maxDepth;
        this.camera = floorPlan.getCamera();

        this.world = floorPlan.getIntersectableList();
        this.camera.setFocusDist(Point3D.distanceBetween(new Point3D(this.camera.getLookFrom()), floorPlan.getMidPoint()));
        this.background = new Color(0, 0, 0);
        
        // add a light to the world so we can see stuff...
        Material diffuseLight = new DiffuseLight(new Color(15, 15, 15));
        Point3D midPoint = floorPlan.getMidPoint();
        // make it a big sphere high above mid point of floor plan
        world.add(
            new Sphere(midPoint.getX(), midPoint.getY(), 10, 6, diffuseLight)
        );

        // initialize ray trace properties needed for camera
        this.camera.initRayTraceProperties(imageWidth, imageHeight);
    }

    public RayTracer(
        int imageHeight,
        int imageWidth,
        int maxDepth,
        RayTraceTaskType type
    ) {
        this.imageHeight = imageHeight;
        this.imageWidth = imageWidth;
        this.maxDepth = maxDepth;
        this.camera = new Camera(imageWidth, imageHeight, 1);
        this.initWorld(type);

        // initialize ray trace properties needed for camera
        this.camera.initRayTraceProperties(imageWidth, imageHeight);
    }

    private void initWorld(RayTraceTaskType type) {
        switch(type) {
            case CORNELL_BOX:
                this.world = service.cornellBox();
                this.camera.setVFov(40);
                this.camera.setLookAt(278, 278, 0);
                this.camera.setLookFrom(278, 278, -800);
                this.camera.setVUp(0, 1, 0);
                this.camera.setDefocusAngle(0);
                this.background = new Color(0, 0, 0);
                break;
            case CUBE:
                this.world = service.cube();
                this.camera.setVFov(40);
                this.camera.setLookFrom(2, 2, 2);
                this.camera.setLookAt(0, 0, 0);
                this.camera.setDefocusAngle(0);
                break;
            case QUADS:
                this.world = service.quads();
                this.camera.setVFov(80);
                this.camera.setLookFrom(0, 0, 9);
                this.camera.setLookAt(0, 0, 0);
                this.camera.setVUp(0, 1, 0);
                this.camera.setDefocusAngle(0);
                break;
            case SOME_SPHERES:
                this.world = service.someSpheres();
                // ray tracing spheres so use "default" settings...
                this.camera.setVUp(0, 1, 0);
                this.camera.setVFov(20);
                this.camera.setLookFrom(13, 2, 3);
                this.camera.setLookAt(0, 0, 0);
                this.camera.setDefocusAngle(0.6);
                this.camera.setFocusDist(10);
                break;
            default:
                this.world = service.spheres();
                this.camera.setVUp(0, 1, 0);
                this.camera.setVFov(20);
                this.camera.setLookFrom(13, 2, 3);
                this.camera.setLookAt(0, 0, 0);
                this.camera.setDefocusAngle(0.6);
                this.camera.setFocusDist(10);
                break;
        }
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
        logger.info("Ray trace done, saving image to disk...");

        // store the image as a PPM file
        String imageStr = PPMImageFormatter.write(image, task, samplesPerPixel);
        File file = new File("image.ppm");
        FileUtil.save(file, imageStr);
        task.updateProgress(task.workDone++);
        logger.info("Ray trace image saved to disk successfully");
    }

    // get a randomly sampled camera ray for pixel at location i,j originating
    // from camera defocus disk
    private Ray getRay(int i, int j) {
        Vector pc1 = Vector.add(this.camera.getPixelDeltaU().multiply(i), this.camera.getPixelDeltaV().multiply(j));
        Vector pixelCenter = Vector.add(this.camera.getPixel00Loc(), pc1);
        Vector pixelSample = Vector.add(pixelCenter, this.pixelSampleSquare());

        Vector rayOrigin = this.camera.getDefocusAngle() <= 0 ? this.camera.getCenter() : defocusDiskSample();
        Vector rayDirection = Vector.subtract(pixelSample, rayOrigin);
        return new Ray(rayOrigin, rayDirection);
    }

    private Vector defocusDiskSample() {
        Vector p = Vector.randomInUnitDisk();
        return Vector.add(
            Vector.add(this.camera.getCenter(), this.camera.getDefocusDiskU().multiply(p.getX())),
            this.camera.getDefocusDiskV().multiply(p.getY())
        );
    }

    private Vector pixelSampleSquare() {
        double px = -0.5 + Random.randomDouble();
        double py = -0.5 + Random.randomDouble();
        return Vector.add(
            this.camera.getPixelDeltaU().multiply(px),
            this.camera.getPixelDeltaV().multiply(py)
        );
    }

    public Color rayColor(Ray r, int depth, IntersectableList world) {
        // If we've exceeded the ray bounce limit, no more light is gathered.
        if (depth <= 0) {
            return new Color(0,0,0);
        }

        IntersectRecord rec = world.intersect(r, new Interval(0.001, Double.POSITIVE_INFINITY));
        if (rec == null) {
            return this.getBackground(r);
        }

        Color colorFromEmission = rec.mat.emitted(rec.getU(), rec.getV(), new Point3D(rec.getP()));

        ScatterAttenuation sa = rec.mat.scatter(r, rec);
        if (sa == null) {
            return colorFromEmission;
        }

        Color colorFromScatter = new Color(
            Vector.multiply(
                sa.getAttenuation().getColor(),
                rayColor(sa.getScattered(), depth - 1, world)
                    .getColor()
            )
        );

        return new Color(
            Vector.add(
                colorFromEmission.getColor(),
                colorFromScatter.getColor()
            )
        );
    }

    private Color getBackground(Ray r) {
        if (this.background != null) {
            return this.background;
        } else {
            Vector unitDirection = Vector.unit(r.getDirection());
            double a = 0.5f * (unitDirection.getY() + 1);
            Vector c1 = new Vector(new double[]{ 1, 1, 1 });
            Vector c2 = new Vector(new double[]{ 0.5, 0.7, 1 });
            Vector color = Vector.add(c1.multiply(1 - a), c2.multiply(a));
            return new Color(color.getFloatValues());
        }
    }
}
