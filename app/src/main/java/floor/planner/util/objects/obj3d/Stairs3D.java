package floor.planner.util.objects.obj3d;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.opengl.GL2;

import floor.planner.constants.Orientation;
import floor.planner.util.math.Color;
import floor.planner.util.math.Matrix;
import floor.planner.util.raytracer.intersectable.IntersectableList;
import floor.planner.util.raytracer.material.Lambertian;

/**
 * The Stairs3D defines properties and methods needed to render stairs in 3D.
 * Stairs in 3D are represented by a list of cubes (one for each step).
 */
public class Stairs3D extends Cube {
    private static final Logger logger = LoggerFactory.getLogger(Wall3D.class);
    private Orientation orientation;
    /** List of cubes that represent each stair in list of stairs. */
    List<Cube> stairs;

    @Override
    public void setColor(Color c) {
        for (Cube s : stairs) {
            s.setColor(c);
        }
    }

    public Stairs3D(float[][] vertices, Orientation orientation) {
        super(vertices);
        this.orientation = orientation;
        this.mat = new Lambertian(this.color);
        this.initStairs();
    }

    /**
     * Initializes the list of cubes to be rendered for each stair.
     */
    private void initStairs() {
        //this.color = new Color(0, 0, 1f);
        this.stairs = new ArrayList<Cube>();
        if (this.orientation.equals(Orientation.EAST_WEST)) {
            for (float i = 0; i <= 0.875; i += 0.125) {
                float[][] vertices = Matrix.copy(this.getVertices());
                vertices = reduceWidth(vertices);
                vertices = moveX(vertices, 1 - (i + 0.125f), -i);
                vertices = moveUp(vertices, i);

                Cube cube = new Cube(vertices);
                cube.color = this.color;
                cube.materialColor = this.materialColor;
                cube.mat = this.mat;
                cube.initQuads();
                this.stairs.add(cube);
            }
        } else if (this.orientation.equals(Orientation.WEST_EAST)) {
            for (float i = 0; i <= 0.875; i += 0.125) {
                float[][] vertices = Matrix.copy(this.getVertices());
                vertices = reduceWidth(vertices);
                vertices = moveX(vertices, i, (i + 0.125f) - 1);
                vertices = moveUp(vertices, i);

                Cube cube = new Cube(vertices);
                cube.color = this.color;
                cube.materialColor = this.materialColor;
                cube.mat = this.mat;
                cube.initQuads();
                this.stairs.add(cube);
            }
        } else if (this.orientation.equals(Orientation.NORTH_SOUTH)) {
            for (float i = 0; i <= 0.875; i += 0.125) {
                float[][] vertices = Matrix.copy(this.getVertices());
                vertices = reduceWidth(vertices);
                vertices = moveY(vertices, i, (i + 0.125f) - 1);
                vertices = moveUp(vertices, i);

                Cube cube = new Cube(vertices);
                cube.color = this.color;
                cube.materialColor = this.materialColor;
                cube.mat = this.mat;
                cube.initQuads();
                this.stairs.add(cube);
            }
        } else if (this.orientation.equals(Orientation.SOUTH_NORTH)) {
            for (float i = 0; i <= 0.875; i += 0.125) {
                float[][] vertices = Matrix.copy(this.getVertices());
                vertices = reduceWidth(vertices);
                vertices = moveY(vertices, 1 - (i + 0.125f), -i);
                vertices = moveUp(vertices, i);

                Cube cube = new Cube(vertices);
                cube.color = this.color;
                cube.materialColor = this.materialColor;
                cube.mat = this.mat;
                cube.initQuads();
                this.stairs.add(cube);
            }
        }
    }

    /**
     * Reduces the width of the stair so there is space around them based on
     * orientation of stairs (there should be space in Y plane for east-west
     * and west-east; thre should be space in X plane for north-south and
     * south-north).
     *
     * @param vertices Vertices of a single stair.
     * @return Updated vertices with width reduced.
     */
    private float[][] reduceWidth(float[][] vertices) {
        if (this.orientation.equals(Orientation.EAST_WEST) || this.orientation.equals(Orientation.WEST_EAST)) {
            vertices = Matrix.translatePartialY(vertices, 0.25f, FRONT_FACE);
            vertices = Matrix.translatePartialY(vertices, -0.25f, BACK_FACE);
        } else {
            vertices = Matrix.translatePartialX(vertices, 0.25f, LEFT_FACE);
            vertices = Matrix.translatePartialX(vertices, -0.25f, RIGHT_FACE);
        }
        return vertices;
    }

    /**
     * Moves the stair up (in the Z plane) based on given index. The index is
     * used to determine how much the top and bottom faces should be moved. The
     * top face should move down from the current index (1 plus the height of a
     * stair; which is 0.125f), and the bottom face should shift up by the
     * amount of the index. 
     *
     * @param vertices Vertices of a single stair.
     * @param i The index of the current stair.
     * @return Updated vertices moved up (in the Z plane).
     */
    private float[][] moveUp(float[][] vertices, float i) {
        // shift top and bottom faces accordingly
        vertices = Matrix.translatePartialZ(vertices, i - 1 + 0.125f, TOP_FACE);
        vertices = Matrix.translatePartialZ(vertices, i, BOTTOM_FACE);

        // shift entire stair up; ensure bottom stair is drawn above floor tile
        vertices = Matrix.translateZ(vertices, 0.05f);
        return vertices;
    }

    /**
     * Moves the stair in the X plane by the given left and right deltas. The
     * left delta shits the left face and the right delta shifts the right face
     * of the stair. This is used for east-west and west-east stairs.
     *
     * @param vertices Vertices of a single stair.
     * @param leftDelta The amount to shift the left face.
     * @param rightDelta The amount to shift the right face.
     * @return Updated vertices moved in the X plane.
     */
    private float[][] moveX(float[][] vertices, float leftDelta, float rightDelta) {
        vertices = Matrix.translatePartialX(vertices, leftDelta, LEFT_FACE);
        vertices = Matrix.translatePartialX(vertices, rightDelta, RIGHT_FACE);
        return vertices;
    }

    /**
     * Moves the stair in the Y plane by the given front and back deltas. The
     * front delta shifts the front face and the back delta shifts the back
     * face of the stair. This is used for north-south and south-north stairs.
     *
     * @param vertices Vertices of a single stair.
     * @param frontDelta The amount to shift the front face.
     * @param backDelta The amount to shift the back face.
     * @return Updated vertices moved in the Y plane.
     */
    private float[][] moveY(float[][] vertices, float frontDelta, float backDelta) {
        vertices = Matrix.translatePartialY(vertices, frontDelta, FRONT_FACE);
        vertices = Matrix.translatePartialY(vertices, backDelta, BACK_FACE);
        return vertices;
    }

    @Override
    public void draw(GL2 gl) {
        for (Cube stair : this.stairs) {
            stair.draw(gl);
        }
    }

    @Override
    public IntersectableList getIntersectableList() {
        IntersectableList list = new IntersectableList();
        for (Cube stair : this.stairs) {
            list.addAll(stair.getIntersectableList());
        }

        return list;
    }
}
