package floor.planner.models;

public class Floor {
    /** The width of a floor. */
    private int width;
    /** The height of the floor. */
    private int height;
    /** The current matrix of elements that make up the floor. */
    private String[][] elements;
    /** The colors in this floor. */
    private String[][] elementColors;

    /**
     * Constructor for the Floor class. Creates a matrix for a 
     * particular floor in the floor plan.
     *
     * @param width
     * @param height
     */
    public Floor(int height, int width) {
        this.width = width;
        this.height = height;
        this.elements = new String[height][width];
        this.elementColors = new String[height][width];
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

    /**
     * Returns the matrix representation of this floor, containing all the 
     * elements.
     *
     * @return The matrix of this floor.
     */
    public String[][] getElements() {
        return this.elements;
    }
    public void setElements(String[][] val) {
        this.elements = val;
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

    public void setElement(int row, int col, String element) {
        this.elements[row][col] = element;
    }

    private boolean aboveEquals(int row, int col, String element) {
        return this.elements[row - 1][col].equals(element);
    }

    private boolean belowEquals(int row, int col, String element) {
        return this.elements[row + 1][col].equals(element);
    }

    private boolean leftEquals(int row, int col, String element) {
        return this.elements[row][col - 1].equals(element);
    }

    private boolean rightEquals(int row, int col, String element) {
        return this.elements[row][col + 1].equals(element);
    }

    /**
     * Converts the current floor of the floor plan into a string. This is 
     * convenient for saving the floor plan.
     */
    public String toString() {
        String floorText = "";
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                if(this.elements[i][j] == null) {
                    floorText = floorText + "o";
                }
                else {
                    floorText = floorText + this.elements[i][j];
                }
            }
            floorText = floorText + "\n";
        }
        return floorText;
    }
}
