package floor.planner.services;

import java.util.Arrays;
import java.util.Comparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import floor.planner.models.Camera;
import floor.planner.util.jogl.material.Dielectric;
import floor.planner.util.jogl.material.DiffuseLight;
import floor.planner.util.jogl.material.Lambertian;
import floor.planner.util.jogl.material.Material;
import floor.planner.util.jogl.material.Metal;
import floor.planner.util.jogl.objects.Color;
import floor.planner.util.jogl.objects.obj3d.Cube;
import floor.planner.util.jogl.objects.obj3d.Quad;
import floor.planner.util.jogl.objects.obj3d.Sphere;
import floor.planner.util.jogl.raytracer.BvhNode;
import floor.planner.util.jogl.raytracer.IntersectableList;
import floor.planner.util.math.Matrix;
import floor.planner.util.math.Point3D;
import floor.planner.util.math.Random;
import floor.planner.util.math.Vector;

/**
 * The Ray Tracing in One Weekend (RTIOW) Series service separates out logic
 * for initializing intersectable lists for worlds in examples used in series.
 */
public class RTIOWSeriesService {
    private static final Logger logger = LoggerFactory.getLogger(RTIOWSeriesService.class);
    
    public IntersectableList cornellBox(Camera camera) {
        QuadComparator comparator = new QuadComparator(camera);
        IntersectableList world = new IntersectableList();
        Material red = new Lambertian(new Color(0.65, 0.05, 0.05));
        Material white = new Lambertian(new Color(0.73, 0.73, 0.73));
        Material green = new Lambertian(new Color(0.12, 0.45, 0.15));
        Material light = new DiffuseLight(new Color(15, 15, 15));
        Material aluminum = new Metal(new Color(0.8, 0.85, 0.88), 0.0);

        // create the "background" box sides
        world.add(new Quad(new Point3D(555, 0, 0), new Vector(0, 555, 0), new Vector(0, 0, 555), green));
        world.add(new Quad(new Point3D(0, 0, 0), new Vector(0, 555, 0), new Vector(0, 0, 555), red));
        world.add(new Quad(new Point3D(0, 0, 0), new Vector(555, 0, 0), new Vector(0, 0, 555), white));
        world.add(new Quad(new Point3D(555, 555, 555), new Vector(-555, 0, 0), new Vector(0, 0, -555), white));
        world.add(new Quad(new Point3D(0, 0, 555), new Vector(555, 0, 0), new Vector(0, 555, 0), white));

        // light
        world.add(new Quad(new Point3D(213,554,227), new Vector(130, 0, 0), new Vector(0, 0, 105), light));

        // box1
        Cube box1 = new Cube(new Point3D(0, 0, 0), new Point3D(165, 330, 165), aluminum);
        box1.setVertices(Matrix.rotateY(box1.getVertices(), 15));
        box1.setVertices(Matrix.translateX(box1.getVertices(), 265));
        box1.setVertices(Matrix.translateZ(box1.getVertices(), 295));
        box1.initQuads();
        world.addAll(box1.getIntersectableList());

        // box 2
        Cube box2 = new Cube(new Point3D(0, 0, 0), new Point3D(165, 165, 165), white);
        box2.setVertices(Matrix.rotateY(box2.getVertices(), -18));
        box2.setVertices(Matrix.translateX(box2.getVertices(), 130));
        box2.setVertices(Matrix.translateZ(box2.getVertices(), 65));
        box2.initQuads();
        world.addAll(box2.getIntersectableList());

        return world;
    }

    public IntersectableList cube() {
        IntersectableList world = new IntersectableList();
        Cube cube = new Cube(Cube.DEFAULT_VERTICES);
        cube.initQuads();
        world.addAll(cube.getIntersectableList());
        return world;
    }

    public IntersectableList quads() {
        IntersectableList world = new IntersectableList();
        Material red = new Lambertian(new Color(1f, 0.2f, 0.2f));
        Material green = new Lambertian(new Color(0.2f, 1f, 0.2f));
        Material blue = new Lambertian(new Color(0.2f, 0.2f, 1f));
        Material orange = new Lambertian(new Color(1f, 0.5f, 0f));
        Material teal = new Lambertian(new Color(0.2f, 0.8f, 0.8f));

        world.add(new Quad(
            new Point3D(-3, -2, 5),
            new Vector(0, 0, -4),
            new Vector(0, 4, 0), red)
        );
        world.add(new Quad(
            new Point3D(-2, -2, 0),
            new Vector(4, 0, 0),
            new Vector(0, 4, 0), green)
        );
        world.add(new Quad(
            new Point3D(3, -2, 1),
            new Vector(0, 0, 4),
            new Vector(0, 4, 0), blue)
        );
        world.add(new Quad(
            new Point3D(-2, 3, 1),
            new Vector(4, 0, 0),
            new Vector(0, 0, 4), orange)
        );
        world.add(new Quad(
            new Point3D(-2, -3, 5),
            new Vector(4, 0, 0),
            new Vector(0, 0, -4), teal)
        );

        return world;
    }

    public IntersectableList someSpheres() {
        IntersectableList world = new IntersectableList();
        Material mat1 = new Dielectric(1.5);
        Material mat2 = new Lambertian(new Color(0.4f, 0.2f, 0.1f));
        Material mat3 = new Metal(new Color(0.7f, 0.6f, 0.5f), 0.0);

        world.add(new Sphere(new Vector(0, 1, 0),   1, mat1));
        world.add(new Sphere(new Vector(-4, 1, 0),   1, mat2));
        world.add(new Sphere(new Vector(4, 1, 0),  1, mat3));
        BvhNode node = new BvhNode(world.getElements());
        world = new IntersectableList(world.getElements(), node.boundingBox());
        return world;
    }

    public IntersectableList spheres() {
        IntersectableList world = new IntersectableList();
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
        return world;
    }

    /**
     * The QuadComparator defines a convenience compare method for 2 quads,
     * comparing them in distance from the defined camera. This is needed for
     * the cubes rendered in this series, because as defined the quads do not
     * render correctly when included in ray traced image. This is true even
     * though the quads for the cube are defined in the same order they are in
     * the series. Without this the cubes can appear inside out when viewed at
     * different angles. Perhaps there is an issue with how vertices and quads
     * are defined, but this seems to at least get the desired image we are
     * going for...
     */
    private class QuadComparator implements Comparator<Quad> {
        private Camera camera;
    
        public QuadComparator(Camera camera) {
            this.camera = camera;
        }

        public int compare(Quad q1, Quad q2) {
            double d1 = Point3D.distanceBetween(q1.getMidPoint(), new Point3D(camera.getCenter()));
            double d2 = Point3D.distanceBetween(q2.getMidPoint(), new Point3D(camera.getCenter()));
            return Double.compare(d1, d2);
        }
    }
}
