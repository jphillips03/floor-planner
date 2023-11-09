package floor.planner.util.jogl.objects.obj3d;

public class Cube {
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
}
