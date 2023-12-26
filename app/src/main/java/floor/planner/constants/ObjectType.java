package floor.planner.constants;

import java.util.Arrays;

public enum ObjectType {
    EAST_WEST_WALL("-"),
    NORTH_SOUTH_WALL("|"),
    CORNER_WALL("+"),
    WINDOW("#"),
    EAST_WEST_STAIRS(">"),
    WEST_EAST_STAIRS("<"),
    SOUTH_NORTH_STAIRS("^"),
    NORTH_SOUTH_STAIRS("v"),
    COLUMN("x"),
    POLE("."),
    HOLE("o"),
    SPHERE("s"),
    EMPTY_SPACE(" ");

    public String value;

    ObjectType(String val) {
        this.value = val;
    }

    public String toString() {
        String s;
        switch(this) {
            case EAST_WEST_WALL:
                s = "East-West Wall";
                break;
            case NORTH_SOUTH_WALL:
                s = "North-South Wall";
                break;
            case CORNER_WALL:
                s = "Corner Wall";
                break;
            case WINDOW:
                s = "Window";
                break;
            case EAST_WEST_STAIRS:
                s = "East-West Stairs";
                break;
            case WEST_EAST_STAIRS:
                s = "West-East Stairs";
                break;
            case NORTH_SOUTH_STAIRS:
                s = "North-South Stairs";
                break;
            case SOUTH_NORTH_STAIRS:
                s = "South-North Stairs";
                break;
            case COLUMN:
                s = "Column";
                break;
            case POLE:
                s = "Pole";
                break;
            case HOLE:
                s = "Hole";
                break;
            case SPHERE:
                s = "Sphere";
                break;
            default:
                s = "Empty Space";
                break;
        }
        return s + " (\"" + this.value + "\")";
    }

    /**
     * Returns the enum based on given string value (if it exists), otherwise
     * the default EMPTY_SPACE is returned.
     *
     * @param val The string value of the enum.
     * @return The enum based on given string value (or default EMPTY_SPACE).
     */
    public static ObjectType lookup(String val) {
        return Arrays.stream(ObjectType.values())
            .filter(aEnum -> aEnum.value.equals(val))
            .findFirst()
            .orElse(ObjectType.EMPTY_SPACE);
    } 
}
