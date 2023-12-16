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
import floor.planner.util.jogl.material.ScatterRecord;
import floor.planner.util.jogl.objects.Color;
import floor.planner.util.jogl.objects.obj3d.Quad;
import floor.planner.util.jogl.objects.obj3d.Sphere;
import floor.planner.util.jogl.raytracer.pdf.CosPdf;
import floor.planner.util.jogl.raytracer.pdf.IntersectablePdf;
import floor.planner.util.jogl.raytracer.pdf.MixturePdf;
import floor.planner.util.math.Interval;
import floor.planner.util.math.Point3D;
import floor.planner.util.math.Random;
import floor.planner.util.math.Ray;
import floor.planner.util.math.Vector;

public class RayTracer {
    private static final Logger logger = LoggerFactory.getLogger(RayTracer.class);
    private final RTIOWSeriesService service = new RTIOWSeriesService();

    private int samplesPerPixel = 10; // Count of random samples for each pixel
    private int sqrtSpp;
    private double recipSqrtSpp;

    // image
    private int imageWidth;
    private int imageHeight;

    // camera
    private Camera camera;
    private int maxDepth; // Maximum number of ray bounces into scene
    private Color background;

    private IntersectableList world;
    private IntersectableList lights;

    /**
     * Initializes the ray tracer for the current view of the 3D floor plan.
     */
    public RayTracer(FloorPlan floorPlan, int imageHeight, int imageWidth, int maxDepth, int samplesPerPixel) {
        this.imageHeight = imageHeight;
        this.imageWidth = imageWidth;
        this.maxDepth = maxDepth;
        this.samplesPerPixel = samplesPerPixel;
        this.camera = floorPlan.getCamera();

        this.world = floorPlan.getIntersectableList();
        this.camera.setFocusDist(Point3D.distanceBetween(new Point3D(this.camera.getLookFrom()), floorPlan.getMidPoint()));
        this.background = new Color(0, 0, 0);

        // add a light to the world so we can see stuff...
        Material diffuseLight = new DiffuseLight(new Color(15, 15, 15));
        Point3D midPoint = floorPlan.getMidPoint();
        // make it a big sphere high above mid point of floor plan
        // world.add(
        //     new Sphere(midPoint.getX(), midPoint.getY(), 10, floorPlan.getWidth() / 2, diffuseLight)
        // );
        Vector dx = new Vector(1, 0, 0);
        Vector dy = new Vector(0, -1, 0);
        world.add(
            new Quad(new Point3D(midPoint.getX() - 0.5f, midPoint.getY() - 0.5f, 3), dx, dy, diffuseLight)
        );

        Material m = new CustomMateral();
        this.lights = new IntersectableList(new Quad(new Point3D(midPoint.getX() - 0.5f, midPoint.getY() - 0.5f, 3), dx, dy, m));

        this.initRayTracer();
    }

    public RayTracer(
        int imageHeight,
        int imageWidth,
        int maxDepth,
        RayTraceTaskType type,
        int samplesPerPixel
    ) {
        this.imageHeight = imageHeight;
        this.imageWidth = imageWidth;
        this.maxDepth = maxDepth;
        this.camera = new Camera(imageWidth, imageHeight, 1);
        this.samplesPerPixel = samplesPerPixel;
        this.initWorld(type);

        this.initRayTracer();
    }

    private void initRayTracer() {
        // initialize ray trace properties needed for camera
        this.camera.initRayTraceProperties(imageWidth, imageHeight);

        this.sqrtSpp = (int) Math.sqrt(this.samplesPerPixel);
        this.recipSqrtSpp = (double) 1 / (double) this.sqrtSpp;
    }

    private class CustomMateral extends Material {
        public ScatterRecord scatter(Ray rIn, IntersectRecord rec) {
            return null;
        }
    }

    private void initWorld(RayTraceTaskType type) {
        Material m = new CustomMateral();
        switch(type) {
            case CORNELL_BOX:
                this.camera.setVFov(40);
                this.camera.setLookFrom(278, 278, -800);
                this.camera.setLookAt(278, 278, 0);
                this.camera.setVUp(0, 1, 0);
                this.camera.setDefocusAngle(0);
                this.world = service.cornellBox();
                this.lights = new IntersectableList(new Quad(new Point3D(343, 554, 332), new Vector(-130, 0, 0), new Vector(0, 0, -105), m));
                this.background = new Color(0, 0, 0); 
                break;
            case CORNELL_BOX_METAL:
                this.camera.setVFov(40);
                this.camera.setLookFrom(278, 278, -800);
                this.camera.setLookAt(278, 278, 0);
                this.camera.setVUp(0, 1, 0);
                this.camera.setDefocusAngle(0);
                this.world = service.cornellBoxMetal();
                this.lights = new IntersectableList(new Quad(new Point3D(343, 554, 332), new Vector(-130, 0, 0), new Vector(0, 0, -105), m));
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
                for (int sI = 0; sI < this.sqrtSpp; ++sI) {
                    for (int sJ = 0; sJ < this.sqrtSpp; ++sJ) {
                        Ray r = getRay(j, i, sJ, sI);
                        Color rayColor = this.rayColor(r, maxDepth, world, lights);
                        pixelColor.setColor(
                            pixelColor.getColor().add(rayColor.getColor())
                        );
                        task.updateProgress(task.workDone++);
                    }
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
    private Ray getRay(int i, int j, int sI, int sJ) {
        Vector pc1 = this.camera.getPixelDeltaU().multiply(i).add(this.camera.getPixelDeltaV().multiply(j));
        Vector pixelCenter = this.camera.getPixel00Loc().add(pc1);
        Vector pixelSample = pixelCenter.add(this.pixelSampleSquare(sI, sJ));

        Vector rayOrigin = this.camera.getDefocusAngle() <= 0 ? this.camera.getCenter() : defocusDiskSample();
        Vector rayDirection = pixelSample.subtract(rayOrigin);
        return new Ray(rayOrigin, rayDirection, Random.randomDouble());
    }

    private Vector defocusDiskSample() {
        Vector p = Vector.randomInUnitDisk();
        return 
            this.camera.getCenter().add(this.camera.getDefocusDiskU().multiply(p.getX())).add(
            this.camera.getDefocusDiskV().multiply(p.getY())
        );
    }

    private Vector pixelSampleSquare(int sI, int sJ) {
        double px = -0.5 + this.recipSqrtSpp * (sI + Random.randomDouble());
        double py = -0.5 + this.recipSqrtSpp * (sJ + Random.randomDouble());
        return
            this.camera.getPixelDeltaU().multiply(px).add(
            this.camera.getPixelDeltaV().multiply(py)
        );
    }

    public Color rayColor(Ray r, int depth, IntersectableList world, IntersectableList lights) {
        // If we've exceeded the ray bounce limit, no more light is gathered.
        if (depth <= 0) {
            return new Color(0,0,0);
        }

        IntersectRecord rec = world.intersect(r, new Interval(0.001, Double.POSITIVE_INFINITY));
        if (rec == null) {
            return this.getBackground(r);
        }

        Ray scattered;
        double pdfVal;
        Color colorFromEmission = rec.mat.emitted(r, rec, rec.getU(), rec.getV(), new Point3D(rec.getP()));
        ScatterRecord srec = rec.mat.scatter(r, rec);
        if (srec == null) {
            return colorFromEmission;
        }

        if (srec.skipPdf) {
            return new Color(
                srec.attenuation.getColor().multiply(
                this.rayColor(srec.skipPdfRay, depth - 1, world, lights).getColor()
            ));
        }

        Color colorFromScatter;
        // not all scenes use lights
        if (this.lights != null) {
            // calculate color including lights
            IntersectablePdf lightPdf = new IntersectablePdf(lights, new Point3D(rec.getP()));
            MixturePdf mixedPdf = new MixturePdf(lightPdf, srec.pdf);
            scattered = new Ray(rec.getP(), mixedPdf.generate(), r.getTime());
            pdfVal = mixedPdf.value(scattered.getDirection());

            double scatteringPdf = rec.mat.scatteringPdf(r, rec, scattered);

            Color sampleColor = this.rayColor(scattered, depth - 1, world, lights);
            colorFromScatter = new Color(
                srec.attenuation.getColor().multiply(scatteringPdf).multiply(
                sampleColor.getColor()
            ).divide(pdfVal));
        } else {
            // calculate color without lights; scatter is either defined by scatter
            // ray or using existing skipPdfRay (lambertian is only material that
            // currently calculates separate scatterRay, all others just use the
            // skipPdfRay which seems to be same as what scatterRay would have been
            // from ScatterAttentuation...)
            Ray scatter = srec.scatterRay != null ? srec.scatterRay : srec.skipPdfRay;

            colorFromScatter = new Color(
                Vector.multiply(
                    srec.attenuation.getColor(),
                    rayColor(scatter, depth - 1, world, null)
                        .getColor()
                )
            );
        }

        return new Color(
            colorFromEmission.getColor().add(
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
            Vector color = c1.multiply(1 - a).add(c2.multiply(a));
            return new Color(color.getFloatValues());
        }
    }
}
