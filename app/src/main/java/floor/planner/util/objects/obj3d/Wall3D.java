package floor.planner.util.objects.obj3d;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import floor.planner.constants.Orientation;
import floor.planner.util.math.Color;
import floor.planner.util.math.Matrix;
import floor.planner.util.raytracer.material.Lambertian;
import floor.planner.util.raytracer.material.Material;

public class Wall3D extends Cube {
    private static final Logger logger = LoggerFactory.getLogger(Wall3D.class);
    private Orientation orientation;

    public Wall3D(float[][] vertices, Orientation orientation) {
        super(vertices);
        this.orientation = orientation;
        this.scaleCube();
        this.mat = new Lambertian(this.color);
        this.initQuads();
    }

    public Wall3D(float[][] vertices, Orientation orientation, Material mat) {
        super(vertices);
        this.orientation = orientation;
        this.scaleCube();
        this.mat = mat;
        this.initQuads();
    }

    /**
     * Scale the default 1x1x1 cube vertices down to reduce width of walls
     * depending on orientation.
     */
    private void scaleCube() {
        float[][] vertices = this.getVertices();
        if (this.orientation.equals(Orientation.EAST_WEST)) {
            vertices = Matrix.translatePartialY(vertices, 0.45f, FRONT_FACE);
            vertices = Matrix.translatePartialY(vertices, -0.45f, BACK_FACE);
        } else if (this.orientation.equals(Orientation.NORTH_SOUTH)) {
            vertices = Matrix.translatePartialX(vertices, 0.45f, LEFT_FACE);
            vertices = Matrix.translatePartialX(vertices, -0.45f, RIGHT_FACE);
        } else if (this.orientation.equals(Orientation.COLUMN)) {
            vertices = Matrix.translatePartialY(vertices, 0.45f, FRONT_FACE);
            vertices = Matrix.translatePartialY(vertices, -0.45f, BACK_FACE);

            vertices = Matrix.translatePartialX(vertices, 0.45f, LEFT_FACE);
            vertices = Matrix.translatePartialX(vertices, -0.45f, RIGHT_FACE);

            this.materialColor = new float[]{ 0.0f, 0.5f, 0.7f, 1f };
        }

        // shorten z plane of wall and move it up above floor tile to prevent
        // overlap in rendering (which leads to grainy image)...
        vertices = Matrix.translatePartialZ(vertices, 0.05f, BOTTOM_FACE);
        this.setVertices(vertices);
    }
    
}
