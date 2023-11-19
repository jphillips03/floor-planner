package floor.planner.util.jogl.objects.obj3d;

import com.jogamp.opengl.GL2;

import floor.planner.util.jogl.objects.Color;
import floor.planner.util.jogl.raytracer.IntersectRecord;
import floor.planner.util.math.Ray;
import floor.planner.util.math.Vector;

import java.util.Arrays;
import java.util.List;

public class Cube extends DrawableElement3D {
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

    public Cube(float[][] vertices) {
        this.vertices = vertices;

        // default to red for now...
        this.color = new Color(1f, 0f, 0f);
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
            float[] normal = Vector.normal(
                vertices[order[0]],
                vertices[order[1]],
                vertices[order[2]],
                vertices[order[3]]
            );
            gl.glNormal3f(normal[0], normal[1], normal[2]);
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
        float tMinRay,
        float tMaxRay,
        IntersectRecord rec
    ) {
        return false;
    }
}
