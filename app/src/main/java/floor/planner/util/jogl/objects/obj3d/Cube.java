package floor.planner.util.jogl.objects.obj3d;

import com.jogamp.opengl.GL2;

import floor.planner.util.jogl.material.Lambertian;
import floor.planner.util.jogl.material.Material;
import floor.planner.util.jogl.objects.Color;
import floor.planner.util.jogl.raytracer.Aabb;
import floor.planner.util.jogl.raytracer.IntersectRecord;
import floor.planner.util.jogl.raytracer.IntersectableList;
import floor.planner.util.math.Interval;
import floor.planner.util.math.MathUtil;
import floor.planner.util.math.Point3D;
import floor.planner.util.math.Random;
import floor.planner.util.math.Ray;
import floor.planner.util.math.Vector;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cube extends DrawableElement3D {
    private static Logger logger = LoggerFactory.getLogger(Cube.class);

    /**
     * Default vertices for a 1x1x1 cube located at the origin extending in
     * positive x, y, z planes.
     */
    public static float[][] DEFAULT_VERTICES = {
        {0, 0, 1}, {0, 1, 1}, {1, 1, 1}, {1, 0, 1},
        {0, 0, 0}, {0, 1, 0}, {1, 1, 0}, {1, 0, 0}
    };
    /**
     * Order to draw vertices to render cube in 3D.
     */
    public static int[][] VERTICES_ORDER = {
        {0, 3, 2, 1},
        {2, 3, 7, 6},
        {3, 0, 4, 7},
        {1, 2, 6, 5},
        {4, 5, 6, 7},
        {5, 4, 0, 1}
    };
    public static List<Integer> TOP_FACE = Arrays.asList(0, 1, 2, 3);
    public static List<Integer> BOTTOM_FACE = Arrays.asList(4, 5, 6, 7);
    public static List<Integer> FRONT_FACE = Arrays.asList(0, 3, 4, 7);
    public static List<Integer> BACK_FACE = Arrays.asList(1, 2, 5, 6);
    public static List<Integer> RIGHT_FACE = Arrays.asList(2, 3, 6, 7);
    public static List<Integer> LEFT_FACE = Arrays.asList(0, 1, 4, 5);

    private float[][] vertices;
    private Quad[] quads;
    protected Material mat;

    public Cube(float[][] vertices) {
        this.vertices = vertices;

        // default to diffuse red for now...
        this.color = new Color(1f, 0f, 0f);
        this.mat = new Lambertian(new Color(Vector.random()));
    }

    /**
     * Initializes quads for each side of the cube which are needed for ray
     * tracer.
     */
    public void initQuads() {
        this.quads = new Quad[6];
        this.quads[0] = this.createQuad(vertices[2], vertices[1], vertices[3], mat);
        this.quads[1] = this.createQuad(vertices[2], vertices[6], vertices[1], mat);
        this.quads[2] = this.createQuad(vertices[2], vertices[3], vertices[6], mat);
        this.quads[3] = this.createQuad(vertices[4], vertices[5], vertices[7], mat);
        this.quads[4] = this.createQuad(vertices[4], vertices[0], vertices[5], mat);
        this.quads[5] = this.createQuad(vertices[4], vertices[7], vertices[0], mat);

        this.boundingBox = new Aabb();
    }

    /**
     * Creates and returns a Quad given 3 points in a plane. The points are
     * provided as float arrays since these values come from the vertices for
     * the cube. The first point should be the origin, the other 2 should be
     * points that can be used to generate perpendicular vectors starting at
     * the origin point.
     *
     * @param originf The origin point of the quad. 
     * @param uf A point to generate vector for one side of quad.
     * @param vf A point to generate perpendicular vector for other side of quad.
     * @param mat The material for the quad (needed for ray tracer).
     * @return The Quad 
     */
    private Quad createQuad(float[] originf, float[] uf, float[] vf, Material mat) {
        Point3D origin = new Point3D(originf[0], originf[1], originf[2]);
        Vector u = new Vector(
            MathUtil.floatToDoubleArray(originf),
            MathUtil.floatToDoubleArray(uf)
        );
        Vector v = new Vector(
            MathUtil.floatToDoubleArray(originf),
            MathUtil.floatToDoubleArray(vf)
        );
        return new Quad(origin, u, v, mat);
    }

    public float[][] getVertices() {
        return this.vertices;
    }
    public void setVertices(float[][] vertices) {
        this.vertices = vertices;
    }

    @Override
    public void draw(GL2 gl) {
        int[][] verticesOrder = Cube.VERTICES_ORDER;
        for (int i = 0; i < verticesOrder.length; i++) {
            int[] order = verticesOrder[i];
            double[] normal = Vector.normal(
                MathUtil.floatToDoubleArray(vertices[order[0]]),
                MathUtil.floatToDoubleArray(vertices[order[1]]),
                MathUtil.floatToDoubleArray(vertices[order[2]]),
                MathUtil.floatToDoubleArray(vertices[order[3]])
            );
            gl.glNormal3f((float) normal[0], (float) normal[1], (float) normal[2]);
            this.drawPolygon(
                gl,
                Arrays.asList(
                    vertices[order[0]],
                    vertices[order[1]],
                    vertices[order[2]],
                    vertices[order[3]]
                )
            );
        }
    }

    @Override
    public boolean intersect(
        Ray r,
        Interval rayT,
        IntersectRecord rec
    ) {
        // there is an intersect if any of the quads that make up the cube are
        // intersected
        for (Quad quad : this.quads) {
            if (quad.intersect(r, rayT, rec)) {
                return true;
            }
        }
         return false;
    }

    @Override
    public Aabb boundingBox() {
        return this.boundingBox;
    }

    public IntersectableList getIntersectableList() {
        return new IntersectableList(Arrays.asList(this.quads), boundingBox);
    }

    public Point3D getMidPoint() {
        Vector midPoint = new Vector(0, 0, 0);
        for(float[] vertex : vertices) {
            midPoint = midPoint.add(MathUtil.floatToDoubleArray(vertex));
        }
        midPoint = midPoint.divide(this.vertices.length);
        return new Point3D(midPoint);
    }
}
