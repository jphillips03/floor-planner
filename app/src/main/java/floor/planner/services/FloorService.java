package floor.planner.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import floor.planner.constants.ObjectType;
import floor.planner.constants.Orientation;
import floor.planner.models.Floor;
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
    public Floor create(String text, int floorNumber, int line, int height, int width) {
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

        return this.createUtil(rows, floorNumber, line, height, width);
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

        return this.createUtil(text.split("\n"), 0, width, height, width);
    }

    private Floor createUtil(String[] rows, int floorNumber, int line, int height, int width) {
        Floor floor = new Floor(floorNumber, height, width);
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
        this.initElements(floor);
        return floor;
    }

    private void initElements(Floor floor) {
        List<DrawableElement2D> elements = new ArrayList<DrawableElement2D>();
        List<DrawableElement3D> elements3D = new ArrayList<DrawableElement3D>();
        for (int i = 0; i < floor.getHeight(); i++) {
            int r = floor.getHeight() - i;
            float[][] vertices = Matrix.translateZ(Cube.DEFAULT_VERTICES, floor.getFloorNumber() * 4);
            vertices = Matrix.translateY(vertices, r);
            for (int j = 0; j < floor.getWidth(); j++) {
                switch (floor.getElementsMatrix()[i][j]) {
                    case EAST_WEST_WALL:
                        elements.add(new Wall(new Point2D(j, r), Orientation.EAST_WEST));

                        // add 3D elements
                        elements3D.add(new FloorTile(Matrix.translateX(vertices, j)));
                        elements3D.add(new Wall3D(Matrix.translateX(vertices, j), Orientation.EAST_WEST));
                        break;
                    case NORTH_SOUTH_WALL:
                        elements.add(new Wall(new Point2D(j, r), Orientation.NORTH_SOUTH));

                        // add 3D elements
                        elements3D.add(new FloorTile(Matrix.translateX(vertices, j)));
                        elements3D.add(new Wall3D(Matrix.translateX(vertices, j), Orientation.NORTH_SOUTH));
                        break;
                    case CORNER_WALL:
                        elements.add(new Wall(new Point2D(j, r), Orientation.EAST_WEST));
                        elements.add(new Wall(new Point2D(j, r), Orientation.NORTH_SOUTH));

                        // add 3D elements
                        // shrink appropriate faces so no part of all extends beyond corner
                        float[][] eastWestVertices = Matrix.translateX(vertices, j);
                        float[][] northSouthVertices = Matrix.translateX(vertices, j);
                        if (floor.belowEquals(i, j, ObjectType.NORTH_SOUTH_WALL) && floor.rightEquals(i, j, ObjectType.EAST_WEST_WALL)) {
                            // north east corner
                            eastWestVertices = Matrix.translatePartialX(eastWestVertices, 0.45f, Cube.LEFT_FACE);
                            northSouthVertices = Matrix.translatePartialY(northSouthVertices, -0.45f, Cube.BACK_FACE);
                        } else if (floor.leftEquals(i, j, ObjectType.EAST_WEST_WALL) && floor.belowEquals(i, j, ObjectType.NORTH_SOUTH_WALL)) {
                            // north west corner
                            eastWestVertices = Matrix.translatePartialX(eastWestVertices, -0.45f, Cube.RIGHT_FACE);
                            northSouthVertices = Matrix.translatePartialY(northSouthVertices, -0.45f, Cube.BACK_FACE);
                        } else if (floor.rightEquals(i, j, ObjectType.EAST_WEST_WALL) && floor.aboveEquals(i, j, ObjectType.NORTH_SOUTH_WALL)) {
                            // south east corner
                            eastWestVertices = Matrix.translatePartialX(eastWestVertices, 0.45f, Cube.LEFT_FACE);
                            northSouthVertices = Matrix.translatePartialY(northSouthVertices, 0.45f, Cube.FRONT_FACE);
                        } else {
                            // south west corner
                            eastWestVertices = Matrix.translatePartialX(eastWestVertices, -0.45f, Cube.RIGHT_FACE);
                            northSouthVertices = Matrix.translatePartialY(northSouthVertices, 0.45f, Cube.FRONT_FACE);
                        }
                        
                        elements3D.add(new Wall3D(eastWestVertices, Orientation.EAST_WEST));
                        elements3D.add(new Wall3D(northSouthVertices, Orientation.NORTH_SOUTH));
                        elements3D.add(new FloorTile(Matrix.translateX(vertices, j)));
                        break;
                    case WINDOW:
                        Orientation orientation = Orientation.EAST_WEST;
                        if (
                            floor.leftEquals(i, j, ObjectType.EAST_WEST_WALL) &&
                            floor.rightEquals(i, j, ObjectType.EAST_WEST_WALL)
                        ) {
                            orientation = Orientation.EAST_WEST;
                        } else if (
                            floor.aboveEquals(i, j, ObjectType.NORTH_SOUTH_WALL) &&
                            floor.belowEquals(i, j, ObjectType.NORTH_SOUTH_WALL)
                        ) {
                            orientation = Orientation.NORTH_SOUTH;
                        }
                        elements.add(
                            new Window(
                                new Point2D(j, r),
                                orientation,
                                new Wall(new Point2D(j, r), orientation)
                            )
                        );

                        // add 3D elements
                        elements3D.add(new FloorTile(Matrix.translateX(vertices, j)));
                        elements3D.add(new Window3D(Matrix.translateX(vertices, j), orientation));
                        break;
                    case COLUMN:
                        elements.add(new Wall(new Point2D(j, r), Orientation.COLUMN));

                        // add 3D elements
                        elements3D.add(new FloorTile(Matrix.translateX(vertices, j)));
                        elements3D.add(new Wall3D(Matrix.translateX(vertices, j), Orientation.COLUMN));
                        break;
                    case EAST_WEST_STAIRS:
                        elements.add(new Stairs(new Point2D(j, r), Orientation.EAST_WEST));

                        // add 3D elements
                        elements3D.add(new FloorTile(Matrix.translateX(vertices, j)));
                        elements3D.add(new Stairs3D(Matrix.translateX(vertices, j), Orientation.EAST_WEST));
                        break;
                    case WEST_EAST_STAIRS:
                        elements.add(new Stairs(new Point2D(j, r), Orientation.WEST_EAST));

                        // add 3D elements
                        elements3D.add(new FloorTile(Matrix.translateX(vertices, j)));
                        elements3D.add(new Stairs3D(Matrix.translateX(vertices, j), Orientation.WEST_EAST));
                        break;
                    case NORTH_SOUTH_STAIRS:
                        elements.add(new Stairs(new Point2D(j, r), Orientation.NORTH_SOUTH));

                        // add 3D elements
                        elements3D.add(new FloorTile(Matrix.translateX(vertices, j)));
                        elements3D.add(new Stairs3D(Matrix.translateX(vertices, j), Orientation.NORTH_SOUTH));
                        break;
                    case SOUTH_NORTH_STAIRS:
                        elements.add(new Stairs(new Point2D(j, r), Orientation.SOUTH_NORTH));

                        // add 3D elements
                        elements3D.add(new FloorTile(Matrix.translateX(vertices, j)));
                        elements3D.add(new Stairs3D(Matrix.translateX(vertices, j), Orientation.SOUTH_NORTH));
                        break;
                    case POLE:
                        elements.add(new Pole(new Point2D(j, r), null));

                        // add 3D elements
                        elements3D.add(new FloorTile(Matrix.translateX(vertices, j)));
                        elements3D.add(new Cylinder(j, r));
                        break;
                    case HOLE:
                        // nothing should be rendered
                        break;
                    case EMPTY_SPACE:
                        // only a floor tile should be rendered in 3D
                        elements3D.add(new FloorTile(Matrix.translateX(vertices, j)));
                        break;
                    default:
                        logger.warn("Unknown architectural object found at " + i + " " + j);
                        break;
                }
            }
        }

        // update the elements for the floor
        floor.setElements(elements);
        floor.setElements3D(elements3D);
    }
}
