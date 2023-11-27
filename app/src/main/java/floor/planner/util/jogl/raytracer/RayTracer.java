package floor.planner.util.jogl.raytracer;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import floor.planner.constants.RayTraceTaskType;
import floor.planner.models.Camera;
import floor.planner.models.FloorPlan;
import floor.planner.util.FileUtil;
import floor.planner.util.jogl.material.Dielectric;
import floor.planner.util.jogl.material.Lambertian;
import floor.planner.util.jogl.material.Material;
import floor.planner.util.jogl.material.Metal;
import floor.planner.util.jogl.material.ScatterAttenuation;
import floor.planner.util.jogl.objects.Color;
import floor.planner.util.jogl.objects.obj3d.Cube;
import floor.planner.util.jogl.objects.obj3d.Quad;
import floor.planner.util.jogl.objects.obj3d.Sphere;
import floor.planner.util.math.Interval;
import floor.planner.util.math.Point3D;
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
    private int maxDepth; // Maximum number of ray bounces into scene

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

        // set camera properties based on world objects
        if (type.equals(RayTraceTaskType.CUBE)) {
            // ray tracing a 1x1x1 cube starting from (0, 0, 0).
            this.camera.setVFov(20);
            this.camera.setLookFrom(2, 2, 2);
            this.camera.setLookAt(0, 0, 1);
            this.camera.setDefocusAngle(0);
        } else {
            // ray tracing spheres so use "default" settings...
            this.camera.setVUp(0, 1, 0);
            this.camera.setVFov(20);
            this.camera.setLookFrom(13, 2, 3);
            this.camera.setLookAt(0, 0, 0);
            this.camera.setDefocusAngle(0.6);
            this.camera.setFocusDist(10);
        }

        // initialize ray trace properties needed for camera
        this.camera.initRayTraceProperties(imageWidth, imageHeight);
    }

    private void initWorld(RayTraceTaskType type) {
        this.world = new IntersectableList();

        switch(type) {
            case CUBE:
                this.cube();
                break;
            case QUADS:
                this.quads();
                break;
            case SOME_SPHERES:
                this.someSpheres();
                break;
            default:
                this.spheres();
                break;
        }
    }

    private void cube() {
        this.world.add(new Cube(Cube.DEFAULT_VERTICES));
    }

    private void quads() {
        Material red = new Lambertian(new Color(1f, 0.2f, 0.2f));
        Material green = new Lambertian(new Color(0.2f, 1f, 0.2f));
        Material blue = new Lambertian(new Color(0.2f, 0.2f, 1f));
        Material orange = new Lambertian(new Color(1f, 0.5f, 0f));
        Material teal = new Lambertian(new Color(0.2f, 0.8f, 0.8f));

        this.world.add(new Quad(
            new Point3D(-3, -2, 5),
            new Vector(0, 0, -4),
            new Vector(0, 4, 0), red)
        );
        this.world.add(new Quad(
            new Point3D(-2, -2, 0),
            new Vector(4, 0, 0),
            new Vector(0, 4, 0), green)
        );
        this.world.add(new Quad(
            new Point3D(3, -2, 1),
            new Vector(0, 0, 4),
            new Vector(0, 4, 0), blue)
        );
        this.world.add(new Quad(
            new Point3D(-2, 3, 1),
            new Vector(4, 0, 0),
            new Vector(0, 0, 4), orange)
        );
        this.world.add(new Quad(
            new Point3D(-2, -3, 5),
            new Vector(4, 0, 0),
            new Vector(0, 0, -4), teal)
        );
    }

    private void someSpheres() {
        Material mat1 = new Dielectric(1.5);
        Material mat2 = new Lambertian(new Color(0.4f, 0.2f, 0.1f));
        Material mat3 = new Metal(new Color(0.7f, 0.6f, 0.5f), 0.0);

        world.add(new Sphere(new Vector(0, 1, 0),   1, mat1));
        world.add(new Sphere(new Vector(-4, 1, 0),   1, mat2));
        world.add(new Sphere(new Vector(4, 1, 0),  1, mat3));
        BvhNode node = new BvhNode(world.getElements());
        world = new IntersectableList(world.getElements(), node.boundingBox());
    }

    private void spheres() {
        Material ground = new Lambertian(new Color(0.5f, 0.5f, 0.5f));
        world.add(new Sphere(new Vector(0.0, -1000, 0), 1000, ground));

        for (int a = -11; a < 11; a++) {
            for (int b = -11; b < 11; b++) {
                // choose a material randomly
                double chooseMat = Random.randomDouble();
                Vector center = new Vector(a + 0.9 * Random.randomDouble(), 0.2, b + 0.9 * Random.randomDouble());

                if (Vector.subtract(center, new Vector(4, 0.2, 0)).length() > 0.9) {
                    Material mat;
                    if (chooseMat < 0.8) {
                        // diffuse
                        Color albedo = new Color(Vector.multiply(Vector.random(), Vector.random()));
                        mat = new Lambertian(albedo);
                    } else if (chooseMat < 0.95) {
                        // metal
                        Color albedo = new Color(Vector.random(0.5, 1));
                        double fuzz = Random.randomDouble(0, 0.5);
                        mat = new Metal(albedo, fuzz);
                    } else {
                        mat = new Dielectric(1.5);
                    }

                    world.add(new Sphere(center, 0.2, mat));
                }
            }
        }

        Material mat1 = new Dielectric(1.5);
        Material mat2 = new Lambertian(new Color(0.4f, 0.2f, 0.1f));
        Material mat3 = new Metal(new Color(0.7f, 0.6f, 0.5f), 0.0);
        
        world.add(new Sphere(new Vector(0, 1, 0),   1, mat1));
        world.add(new Sphere(new Vector(-4, 1, 0),   1, mat2));
        world.add(new Sphere(new Vector(4, 1, 0),  1, mat3));

        BvhNode node = new BvhNode(world.getElements());
        world = new IntersectableList(world.getElements(), node.boundingBox());
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
