package floor.planner.models.twodelements;

import java.util.Arrays;

import floor.planner.constants.EastWestWallOffset2D;
import floor.planner.constants.NorthSouthWallOffset2D;
import floor.planner.constants.Orientation;
import floor.planner.abstractClasses.DrawableElement;
import floor.planner.models.Point2D;

public class Wall extends DrawableElement {
    public Wall(Point2D point, Orientation orientation) {
        super(point, orientation);
    }

    public void initPoints() {
        if (this.orientation == Orientation.EAST_WEST) {
            this.points = Arrays.asList(
                new Point2D(
                    this.point.getX() + 1,
                    this.point.getY() + EastWestWallOffset2D.TOP.value
                ),
                new Point2D(
                    this.point.getX() + 1,
                    this.point.getY() + EastWestWallOffset2D.BOTTOM.value
                ),
                new Point2D(
                    this.point.getX(),
                    this.point.getY() + EastWestWallOffset2D.BOTTOM.value
                ),
                new Point2D(
                    this.point.getX(),
                    this.point.getY() + EastWestWallOffset2D.TOP.value
                )
            );
        } else if (this.orientation == Orientation.NORTH_SOUTH) {
            this.points = Arrays.asList(
                new Point2D(
                    this.point.getX() + NorthSouthWallOffset2D.LEFT.value,
                    this.point.getY() + 1
                ),
                new Point2D(
                    this.point.getX() + NorthSouthWallOffset2D.RIGHT.value,
                    this.point.getY() + 1
                ),
                new Point2D(
                    this.point.getX() + NorthSouthWallOffset2D.RIGHT.value,
                    this.point.getY()
                ),
                new Point2D(
                    this.point.getX() + NorthSouthWallOffset2D.LEFT.value,
                    this.point.getY()
                )
            );
        } else if (this.orientation == Orientation.COLUMN) {
            // basically the intersection point of corner walls, so it's a
            // small but mighty column...
            this.points = Arrays.asList(
                new Point2D(
                    this.point.getX() + NorthSouthWallOffset2D.LEFT.value,
                    this.point.getY() + EastWestWallOffset2D.TOP.value
                ),
                new Point2D(
                    this.point.getX() + NorthSouthWallOffset2D.RIGHT.value,
                    this.point.getY() + EastWestWallOffset2D.TOP.value
                ),
                new Point2D(
                    this.point.getX() + NorthSouthWallOffset2D.RIGHT.value,
                    this.point.getY() + EastWestWallOffset2D.BOTTOM.value
                ),
                new Point2D(
                    this.point.getX() + NorthSouthWallOffset2D.LEFT.value,
                    this.point.getY() + EastWestWallOffset2D.BOTTOM.value
                )
            );
        }
    }
}
