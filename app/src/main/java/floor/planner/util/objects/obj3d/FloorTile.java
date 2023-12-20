package floor.planner.util.objects.obj3d;

import floor.planner.util.math.Color;
import floor.planner.util.math.Matrix;
import floor.planner.util.raytracer.material.Lambertian;

public class FloorTile extends Cube {
    
    public FloorTile(float[][] vertices) {
        super(vertices);
        this.scaleCube();

        // default grey
        this.mat = new Lambertian(new Color(this.materialColor));
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
