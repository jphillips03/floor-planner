package floor.planner.util.jogl.raytracer;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import floor.planner.models.Camera;
import floor.planner.models.FloorPlan;
import floor.planner.util.FileUtil;
import floor.planner.util.jogl.material.Dielectric;
import floor.planner.util.jogl.material.Lambertian;
import floor.planner.util.jogl.material.Material;
import floor.planner.util.jogl.material.Metal;
import floor.planner.util.jogl.material.ScatterAttenuation;
import floor.planner.util.jogl.objects.Color;
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
    private Vector lookFrom = new Vector(13f, 2f, 3f);
    private Vector lookAt = new Vector(0, 0, 0);
    private Vector vUp = new Vector(0, 1, 0);
    private Vector defocusDiskU; // defocus disk horizontal radius
    private Vector defocusDiskV; // defocus disk vertical radius
    private double viewportHeight;
    private double viewportWidth;
    private Vector cameraCenter;
    private int maxDepth; // Maximum number of ray bounces into scene
    private double vFov = 20; // vertical view angle (field of view)
    private Vector u, v, w; // camera frame basis vectors
    private double defocusAngle = 0.6; // variation angle of rays through each pixel
    private double focusDist = 10.0; // distance from camera lookFrom point to plane of perfect focus

    // vectors across horizontal and down vertical viewport edges
    private Vector viewportU;
    private Vector viewportV;

    private Vector pixelDeltaU; // offset to pixel to right
    private Vector pixelDeltaV; // offset to pixel below

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
        this.world = new IntersectableList();

        //this.spheres();
        this.quads();

        this.initialize();
    }

    private void quads() {
        Material left = new Lambertian(new Color(1f, 0.2f, 0.2f));
        Material green = new Lambertian(new Color(0.2f, 1f, 0.2f));
        Material blue = new Lambertian(new Color(0.2f, 0.2f, 1f));
        Material orange = new Lambertian(new Color(1f, 0.5f, 0f));
        Material teal = new Lambertian(new Color(0.2f, 0.8f, 0.8f));

        this.world.add(new Quad(
            new Point3D(-3, -2, 5),
            new Vector(0, 0, -4),
            new Vector(0, 4, 0), left)
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

        this.vFov = 80;
        this.lookFrom = new Vector(0, 0, 9);
        this.lookAt = new Vector(0, 0, 0);
        this.vUp = new Vector(0, 1, 0);
        defocusAngle = 0;
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

    private void initialize() {
        this.cameraCenter = lookFrom;

        // determine viewport dimensions
        double theta = Math.toRadians(vFov);
        double h = Math.tan(theta / 2);
        this.viewportHeight = 2 * h * focusDist;
        this.viewportWidth = this.viewportHeight * ((double) imageWidth / (double) imageHeight);

        // calculate u, v, w unit basis vectors for camera coordinate frame
        w = Vector.unit(Vector.subtract(lookFrom, lookAt));
        u = Vector.unit(Vector.cross(vUp, w));
        v = Vector.cross(w, u);
    
        this.viewportU = u.multiply(this.viewportWidth);// new Vector(new double[]{ this.viewportWidth, 0, 0});
        this.viewportV = v.multiply(-1).multiply(this.viewportHeight); // new Vector(new double[]{ 0, -this.viewportHeight, 0});

        this.pixelDeltaU = this.viewportU.divide((double) imageWidth);
        this.pixelDeltaV = this.viewportV.divide((double) imageHeight);

        // calculate location of upper left pixel
        Vector v1 = Vector.subtract(cameraCenter, w.multiply(focusDist));
        Vector v2 = Vector.subtract(v1, this.viewportU.divide(2));
        this.viewportUpperLeft = Vector.subtract(v2, this.viewportV.divide(2));

        Vector pixelDelta = Vector.add(this.pixelDeltaU, this.pixelDeltaV);
        this.pixel00Loc = Vector.add(this.viewportUpperLeft, pixelDelta.multiply(0.5f));

        // calculate camera defocus disk basis vectors
        double defocusRadius = focusDist * Math.tan(Math.toRadians(defocusAngle / 2));
        defocusDiskU = u.multiply(defocusRadius);
        defocusDiskV = v.multiply(defocusRadius);
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
        Vector pc1 = Vector.add(this.pixelDeltaU.multiply(i), this.pixelDeltaV.multiply(j));
        Vector pixelCenter = Vector.add(this.pixel00Loc, pc1);
        Vector pixelSample = Vector.add(pixelCenter, this.pixelSampleSquare());

        Vector rayOrigin = defocusAngle <= 0 ? cameraCenter : defocusDiskSample();
        Vector rayDirection = Vector.subtract(pixelSample, rayOrigin);
        return new Ray(rayOrigin, rayDirection);
    }

    private Vector defocusDiskSample() {
        Vector p = Vector.randomInUnitDisk();
        return Vector.add(
            Vector.add(cameraCenter, defocusDiskU.multiply(p.getX())),
            defocusDiskV.multiply(p.getY())
        );
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
