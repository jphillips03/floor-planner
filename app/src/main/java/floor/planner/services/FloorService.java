package floor.planner.services;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import floor.planner.constants.ObjectType;
import floor.planner.models.Floor;

public class FloorService {
    private static final Logger logger = LoggerFactory.getLogger(FloorService.class);

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
        String[] floorRows = Arrays.copyOfRange(rows, line, line + height);
        // TODO throw Exceptions instead of returning null...
        if (width < floorRows[0].length() - 1) {
            logger.error("Error parsing floor plan more elements than defined width");
            logger.error("Elements vs defined width: " + (floorRows[0].length() - 1) + " vs " + width);
            return null;
        } else if (width > floorRows[0].length()) {
            logger.error("Error parsing floor plan fewer elements than defined width");
            logger.error("Elements vs defined width: " + (floorRows[0].length() - 1) + " vs " + width);
            return null;
        } else if (height < floorRows.length - 1) {
            logger.error("Error parsing floor plan more elements than defined height");
            logger.error("Elements vs defined height: " + (floorRows.length - 1) + " vs " + height);
            return null;
        } else if (height > floorRows.length) {
            logger.error("Error parsing floor plan fewer elements than defined height");
            logger.error("Elements vs defined height: " + (floorRows.length - 1) + " vs " + height);
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
        ObjectType[][] elements = new ObjectType[height][width];
        String[][] elementColors = new String[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                elements[i][j] = ObjectType.lookup(String.valueOf(rows[line].charAt(j)));
                elementColors[i][j] = "b"; // default to blue
            }
            line++;
        }
        floor.setElementsMatrix(elements);
        floor.setElementColors(elementColors);
        return floor;
    }
}
