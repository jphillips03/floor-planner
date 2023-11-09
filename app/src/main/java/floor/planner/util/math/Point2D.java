package floor.planner.util.math;

public class Point2D {
    
    private float x;
    private float y;

    public Point2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return this.x;
    }
    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return this.y;
    }
    public void setY(float y) {
        this.y = y;
    }

    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }
}
