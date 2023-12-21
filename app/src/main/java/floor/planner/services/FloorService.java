package floor.planner.services;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import floor.planner.constants.ObjectType;
import floor.planner.constants.Orientation;
import floor.planner.models.Floor;
import floor.planner.util.math.Matrix;
import floor.planner.util.math.Point2D;
import floor.planner.util.objects.obj2d.Disk;
import floor.planner.util.objects.obj2d.DiskStack;
import floor.planner.util.objects.obj2d.DrawableElement2D;
import floor.planner.util.objects.obj2d.Stairs;
import floor.planner.util.objects.obj2d.Wall;
import floor.planner.util.objects.obj2d.Window;
import floor.planner.util.objects.obj3d.Cube;
import floor.planner.util.objects.obj3d.Cylinder;
import floor.planner.util.objects.obj3d.DrawableElement3D;
import floor.planner.util.objects.obj3d.FloorTile;
import floor.planner.util.objects.obj3d.Sphere;
import floor.planner.util.objects.obj3d.Stairs3D;
import floor.planner.util.objects.obj3d.Wall3D;
import floor.planner.util.objects.obj3d.Window3D;

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
    public Floor createEmptyFloor(int height, int width, int floorNum) {
        String text = "";
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                text += ObjectType.HOLE.value;
            }
            text += "\n";
        }

        return this.createUtil(text.split("\n"), floorNum, 0, height, width);
    }

    private Floor createUtil(String[] rows, int floorNumber, int line, int height, int width) {
        Floor floor = new Floor(floorNumber, height, width);
        
        // init object matrix first so it can be used in logic when drawable
        // elements initialized
        ObjectType[][] elements = this.initElementsMatrix(
            rows,
            floor,
            line,
            height,
            width
        );
        this.initDrawableElements(elements, floor, line, height, width);
        return floor;
    }

    /**
     * Initialize elements matrix by parsing chars in each row of floor.
     * 
     * @param rows
     * @param floor
     * @param line
     * @param height
     * @param width
     */
    private ObjectType[][] initElementsMatrix(
        String[] rows,
        Floor floor,
        int line,
        int height,
        int width
    ) {
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
        return elements;
    }

    private void initDrawableElements(
        ObjectType[][] elements,
        Floor floor,
        int line,
        int height,
        int width
    ) {
        DrawableElement2D[][][] drawableElements2D = new DrawableElement2D[height][width][];
        DrawableElement3D[][][] drawableElements3D = new DrawableElement3D[height][width][];
        for (int i = 0; i < height; i++) {
            int r = height - i;
            float[][] vertices = Matrix.translateZ(Cube.DEFAULT_VERTICES, floor.getFloorNumber() * 4);
            vertices = Matrix.translateY(vertices, r);
            for (int j = 0; j < width; j++) {
                Elements drawabElements = this.getDrawableElements(
                    floor,
                    elements[i][j],
                    i,
                    j,
                    r,
                    vertices
                );
                drawableElements2D[i][j] = drawabElements.elements2D;
                drawableElements3D[i][j] = drawabElements.elements3D;
            }
        }
        floor.setElements(drawableElements2D);
        floor.setElements3D(drawableElements3D);
    }

    private Elements getDrawableElements(
        Floor floor,
        ObjectType objectType,
        int i,
        int j,
        int r,
        float[][] vertices
    ) {
        DrawableElement2D[] elements2D;
        DrawableElement3D[] elements3D;
        switch (objectType) {
            case EAST_WEST_WALL:
                elements2D = new DrawableElement2D[] {
                    new Wall(new Point2D(j, r), Orientation.EAST_WEST)
                };

                elements3D = new DrawableElement3D[] {
                    new FloorTile(Matrix.translateX(vertices, j)),
                    new Wall3D(Matrix.translateX(vertices, j), Orientation.EAST_WEST)
                };
                break;
            case NORTH_SOUTH_WALL:
                elements2D = new DrawableElement2D[] {
                    new Wall(new Point2D(j, r), Orientation.NORTH_SOUTH)
                };

                elements3D = new DrawableElement3D[] {
                    new FloorTile(Matrix.translateX(vertices, j)),
                    new Wall3D(Matrix.translateX(vertices, j), Orientation.NORTH_SOUTH)
                };
                break;
            case CORNER_WALL:
                elements2D = new DrawableElement2D[] {
                    new Wall(new Point2D(j, r), Orientation.EAST_WEST),
                    new Wall(new Point2D(j, r), Orientation.NORTH_SOUTH)
                };

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
                
                elements3D = new DrawableElement3D[] {
                    new FloorTile(Matrix.translateX(vertices, j)),
                    new Wall3D(eastWestVertices, Orientation.EAST_WEST),
                    new Wall3D(northSouthVertices, Orientation.NORTH_SOUTH)
                };
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
                elements2D = new DrawableElement2D[]{
                    new Window(
                        new Point2D(j, r),
                        orientation,
                        new Wall(new Point2D(j, r), orientation)
                    )
                };

                elements3D = new DrawableElement3D[] {
                    new FloorTile(Matrix.translateX(vertices, j)),
                    new Window3D(Matrix.translateX(vertices, j), orientation)
                };
                break;
            case COLUMN:
                elements2D = new DrawableElement2D[]{
                    new Wall(new Point2D(j, r), Orientation.COLUMN)
                };

                elements3D = new DrawableElement3D[] {
                    new FloorTile(Matrix.translateX(vertices, j)),
                    new Wall3D(Matrix.translateX(vertices, j), Orientation.COLUMN)
                };
                break;
            case EAST_WEST_STAIRS:
                elements2D = new DrawableElement2D[]{
                    new Stairs(new Point2D(j, r), Orientation.EAST_WEST)
                };

                elements3D = new DrawableElement3D[] {
                    new FloorTile(Matrix.translateX(vertices, j)),
                    new Stairs3D(Matrix.translateX(vertices, j), Orientation.EAST_WEST)
                };
                break;
            case WEST_EAST_STAIRS:
                elements2D = new DrawableElement2D[]{
                    new Stairs(new Point2D(j, r), Orientation.WEST_EAST)
                };

                elements3D = new DrawableElement3D[] {
                    new FloorTile(Matrix.translateX(vertices, j)),
                    new Stairs3D(Matrix.translateX(vertices, j), Orientation.WEST_EAST)
                };
                break;
            case NORTH_SOUTH_STAIRS:
                elements2D = new DrawableElement2D[]{
                    new Stairs(new Point2D(j, r), Orientation.NORTH_SOUTH)
                };

                elements3D = new DrawableElement3D[] {
                    new FloorTile(Matrix.translateX(vertices, j)),
                    new Stairs3D(Matrix.translateX(vertices, j), Orientation.NORTH_SOUTH)
                };
                break;
            case SOUTH_NORTH_STAIRS:
                elements2D = new DrawableElement2D[]{
                    new Stairs(new Point2D(j, r), Orientation.SOUTH_NORTH)
                };

                elements3D = new DrawableElement3D[] {
                    new FloorTile(Matrix.translateX(vertices, j)),
                    new Stairs3D(Matrix.translateX(vertices, j), Orientation.SOUTH_NORTH)
                };
                break;
            case POLE:
                elements2D = new DrawableElement2D[]{
                    new Disk(new Point2D(j, r), null)
                };

                elements3D = new DrawableElement3D[] {
                    new FloorTile(Matrix.translateX(vertices, j)),
                    new Cylinder(j, r)
                };
                break;
            case HOLE:
                elements2D = new DrawableElement2D[]{};
                elements3D = new DrawableElement3D[]{};
                break;
            case SPHERE:
                elements2D = new DrawableElement2D[]{
                    new DiskStack(new Point2D(j, r), null, 0.25)
                };
                elements3D = new DrawableElement3D[]{
                    new FloorTile(Matrix.translateX(vertices, j)),
                    new Sphere(j, r, 0.5, 0.25)
                };
                break;
            case EMPTY_SPACE:
                elements2D = new DrawableElement2D[]{};
                // only a floor tile should be rendered in 3D
                elements3D = new DrawableElement3D[] {
                    new FloorTile(Matrix.translateX(vertices, j))
                };
                break;
            default:
                elements2D = new DrawableElement2D[]{};
                elements3D = new DrawableElement3D[]{};
                logger.warn("Unknown architectural object found at " + i + " " + j);
                break;
        }

        return new Elements(elements2D, elements3D);
    }

    private class Elements {
        public DrawableElement2D[] elements2D;
        public DrawableElement3D[] elements3D;

        public Elements(
            DrawableElement2D[] elements2D,
            DrawableElement3D[] elements3D
        ) {
            this.elements2D = elements2D;
            this.elements3D = elements3D;
        }
    }

    public void setElement(Floor floor, ObjectType newObjectType, int row, int col) {
        floor.setElement(row, col, newObjectType);

        int r = floor.getHeight() - row;
        float[][] vertices = Matrix.translateZ(Cube.DEFAULT_VERTICES, floor.getFloorNumber() * 4);
        vertices = Matrix.translateY(vertices, r);
        Elements elements = this.getDrawableElements(
            floor,
            newObjectType,
            row,
            col,
            r,
            vertices
        );
        floor.setElement2D(row, col, elements.elements2D);
        floor.setElement3D(row, col, elements.elements3D);
    }
}
