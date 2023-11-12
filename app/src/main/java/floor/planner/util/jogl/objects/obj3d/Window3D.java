package floor.planner.util.jogl.objects.obj3d;

import com.jogamp.opengl.GL2;

import floor.planner.constants.Orientation;
import floor.planner.util.math.Matrix;

/**
 * The Window3D defines properties needed to render a window in 3D. A window
 * is just space between 2 "short" walls. The walls are shortened by translating
 * the bottom or top face (bottom face for top wall; top face for bottom wall).
 * Each face is translated by the amount of the window width (+/- depending on
 * which face is being translated) to create the desired space between the 2
 * walls.
 */
public class Window3D extends Cube {
    private Wall3D topWall;
    private Wall3D bottomWall;
    private float windowWidth = 0.75f;

    public Window3D(float[][] vertices, Orientation orientation) {
        super(vertices);

        float[][] bottomWallVertices = Matrix.translatePartialZ(
            this.getVertices(),
            -this.windowWidth,
            Cube.TOP_FACE
        );
        float[][] topWallVertices = Matrix.translatePartialZ(
            this.getVertices(),
            this.windowWidth,
            Cube.BOTTOM_FACE
        );

        this.bottomWall = new Wall3D(bottomWallVertices, orientation);
        this.topWall = new Wall3D(topWallVertices, orientation);
    }

    @Override
    public void draw(GL2 gl) {
        this.bottomWall.draw(gl);
        this.topWall.draw(gl);
    }
}
