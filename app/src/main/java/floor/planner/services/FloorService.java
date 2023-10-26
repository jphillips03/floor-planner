package floor.planner.services;

import floor.planner.models.Floor;

public class FloorService {
    /**
     * Creates and returns a new Floor using the given text, height, and width,
     * starting at the given line in the text (based on a split on "\n"). The
     * given text is the text for the entire floor plan, which is why we need
     * the line to start at for the floor.
     *
     * @param text The text for the entire floor plan.
     * @param line The line in the text where the floor starts.
     * @param height The height of the floor.
     * @param width The width of the floor.
     * @return A Floor based on given text starting at the given line.
     */
    public Floor create(String text, int line, int height, int width) {
        String[] rows = text.split("\n");
        if (rows[0].length() - 1 > width || width < rows[0].length() - 1) {
            return null;
        } else if (rows.length/2 > height || height < rows.length/2) {
            return null;
        }

        return this.createUtil(rows, line, height, width);
    }

    /**
     * Creates and returns a new empty Floor with given height and width.
     *
     * @param height The 
     * @param width
     * @return
     */
    public Floor createEmptyFloor(int height, int width) {
        String text = "";
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                text += "o";
            }
            text += "\n";
        }

        return this.createUtil(text.split("\n"), width, height, width);
    }

    private Floor createUtil(String[] rows, int line, int height, int width) {
        Floor floor = new Floor(height, width);
        String[][] elements = new String[height][width];
        String[][] elementColors = new String[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                elements[i][j] = "" + rows[line].charAt(j);
                elementColors[i][j] = "b"; // default to blue
            }
            line++;
        }
        floor.setElements(elements);
        floor.setElementColors(elementColors);
        return floor;
    }
}
