package floor.planner.util.jogl.objects.obj3d;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import floor.planner.constants.Orientation;
import floor.planner.util.jogl.objects.Color;
import floor.planner.util.math.Matrix;

public class Wall3D extends Cube {
    private static final Logger logger = LoggerFactory.getLogger(Wall3D.class);
    private Orientation orientation;
    private int col;
    private int row;

    public Wall3D(float[][] vertices, Orientation orientation, int col, int row) {
        super(vertices);
        this.orientation = orientation;
        this.color = new Color(0, 0, 1);
        this.col = col;
        this.row = row;
        this.scaleCube();
    }

    /**
     * Scale the default 1x1x1 cube vertices down to reduce width of walls
     * depending on orientation.
     */
    private void scaleCube() {
        float[][] vertices = this.getVertices();
        if (this.orientation.equals(Orientation.EAST_WEST)) {
            // shorten y plane and then move to center of row
            vertices = Matrix.scaleY(vertices, 0.05f);
            vertices = Matrix.translateY(vertices, this.row + 0.5f);
        } else if (this.orientation.equals(Orientation.NORTH_SOUTH)) {
            // shorten x plane and then move to center of row
            vertices = Matrix.scaleX(vertices, 0.05f);
            vertices = Matrix.translateX(vertices, this.col + 0.5f);
        } else if (this.orientation.equals(Orientation.COLUMN)) {
            vertices = Matrix.translatePartialY(vertices, 0.45f, Cube.FRONT_FACE);
            vertices = Matrix.translatePartialY(vertices, -0.45f, Cube.BACK_FACE);

            vertices = Matrix.translatePartialX(vertices, 0.45f, Cube.LEFT_FACE);
            vertices = Matrix.translatePartialX(vertices, -0.45f, Cube.RIGHT_FACE);
        }

        // shorten z plane of wall and move it up above floor tile to prevent
        // overlap in rendering (which leads to grainy image)...
        vertices = Matrix.scaleZ(vertices, 0.95f);
        vertices = Matrix.translateZ(vertices, 0.05f);
        this.setVertices(vertices);
    }
    
}
