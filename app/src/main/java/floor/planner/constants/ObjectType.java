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
