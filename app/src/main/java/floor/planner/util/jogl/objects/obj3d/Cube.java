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
import floor.planner.util.math.Ray;
import floor.planner.util.math.Vector;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**    
 *          y
 *   z      ^          
 *   ^     / 
 *   | 5  / 6
 *   | +----+
 *   |/| / /|
 *  1+--/-+2|
 *   |4+--|-+7
 *   |/   |/
 *   +----+-------->x
 *   0    3
 */
public class Cube extends DrawableElement3D {
    private static Logger logger = LoggerFactory.getLogger(Cube.class);

    /**
     * Default vertices for a 1x1x1 cube located at the origin extending in
     * positive x, y, z planes with z plane being up.
     */
    public static float[][] DEFAULT_VERTICES = {
        {0, 0, 0}, {0, 0, 1}, {1, 0, 1}, {1, 0, 0},
        {0, 1, 0}, {0, 1, 1}, {1, 1, 1}, {1, 1, 0}
    };
    /**
     * Default vertices for a 1x1x1 cube located at the origin extending in
     * positive x, y, z planes with y plane being up. The RTIOW series uses
     * y as up, so this is used when creating cubes for worlds in that series.
     */
    public static float[][] DEFAULT_VERTICES_Y_UP = {
        {0, 0, 0}, {0, 1, 0}, {1, 1, 0}, {1, 0, 0},
        {0, 0, 1}, {0, 1, 1}, {1, 1, 1}, {1, 0, 1}
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
    public static int[] FRONT_FACE = VERTICES_ORDER[0];
    public static int[] RIGHT_FACE = VERTICES_ORDER[1];
    public static int[] BOTTOM_FACE = VERTICES_ORDER[2];
    public static int[] TOP_FACE = VERTICES_ORDER[3];
    public static int[] BACK_FACE = VERTICES_ORDER[4];
    public static int[] LEFT_FACE = VERTICES_ORDER[5];

    private float[][] vertices;
    private Quad[] quads;
    protected Material mat;

    public Cube(float[][] vertices) {
        this.vertices = vertices;

        // default to diffuse red for now...
        this.color = new Color(1f, 0f, 0f);
        this.mat = new Lambertian(new Color(Vector.random()));
    }

    public Cube(float[][] vertices, Material mat) {
        this.vertices = vertices;
        this.mat = mat;
    }

    public Quad[] getQuads() {
        return this.quads;
    }
    public void setQuads(Quad[] quads) {
        this.quads = quads;

        // reset the bounding box...
        this.boundingBox = new Aabb();
        for (Quad quad : this.quads) {
            this.boundingBox = new Aabb(this.boundingBox, quad.boundingBox());
        }
    }

    /**
     * Initializes quads for each side of the cube which are needed for ray
     * tracer. The order the quads are defined matters to the ray tracer. If
     * the order is not right, then the cube appears inside out.
     */
    public void initQuads() {
        this.quads = new Quad[6];
        this.quads[0] = new Quad(vertices[0], vertices[3], vertices[1], mat); // front
        this.quads[1] = new Quad(vertices[3], vertices[7], vertices[2], mat); // right
        this.quads[2] = new Quad(vertices[7], vertices[4], vertices[6], mat); // back
        this.quads[3] = new Quad(vertices[4], vertices[0], vertices[5], mat); // left
        this.quads[4] = new Quad(vertices[1], vertices[2], vertices[5], mat); // top
        this.quads[5] = new Quad(vertices[4], vertices[0], vertices[7], mat); // bottom

        this.boundingBox = new Aabb();
        for (Quad quad : this.quads) {
            this.boundingBox = new Aabb(this.boundingBox, quad.boundingBox());
        }
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
