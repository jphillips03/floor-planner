package floor.planner.models;

/**
 * The Camera class defines properties and methods needed for positioning and
 * moving the camera in floor plan in 3D.
 */
public class Camera {
    private int width;
    private int height;
    private int numFloors;

    // x and z position of the player, y is 0
    private float posX = 0;
    private float posY = 0;
    private float headingZ = 0; // heading of player, about y-axis
    private float lookUpAngle = 0.0f;

    private float moveIncrement = 0.05f;
    private float turnIncrement = 1.5f; // each turn in degree
    private float lookUpIncrement = 1.0f;

    public Camera(int width, int height, int numFloors) {
        this.width = width;
        this.height = height;
        this.numFloors = numFloors;
        this.init();
    }

    public float getLookUpAngle() {
        return this.lookUpAngle;
    }

    public float getHeadingZ() {
        return this.headingZ;
    }

    public float getPosX() {
        return this.posX;
    }

    public float getPosY() {
        return this.posY;
    }

    public void reset() {
        this.init();
    }

    private void init() {
        //this.position = new Point3D((float) width / 2f, (float) height / 2f, 0);
        //new Point3D(0, 0, 0);
        //this.center = new Point3D((float) width / 2f, (float) height / 2f, 0);
        //this.up = new Point3D(0f, 1f, 0f);
        this.posX = this.width / 2;
        this.posY = this.height / 2;
    }

    public void lookUp() {
        this.lookUpAngle -= this.lookUpIncrement;
    }

    public void lookDown() {
        this.lookUpAngle += this.lookUpIncrement;
    }

    public void moveIn() {
        this.move(-1);
    }

    public void moveOut() {
        this.move(1);
    }

    /**
     * Moves the camera in or out based on the given increment which should be
     * -1 or 1. If increment is -1, then camera moves in to scene. Otherwise
     * the increment should be 1 and the camera should move out.
     *
     * @param increment The direction to move 1 (out) or -1 (in).
     */
    private void move(int increment) {
        // Player move in, posX and posZ become smaller
        this.posX += increment * (float)Math.sin(Math.toRadians(this.posX)) * moveIncrement;
        this.posY += increment * (float)Math.cos(Math.toRadians(this.posY)) * moveIncrement;
    }

    public void turnLeft() {
        this.turn(this.turnIncrement);
    }

    public void turnRight() {
        this.turn(-this.turnIncrement);
    }

    private void turn(float increment) {
        this.headingZ += increment;
    }
}
