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
        this.elementsMatrix = new ObjectType[height][width];
        this.elementColors = new String[height][width];
    }

    private void initFloorTileVertices() {
        this.floorTileVertices = new ArrayList<float[][]>();
        for (int i = 0; i < this.height; i++) {
            // int r = this.height - i;
            float[][] vertices = Matrix.translateZ(Cube.DEFAULT_VERTICES, this.floorNumber * 4);
            vertices = Matrix.translateY(vertices, i);
            for (int j = 0; j < this.width; j++) {
                if (!this.elementsMatrix[i][j].equals(ObjectType.HOLE)) {
                    floorTileVertices.add(Matrix.translateX(vertices, j));
                }
            }
        }
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
        this.initFloorTileVertices();
    }

    private void initElements() {
        this.elements = new ArrayList<DrawableElement2D>();
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                int r = this.height - i;

                switch (this.elementsMatrix[i][j]) {
                    case EAST_WEST_WALL:
                        this.elements.add(new Wall(new Point2D(j, r), Orientation.EAST_WEST));
                        break;
                    case NORTH_SOUTH_WALL:
                        this.elements.add(new Wall(new Point2D(j, r), Orientation.NORTH_SOUTH));
                        break;
                    case CORNER_WALL:
                        this.elements.add(new Wall(new Point2D(j, r), Orientation.EAST_WEST));
                        this.elements.add(new Wall(new Point2D(j, r), Orientation.NORTH_SOUTH));
                        break;
                    case WINDOW:
                        if (
                            this.leftEquals(i, j, ObjectType.EAST_WEST_WALL) &&
                            this.rightEquals(i, j, ObjectType.EAST_WEST_WALL)
                        ) {
                            this.elements.add(
                                new Window(
                                    new Point2D(j, r),
                                    Orientation.EAST_WEST,
                                    new Wall(new Point2D(j, r), Orientation.EAST_WEST)
                                )
                            );
                        } else if (
                            this.aboveEquals(i, j, ObjectType.NORTH_SOUTH_WALL) &&
                            this.belowEquals(i, j, ObjectType.NORTH_SOUTH_WALL)
                        ) {
                            this.elements.add(
                                new Window(
                                    new Point2D(j, r),
                                    Orientation.NORTH_SOUTH,
                                    new Wall(new Point2D(j, r), Orientation.NORTH_SOUTH)
                                )
                            );
                        }
                        break;
                    case COLUMN:
                        this.elements.add(new Wall(new Point2D(j, r), Orientation.COLUMN));
                        break;
                    case EAST_WEST_STAIRS:
                        this.elements.add(new Stairs(new Point2D(j, r), Orientation.EAST_WEST));
                        break;
                    case WEST_EAST_STAIRS:
                        this.elements.add(new Stairs(new Point2D(j, r), Orientation.WEST_EAST));
                        break;
                    case NORTH_SOUTH_STAIRS:
                        this.elements.add(new Stairs(new Point2D(j, r), Orientation.NORTH_SOUTH));
                        break;
                    case SOUTH_NORTH_STAIRS:
                        this.elements.add(new Stairs(new Point2D(j, r), Orientation.SOUTH_NORTH));
                        break;
                    case POLE:
                        this.elements.add(new Pole(new Point2D(j, r), null));
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
