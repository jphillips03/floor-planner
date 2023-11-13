package floor.planner.models;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import floor.planner.util.math.Point3D;

/**
 * The Camera class defines properties and methods needed for positioning and
 * moving the camera in floor plan in 3D.
 */
public class Camera {
    private static final Logger logger = LoggerFactory.getLogger(Camera.class);
    private int width;
    private int height;
    private int numFloors;

    // x and z position of the player, y is 0
    private float posX = 0;
    private float posY = 0;
    private float posZ = 0;
    private float lookAtX = 0;
    private float lookAtY = 0;
    private float lookAtZ = 0;
    private float headingZ = 0; // heading of player, about y-axis
    private float lookUpAngle = 0.0f;
    private float viewRadius = 1f;

    private float moveIncrement = 0.05f;
    private float turnIncrement = 0.05f; // each turn in degree
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

    public float getPosZ() {
        return this.posZ;
    }

    public float getLookAtX() {
        return this.lookAtX;
    }

    public float getLookAtY() {
        return this.lookAtY;
    }

    public float getLookAtZ() {
        return this.lookAtZ;
    }

    public void reset() {
        this.init();
    }

    private void init() {
        this.posX = this.width / 2;
        this.posY = -this.height / 2;
        this.posZ = this.numFloors + 1;

        this.lookAtX = this.posX;
        this.lookAtY = this.height / 2;
        this.lookAtZ = 0;
    }

    public void lookUp() {
        this.lookUpAngle -= this.lookUpIncrement;
        this.lookAtZ++;
    }

    public void lookDown() {
        this.lookUpAngle += this.lookUpIncrement;
        this.lookAtZ--;
    }

    public void moveIn() {
        this.move(1);
    }

    public void moveOut() {
        this.move(-1);
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

        this.lookAtX = this.posX;
        this.lookAtY = -this.posY;
        //this.lookAtX += this.posX + this.viewRadius; //increment * (float)Math.sin(Math.toRadians(this.turnIncrement)) * this.viewRadius;
        //this.lookAtY += this.posY + this.viewRadius; //increment * (float)Math.cos(Math.toRadians(this.turnIncrement)) * this.viewRadius;
    }

    public void turnLeft() {
        this.turn(this.turnIncrement);
        //this.lookAtX -= (float)Math.sin(this.turnIncrement) * this.viewRadius;
        //this.lookAtY -= (float)Math.cos(this.turnIncrement) * this.viewRadius;
        //logger.info("" + lookAtX);

        this.lookAtX -= turnIncrement;
        this.lookAtY = findPointOnCircle(this.lookAtX, this.posX, this.posY);
    }

    public void turnRight() {
        this.turn(-this.turnIncrement);
        //this.lookAtX = (float)Math.sin(this.turnIncrement) * this.lookAtX + (float)Math.sin(this.turnIncrement) * this.lookAtY;
        //this.lookAtY = (float)Math.sin(this.turnIncrement) * this.lookAtX + (float)Math.cos(this.turnIncrement) * this.lookAtY;
        //logger.info("" + lookAtX);

        this.lookAtX += turnIncrement;
        this.lookAtY = findPointOnCircle(this.lookAtX, this.posX, this.posY);
    }

    private float findPointOnCircle(float x, float h, float k) {
        float res = this.viewRadius * this.viewRadius - (x - h) * (x - h);
        return (float)Math.sqrt((double) res) + k;
    }

    private void turn(float increment) {
        this.headingZ += increment;
    }
}
