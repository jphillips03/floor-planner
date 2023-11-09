package floor.planner.util.jogl.objects.obj3d;

import com.jogamp.opengl.GL2;

import java.util.Arrays;

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

    private float[][] vertices;

    public Cube(float[][] vertices) {
        this.vertices = vertices;
    }

    public float[][] getVertices() {
        return this.vertices;
    }
    public void setVertices(float[][] vertices) {
        this.vertices = vertices;
    }

    public void draw(GL2 gl) {
        int[][] verticesOrder = Cube.VERTICES_ORDER;
        for (int i = 0; i < verticesOrder.length; i++) {
            int[] order = verticesOrder[i];
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
}
