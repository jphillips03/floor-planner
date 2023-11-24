package floor.planner.util.jogl.objects.obj3d;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.opengl.GL2;

import floor.planner.constants.Orientation;
import floor.planner.util.jogl.material.Dielectric;
import floor.planner.util.jogl.material.Lambertian;
import floor.planner.util.jogl.objects.Color;
import floor.planner.util.jogl.raytracer.IntersectableList;
import floor.planner.util.math.Matrix;
import floor.planner.util.math.Vector;

/**
 * The Window3D defines properties needed to render a window in 3D. A window
 * is just space between 2 "short" walls. The walls are shortened by translating
 * the bottom or top face (bottom face for top wall; top face for bottom wall).
 * Each face is translated by the amount of the window width (+/- depending on
 * which face is being translated) to create the desired space between the 2
 * walls.
 */
public class Window3D extends Cube {
    private static Logger logger = LoggerFactory.getLogger(Window3D.class);
    private Wall3D topWall;
    private Wall3D bottomWall;
    private Wall3D window;
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
        float[][] windowVertices = Matrix.translatePartialZ(
            this.getVertices(),
            - (1 - windowWidth),
            TOP_FACE
        );
        windowVertices = Matrix.translatePartialZ(
            windowVertices,
            1 - windowWidth,
            BOTTOM_FACE
        );

        this.materialColor = new float[]{ 0.0f, 0.1f, 0.7f, 1f };
        this.mat = new Lambertian(new Color(this.materialColor));
        this.bottomWall = new Wall3D(bottomWallVertices, orientation, this.mat);
        this.topWall = new Wall3D(topWallVertices, orientation, this.mat);
        this.window = new Wall3D(windowVertices, orientation, new Dielectric(1.55));
    }

    @Override
    public void draw(GL2 gl) {
        this.bottomWall.draw(gl);
        this.topWall.draw(gl);
    }

    @Override
    public IntersectableList getIntersectableList() {
        IntersectableList list = new IntersectableList();
        list.addAll(this.bottomWall.getIntersectableList());
        list.addAll(this.window.getIntersectableList());
        list.addAll(this.topWall.getIntersectableList());
        return list;
    }
}
