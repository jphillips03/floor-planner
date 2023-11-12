package floor.planner.models;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import floor.planner.constants.ObjectType;
import floor.planner.constants.Orientation;
import floor.planner.util.jogl.objects.obj2d.DrawableElement2D;
import floor.planner.util.jogl.objects.obj2d.Pole;
import floor.planner.util.jogl.objects.obj2d.Stairs;
import floor.planner.util.jogl.objects.obj2d.Wall;
import floor.planner.util.jogl.objects.obj2d.Window;
import floor.planner.util.jogl.objects.obj3d.Cube;
import floor.planner.util.jogl.objects.obj3d.Cylinder;
import floor.planner.util.jogl.objects.obj3d.DrawableElement3D;
import floor.planner.util.jogl.objects.obj3d.FloorTile;
import floor.planner.util.jogl.objects.obj3d.Stairs3D;
import floor.planner.util.jogl.objects.obj3d.Wall3D;
import floor.planner.util.jogl.objects.obj3d.Window3D;
import floor.planner.util.math.Matrix;
import floor.planner.util.math.Point2D;

public class Floor {
    private static final Logger logger = LoggerFactory.getLogger(Floor.class);

    private int floorNumber;
    /** The width of a floor. */
    private int width;
    /** The height of the floor. */
    private int height;
    private List<DrawableElement2D> elements;
    private List<DrawableElement3D> elements3D;
    /** The current matrix of elements that make up the floor. */
    private ObjectType[][] elementsMatrix;
    /** The colors in this floor. */
    private String[][] elementColors;
    private List<float[][]> floorTileVertices;

    /**
     * Constructor for the Floor class. Creates a matrix for a 
     * particular floor in the floor plan.
     *
     * @param width
     * @param height
     */
    public Floor(int floorNumber, int height, int width) {
        this.floorNumber = floorNumber;
        this.width = width;
        this.height = height;
        this.elements = new ArrayList<DrawableElement2D>();
        this.elements3D = new ArrayList<DrawableElement3D>();
        this.elementsMatrix = new ObjectType[height][width];
        this.elementColors = new String[height][width];
    }

    public List<float[][]> getFloorTileVertices() {
        return this.floorTileVertices;
    }

    public ObjectType getElementByRowAndCol(int row, int col) {
        return this.elementsMatrix[row][col];
    }

    /**
     * Returns this floors height.
     *
     * @return This floors height.
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Returns this floors width.
     *
     * @return This floors width.
     */
    public int getWidth() {
        return this.width;
    }

    public List<DrawableElement2D> getElements() {
        return this.elements;
    }
    public void setElements(List<DrawableElement2D> val) {
        this.elements = val;
    }

    public List<DrawableElement3D> getElements3D() {
        return this.elements3D;
    }

    /**
     * Returns the matrix representation of this floor, containing all the 
     * elements.
     *
     * @return The matrix of this floor.
     */
    public ObjectType[][] getElementsMatrix() {
        return this.elementsMatrix;
    }
    public void setElementsMatrix(ObjectType[][] val) {
        this.elementsMatrix = val;
        this.initElements();
    }

    private void initElements() {
        this.elements = new ArrayList<DrawableElement2D>();
        this.elements3D = new ArrayList<DrawableElement3D>();
        for (int i = 0; i < this.height; i++) {
            int r = this.height - i;
            float[][] vertices = Matrix.translateZ(Cube.DEFAULT_VERTICES, this.floorNumber * 4);
            vertices = Matrix.translateY(vertices, r);
            for (int j = 0; j < this.width; j++) {
                switch (this.elementsMatrix[i][j]) {
                    case EAST_WEST_WALL:
                        this.elements.add(new Wall(new Point2D(j, r), Orientation.EAST_WEST));

                        // add 3D elements
                        this.elements3D.add(new FloorTile(Matrix.translateX(vertices, j)));
                        this.elements3D.add(new Wall3D(Matrix.translateX(vertices, j), Orientation.EAST_WEST));
                        break;
                    case NORTH_SOUTH_WALL:
                        this.elements.add(new Wall(new Point2D(j, r), Orientation.NORTH_SOUTH));

                        // add 3D elements
                        this.elements3D.add(new FloorTile(Matrix.translateX(vertices, j)));
                        this.elements3D.add(new Wall3D(Matrix.translateX(vertices, j), Orientation.NORTH_SOUTH));
                        break;
                    case CORNER_WALL:
                        this.elements.add(new Wall(new Point2D(j, r), Orientation.EAST_WEST));
                        this.elements.add(new Wall(new Point2D(j, r), Orientation.NORTH_SOUTH));

                        // add 3D elements
                        // shrink appropriate faces so no part of all extends beyond corner
                        float[][] eastWestVertices = Matrix.translateX(vertices, j);
                        float[][] northSouthVertices = Matrix.translateX(vertices, j);
                        if (belowEquals(i, j, ObjectType.NORTH_SOUTH_WALL) && rightEquals(i, j, ObjectType.EAST_WEST_WALL)) {
                            // north east corner
                            eastWestVertices = Matrix.translatePartialX(eastWestVertices, 0.45f, Cube.LEFT_FACE);
                            northSouthVertices = Matrix.translatePartialY(northSouthVertices, -0.45f, Cube.BACK_FACE);
                        } else if (leftEquals(i, j, ObjectType.EAST_WEST_WALL) && belowEquals(i, j, ObjectType.NORTH_SOUTH_WALL)) {
                            // north west corner
                            eastWestVertices = Matrix.translatePartialX(eastWestVertices, -0.45f, Cube.RIGHT_FACE);
                            northSouthVertices = Matrix.translatePartialY(northSouthVertices, -0.45f, Cube.BACK_FACE);
                        } else if (rightEquals(i, j, ObjectType.EAST_WEST_WALL) && aboveEquals(i, j, ObjectType.NORTH_SOUTH_WALL)) {
                            // south east corner
                            eastWestVertices = Matrix.translatePartialX(eastWestVertices, 0.45f, Cube.LEFT_FACE);
                            northSouthVertices = Matrix.translatePartialY(northSouthVertices, 0.45f, Cube.FRONT_FACE);
                        } else {
                            // south west corner
                            eastWestVertices = Matrix.translatePartialX(eastWestVertices, -0.45f, Cube.RIGHT_FACE);
                            northSouthVertices = Matrix.translatePartialY(northSouthVertices, 0.45f, Cube.FRONT_FACE);
                        }
                        
                        this.elements3D.add(new Wall3D(eastWestVertices, Orientation.EAST_WEST));
                        this.elements3D.add(new Wall3D(northSouthVertices, Orientation.NORTH_SOUTH));
                        this.elements3D.add(new FloorTile(Matrix.translateX(vertices, j)));
                        break;
                    case WINDOW:
                        Orientation orientation = Orientation.EAST_WEST;
                        if (
                            this.leftEquals(i, j, ObjectType.EAST_WEST_WALL) &&
                            this.rightEquals(i, j, ObjectType.EAST_WEST_WALL)
                        ) {
                            orientation = Orientation.EAST_WEST;
                        } else if (
                            this.aboveEquals(i, j, ObjectType.NORTH_SOUTH_WALL) &&
                            this.belowEquals(i, j, ObjectType.NORTH_SOUTH_WALL)
                        ) {
                            orientation = Orientation.NORTH_SOUTH;
                        }
                        this.elements.add(
                            new Window(
                                new Point2D(j, r),
                                orientation,
                                new Wall(new Point2D(j, r), orientation)
                            )
                        );

                        // add 3D elements
                        this.elements3D.add(new FloorTile(Matrix.translateX(vertices, j)));
                        this.elements3D.add(new Window3D(Matrix.translateX(vertices, j), orientation));
                        break;
                    case COLUMN:
                        this.elements.add(new Wall(new Point2D(j, r), Orientation.COLUMN));

                        // add 3D elements
                        this.elements3D.add(new FloorTile(Matrix.translateX(vertices, j)));
                        this.elements3D.add(new Wall3D(Matrix.translateX(vertices, j), Orientation.COLUMN));
                        break;
                    case EAST_WEST_STAIRS:
                        this.elements.add(new Stairs(new Point2D(j, r), Orientation.EAST_WEST));

                        // add 3D elements
                        this.elements3D.add(new FloorTile(Matrix.translateX(vertices, j)));
                        this.elements3D.add(new Stairs3D(Matrix.translateX(vertices, j), Orientation.EAST_WEST));
                        break;
                    case WEST_EAST_STAIRS:
                        this.elements.add(new Stairs(new Point2D(j, r), Orientation.WEST_EAST));

                        // add 3D elements
                        this.elements3D.add(new FloorTile(Matrix.translateX(vertices, j)));
                        this.elements3D.add(new Stairs3D(Matrix.translateX(vertices, j), Orientation.WEST_EAST));
                        break;
                    case NORTH_SOUTH_STAIRS:
                        this.elements.add(new Stairs(new Point2D(j, r), Orientation.NORTH_SOUTH));

                        // add 3D elements
                        this.elements3D.add(new FloorTile(Matrix.translateX(vertices, j)));
                        this.elements3D.add(new Stairs3D(Matrix.translateX(vertices, j), Orientation.NORTH_SOUTH));
                        break;
                    case SOUTH_NORTH_STAIRS:
                        this.elements.add(new Stairs(new Point2D(j, r), Orientation.SOUTH_NORTH));

                        // add 3D elements
                        this.elements3D.add(new FloorTile(Matrix.translateX(vertices, j)));
                        this.elements3D.add(new Stairs3D(Matrix.translateX(vertices, j), Orientation.SOUTH_NORTH));
                        break;
                    case POLE:
                        this.elements.add(new Pole(new Point2D(j, r), null));

                        // add 3D elements
                        this.elements3D.add(new FloorTile(Matrix.translateX(vertices, j)));
                        this.elements3D.add(new Cylinder(j, r));
                        break;
                    case HOLE:
                        // nothing should be rendered
                        break;
                    case EMPTY_SPACE:
                        // only a floor tile should be rendered in 3D
                        this.elements3D.add(new FloorTile(Matrix.translateX(vertices, j)));
                        break;
                    default:
                        logger.warn("Unknown architectural object found at " + i + " " + j);
                        break;
                }
            }
        }
    }

    /**
     * Returns the matrix representation of the colors for this floor.
     *
     * @return The matrix representation of the colors for this floor.
     */
    public String[][] getElementColors() {
        return this.elementColors;
    }
    public void setElementColors(String[][] val) {
        this.elementColors = val;
    }

    /**
     * Sets the new color c at the given row and column.
     *
     * @param row The row to change.
     * @param col The column to change.
     * @param c The new color.
     */
    public void setColor(int row, int col, String c) {
        this.elementColors[row][col] = c;
    }

    public void setElement(int row, int col, ObjectType element) {
        this.elementsMatrix[row][col] = element;
    }

    private boolean aboveEquals(int row, int col, ObjectType element) {
        if (row - 1 < 0 || row - 1 > this.elementsMatrix.length) return false;
        if (col < 0 || col > this.elementsMatrix[0].length) return false;
        return this.elementsMatrix[row - 1][col].equals(element);
    }

    private boolean belowEquals(int row, int col, ObjectType element) {
        if (row + 1 < 0 || row + 1 >= this.elementsMatrix.length) return false;
        if (col < 0 || col >= this.elementsMatrix[0].length) return false;
        return this.elementsMatrix[row + 1][col].equals(element);
    }

    private boolean leftEquals(int row, int col, ObjectType element) {
        if (row < 0 || row >= this.elementsMatrix.length) return false;
        if (col - 1 < 0 || col - 1 >= this.elementsMatrix[0].length) return false;
        return this.elementsMatrix[row][col - 1].equals(element);
    }

    private boolean rightEquals(int row, int col, ObjectType element) {
        if (row < 0 || row >= this.elementsMatrix.length) return false;
        if (col + 1 < 0 || col + 1 >= this.elementsMatrix[0].length) return false;
        return this.elementsMatrix[row][col + 1].equals(element);
    }

    /**
     * Converts the current floor of the floor plan into a string. This is 
     * convenient for saving the floor plan.
     */
    public String toString() {
        String floorText = "";
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                if(this.elementsMatrix[i][j] == null) {
                    floorText = floorText + "o";
                }
                else {
                    floorText = floorText + this.elementsMatrix[i][j];
                }
            }
            floorText = floorText + "\n";
        }
        return floorText;
    }
}
