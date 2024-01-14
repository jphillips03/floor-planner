package floor.planner.models;

public class FloorOption {
    private int value;

    public FloorOption(int val) {
        this.value = val;
    }

    public int getValue() {
        return this.value - 1;
    }

    public String toString() {
        return String.format("Floor %d", this.value);
    }
}
