package floor.planner.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import floor.planner.util.math.Color;
import floor.planner.util.math.Matrix;
import floor.planner.util.math.Point3D;
import floor.planner.util.math.Random;
import floor.planner.util.math.Vector;
import floor.planner.util.objects.obj3d.Cube;
import floor.planner.util.objects.obj3d.Quad;
import floor.planner.util.objects.obj3d.Sphere;
import floor.planner.util.raytracer.BvhNode;
import floor.planner.util.raytracer.IntersectableList;
import floor.planner.util.raytracer.IntersectableWorld;
import floor.planner.util.raytracer.material.Dielectric;
import floor.planner.util.raytracer.material.DiffuseLight;
import floor.planner.util.raytracer.material.Lambertian;
import floor.planner.util.raytracer.material.Material;
import floor.planner.util.raytracer.material.Metal;
import floor.planner.util.raytracer.material.SimpleMaterial;

/**
 * The Ray Tracing in One Weekend (RTIOW) Series service separates out logic
 * for initializing intersectable lists for worlds in examples used in series.
 */
public class RTIOWSeriesService {
    private static final Logger logger = LoggerFactory.getLogger(RTIOWSeriesService.class);
    private Material m = new SimpleMaterial();
    
    public IntersectableWorld cornellBox() {
        IntersectableList world = this.emptyCornellBox();
        Material white = new Lambertian(new Color(0.73, 0.73, 0.73));

        // box 1
        world.addAll(
            this.getCube1(
                new Point3D(0, 0, 0),
                new Point3D(165, 330, 165),
                15, 
                265,
                295,
                white
            ).getIntersectableList()
        );

        // box 2
        world.addAll(
            this.getCube1(
                new Point3D(0, 0, 0),
                new Point3D(165, 165, 165),
                -18, 
                130,
                65,
                white
            ).getIntersectableList()
        );

        IntersectableList lights = new IntersectableList(new Quad(new Point3D(343, 554, 332), new Vector(-130, 0, 0), new Vector(0, 0, -105), m));

        return new IntersectableWorld(world, lights);
    }

    public IntersectableWorld cornellBoxGlass() {
        IntersectableList world = this.emptyCornellBox();
        Material white = new Lambertian(new Color(0.73, 0.73, 0.73));
        Material glass = new Dielectric(1.5);

        // box
        world.addAll(
            this.getCube1(
                new Point3D(0, 0, 0),
                new Point3D(165, 330, 165),
                15, 
                265,
                295,
                white
            ).getIntersectableList()
        );

        // glass sphere
        world.add(new Sphere(new Vector(190, 90, 190), 90, glass));

        IntersectableList lights = new IntersectableList(new Quad(new Point3D(343, 554, 332), new Vector(-130, 0, 0), new Vector(0, 0, -105), m));
        lights.add(new Sphere(new Vector(190, 90, 190), 90, m));

        return new IntersectableWorld(world, lights);
    }

    public IntersectableWorld cornellBoxMetal() {
        IntersectableList world = this.emptyCornellBox();
        Material aluminum = new Metal(new Color(0.8, 0.85, 0.88), 0.0);
        Material white = new Lambertian(new Color(0.73, 0.73, 0.73));

        // box 1
        world.addAll(
            this.getCube1(
                new Point3D(0, 0, 0),
                new Point3D(165, 330, 165),
                15, 
                265,
                295,
                aluminum
            ).getIntersectableList()
        );

        // box 2
        world.addAll(
            this.getCube1(
                new Point3D(0, 0, 0),
                new Point3D(165, 165, 165),
                -18, 
                130,
                65,
                white
            ).getIntersectableList()
        );

        IntersectableList lights = new IntersectableList(new Quad(new Point3D(343, 554, 332), new Vector(-130, 0, 0), new Vector(0, 0, -105), m));

        return new IntersectableWorld(world, lights);
    }

    private Cube getCube1(
        Point3D p1,
        Point3D p2,
        float rotateY,
        float translateX,
        float translateZ,
        Material m
    ) {
        Cube box = new Cube(p1, p2, m);
        box.setVertices(Matrix.rotateY(box.getVertices(), rotateY));
        box.setVertices(Matrix.translateX(box.getVertices(), translateX));
        box.setVertices(Matrix.translateZ(box.getVertices(), translateZ));
        box.initQuads();
        return box;
    }

    private IntersectableList emptyCornellBox() {
        IntersectableList world = new IntersectableList();
        Material red = new Lambertian(new Color(0.65, 0.05, 0.05));
        Material white = new Lambertian(new Color(0.73, 0.73, 0.73));
        Material green = new Lambertian(new Color(0.12, 0.45, 0.15));
        Material light = new DiffuseLight(new Color(15, 15, 15));

        // create the "background" box sides
        world.add(new Quad(new Point3D(555, 0, 0), new Vector(0, 555, 0), new Vector(0, 0, 555), green));
        world.add(new Quad(new Point3D(0, 0, 0), new Vector(0, 555, 0), new Vector(0, 0, 555), red));
        world.add(new Quad(new Point3D(0, 0, 0), new Vector(555, 0, 0), new Vector(0, 0, 555), white));
        world.add(new Quad(new Point3D(555, 555, 555), new Vector(-555, 0, 0), new Vector(0, 0, -555), white));
        world.add(new Quad(new Point3D(0, 0, 555), new Vector(555, 0, 0), new Vector(0, 555, 0), white));

        // light
        world.add(new Quad(new Point3D(213,554,227), new Vector(130, 0, 0), new Vector(0, 0, 105), light));

        return world;
    }

    public IntersectableWorld cube() {
        IntersectableList world = new IntersectableList();
        Cube cube = new Cube(Cube.DEFAULT_VERTICES);
        cube.initQuads();
        world.addAll(cube.getIntersectableList());
        return new IntersectableWorld(world);
    }

    public IntersectableWorld quads() {
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

        return new IntersectableWorld(world);
    }

    public IntersectableWorld someSpheres() {
        IntersectableList world = new IntersectableList();
        Material mat1 = new Dielectric(1.5);
        Material mat2 = new Lambertian(new Color(0.4f, 0.2f, 0.1f));
        Material mat3 = new Metal(new Color(0.7f, 0.6f, 0.5f), 0.0);

        world.add(new Sphere(new Vector(0, 1, 0),   1, mat1));
        world.add(new Sphere(new Vector(-4, 1, 0),   1, mat2));
        world.add(new Sphere(new Vector(4, 1, 0),  1, mat3));
        BvhNode node = new BvhNode(world.getElements());
        world = new IntersectableList(world.getElements(), node.boundingBox());
        return new IntersectableWorld(world);
    }

    public IntersectableWorld spheres() {
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
        return new IntersectableWorld(world);
    }
}
