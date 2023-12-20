package floor.planner.models;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import floor.planner.constants.ObjectType;
import floor.planner.util.math.Point3D;
import floor.planner.util.math.Vector;
import floor.planner.util.objects.obj2d.DrawableElement2D;
import floor.planner.util.objects.obj3d.Cube;
import floor.planner.util.objects.obj3d.DrawableElement3D;
import floor.planner.util.raytracer.IntersectableList;

public class Floor {
    private static final Logger logger = LoggerFactory.getLogger(Floor.class);

    private int floorNumber;
    /** The width of a floor. */
    private int width;
    /** The height of the floor. */
    private int height;
    private DrawableElement2D[][][] elements;
    private DrawableElement3D[][][] elements3D;
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
        this.elements = new DrawableElement2D[height][width][];
        this.elements3D = new DrawableElement3D[height][width][];
        this.elementsMatrix = new ObjectType[height][width];
        this.elementColors = new String[height][width];
    }

    public List<float[][]> getFloorTileVertices() {
        return this.floorTileVertices;
    }

    public ObjectType getElementByRowAndCol(int row, int col) {
        return this.elementsMatrix[row][col];
    }

    public int getFloorNumber() {
        return this.floorNumber;
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

    public DrawableElement2D[][][] getElements() {
        return this.elements;
    }
    public void setElements(DrawableElement2D[][][] val) {
        this.elements = val;
    }

    public DrawableElement3D[][][] getElements3D() {
        return this.elements3D;
    }
    public void setElements3D(DrawableElement3D[][][] val) {
        this.elements3D = val;
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

    public void setElement2D(int row, int col, DrawableElement2D[] elements) {
        this.elements[row][col] = elements;
    }

    public void setElement3D(int row, int col, DrawableElement3D[] elements) {
        this.elements3D[row][col] = elements;
    }

    public IntersectableList getIntersectableList() {
        IntersectableList list = new IntersectableList();
        for (int i = 0; i < this.elements3D.length; i++) {
            for (int j = 0; j < this.elements3D[i].length; j++) {
                for (int k = 0; k < this.elements3D[i][j].length; k++) {
                    DrawableElement3D element = this.elements3D[i][j][k];
                    if (element instanceof Cube) {
                        Cube cube = (Cube) element;
                        list.addAll(cube.getIntersectableList());
                    } else {
                        list.add(element);
                    }
                }
            }
        }

        return list;
    }

    public Point3D getMidPoint() {
        int midRow = this.height / 2;
        int midCol = this.width / 2;
        DrawableElement3D[] elements = this.elements3D[midRow][midCol];
        Vector midPoint = new Vector(0, 0, 0);
        for (DrawableElement3D element : elements) {
            midPoint = midPoint.add(element.getMidPoint().getValues());
        }
        midPoint = midPoint.divide(elements.length);
        return new Point3D(midPoint);
    }

    public boolean aboveEquals(int row, int col, ObjectType element) {
        if (row - 1 < 0 || row - 1 > this.elementsMatrix.length) return false;
        if (col < 0 || col > this.elementsMatrix[0].length) return false;
        return this.elementsMatrix[row - 1][col].equals(element);
    }

    public boolean belowEquals(int row, int col, ObjectType element) {
        if (row + 1 < 0 || row + 1 >= this.elementsMatrix.length) return false;
        if (col < 0 || col >= this.elementsMatrix[0].length) return false;
        return this.elementsMatrix[row + 1][col].equals(element);
    }

    public boolean leftEquals(int row, int col, ObjectType element) {
        if (row < 0 || row >= this.elementsMatrix.length) return false;
        if (col - 1 < 0 || col - 1 >= this.elementsMatrix[0].length) return false;
        return this.elementsMatrix[row][col - 1].equals(element);
    }

    public boolean rightEquals(int row, int col, ObjectType element) {
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
                    floorText = floorText + this.elementsMatrix[i][j].value;
                }
            }
            floorText = floorText + "\n";
        }
        return floorText;
    }
}
