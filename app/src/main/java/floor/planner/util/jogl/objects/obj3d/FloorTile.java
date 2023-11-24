package floor.planner.util.jogl.objects.obj3d;

import floor.planner.util.math.Matrix;

public class FloorTile extends Cube {
    
    public FloorTile(float[][] vertices) {
        super(vertices);
        this.scaleCube();
        this.initQuads();
    }

    /**
     * Scale the default 1x1x1 cube vertices down to reduce height of floor
     * tile (scaled down in z direction).
     */
    private void scaleCube() {
        float[][] vertices = this.getVertices();
        vertices = Matrix.translatePartialZ(vertices, -0.95f, TOP_FACE);
        this.setVertices(vertices);
    }
}
