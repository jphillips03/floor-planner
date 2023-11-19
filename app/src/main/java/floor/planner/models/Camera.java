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

    private float posX = 0;
    private float posY = 0;
    private float posZ = 0;
    private float lookAtX = 0;
    private float lookAtY = 0;
    private float lookAtZ = 0;
    private float viewRadius;
    private float moveIncrement = 0.05f;
    private int currentPoint = 0;
    private int currentZPoint = 0;
    private float degreesBetweenPoints = 5f;
    private List<Point3D> pointsOnLookAtCircle;
    private List<Point3D> pointsOnTiltCircle;

    public Camera(int width, int height, int numFloors) {
        this.width = width;
        this.height = height;
        this.numFloors = numFloors;
        this.init();
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
        this.viewRadius = this.width / this.height * 2;

        this.initPointsOnCircle();
        // set look at point to face +y axis (1/4 of way around circle)
        this.currentPoint = this.pointsOnLookAtCircle.size() / 4;
        this.currentZPoint = this.pointsOnTiltCircle.size() / 4;
        this.setLookAt(this.currentPoint);
    }

    private void initPointsOnCircle() {
        this.pointsOnLookAtCircle = new ArrayList<Point3D>();
        this.pointsOnTiltCircle = new ArrayList<Point3D>();
        for (float d = 0; d < 360; d += this.degreesBetweenPoints) {
            // 0 is +x axis, 90 is +y axis, 180 is -x axis, and 270 is -y axis
            float x = this.posX + (this.viewRadius * (float) Math.cos(Math.toRadians(d)));
            float y = this.posY + (this.viewRadius * (float) Math.sin(Math.toRadians(d)));
            Point3D p = new Point3D(x, y, this.posZ);
            this.pointsOnLookAtCircle.add(p);

            // set up points around vertical circle for tilt
            float z = this.posZ + (this.viewRadius * (float) Math.cos(Math.toRadians(d)));
            p = new Point3D(this.posX, y, z);
            this.pointsOnTiltCircle.add(p);
        }
    }

    private void setLookAt(int i) {
        Point3D lookAtPoint = this.pointsOnLookAtCircle.get(i);
        this.lookAtX = lookAtPoint.getX();
        this.lookAtY = lookAtPoint.getY();
        this.lookAtZ = this.pointsOnTiltCircle.get(this.currentZPoint).getZ();
    }

    public void lookUp() {
        this.changeCurrentZPoint(-1);
        this.lookAtX = this.pointsOnTiltCircle.get(this.currentZPoint).getX();
        this.lookAtZ = this.pointsOnTiltCircle.get(this.currentZPoint).getZ();
    }

    public void lookDown() {
        this.changeCurrentZPoint(1);
        this.lookAtX = this.pointsOnTiltCircle.get(this.currentZPoint).getX();
        this.lookAtZ = this.pointsOnTiltCircle.get(this.currentZPoint).getZ();
    }

    public void moveIn() {
        this.move(1);
    }

    public void moveOut() {
        this.move(-1);
    }

    /**
     * Moves the camera in or out based on the given increment which should be
     * -1 or 1. If increment is -1, then camera moves out to scene. Otherwise
     * the increment should be 1 and the camera should move in.
     *
     * @param increment The direction to move 1 (in) or -1 (out).
     */
    private void move(int increment) {
        float t = moveIncrement / this.viewRadius;

        // moving is determined based on tilt (up or down) and look angle
        // (right or left); moving in XY plane or YZ plane is easy, moving at
        // at tilt is most complicated (starting to see why most solutions
        // just translate/rotate model instead of moving camera...)
        if (
            this.currentZPoint == this.pointsOnTiltCircle.size() / 4 ||
            this.currentZPoint == this.pointsOnTiltCircle.size() * (3/4)
        ) {
            // we're moving flat, so just move in XY plane...
            logger.info("Moving flat");
            // determine point to move towards (either lookAt point or point on
            // opposite end of circle depending on given increment)
            float x2, y2;
            if (increment > 0) {
                // moving in so just use the lookAt point
                x2 = this.lookAtX;
                y2 = this.lookAtY;
            } else {
                // moving away so find the point opposite where we are looking and
                // move toward that; see https://math.stackexchange.com/a/567081
                x2 = 2 * this.posX - this.lookAtX;
                y2 = 2 * this.posY - this.lookAtY;
            }

            // move along the line of sight (line between pos and lookAt) towards
            // (x2, y2) as defined above
            this.posX = (1 - t) * this.posX + t * x2;
            this.posY = (1 - t) * this.posY + t * y2;
        } else if (
            this.currentPoint == 0 ||
            this.currentPoint == this.pointsOnLookAtCircle.size() / 2
        ) {
            // we're moving up or down so just move in YZ plane
            logger.info("Moving up/down");
            // determine point to move towards (either lookAt point or point on
            // opposite end of circle depending on given increment)
            float z2, y2;
            if (increment > 0) {
                // moving in so just use the lookAt point
                z2 = this.lookAtZ;
                y2 = this.lookAtY;
            } else {
                // moving away so find the point opposite where we are looking and
                // move toward that; see https://math.stackexchange.com/a/567081
                z2 = 2 * this.posZ - this.lookAtZ;
                y2 = 2 * this.posY - this.lookAtY;
            }

            // move along the line of sight (line between pos and lookAt) towards
            // (z2, y2) as defined above
            this.posZ = (1 - t) * this.posZ + t * z2;
            this.posY = (1 - t) * this.posY + t * y2;
        } else {
            // we're tilted...
            float x2, y2, z2;
            if (increment > 0) {
                // moving in so just use the lookAt point
                x2 = this.lookAtX;
                y2 = this.lookAtY;
                z2 = this.lookAtZ;
            } else {
                // moving away so find the point opposite where we are looking and
                // move toward that; see https://math.stackexchange.com/a/567081
                x2 = 2 * this.posX - this.lookAtX;
                y2 = 2 * this.posY - this.lookAtY;
                z2 = 2 * this.posZ - this.lookAtZ;
            }

            // move along the line of sight (line between pos and lookAt) towards
            // (x2, y2) as defined above
            this.posX = (1 - t) * this.posX + t * x2;
            this.posY = (1 - t) * this.posY + t * y2;
            this.posZ = (1 - t) * this.posZ + t * z2;
        }

        this.initPointsOnCircle();
        this.setLookAt(this.currentPoint);
    }

    public void turnLeft() {
        this.changeCurrentPoint(1);
        this.setLookAt(this.currentPoint);
    }

    public void turnRight() {
        this.changeCurrentPoint(-1);
        this.setLookAt(this.currentPoint);
    }

    private void changeCurrentPoint(int delta) {
        if (delta < 0) {
            if (this.currentPoint == 0) {
                this.currentPoint = this.pointsOnLookAtCircle.size() - 1;
            } else {
                this.currentPoint--;
            }
        } else {
            if (this.currentPoint == this.pointsOnLookAtCircle.size() - 1) {
                this.currentPoint = 0;
            } else {
                this.currentPoint++;
            }
        }
    }

    private void changeCurrentZPoint(int delta) {
        if (delta < 0) {
            if (this.currentZPoint > 0) {
                this.currentZPoint--;
            }
        } else {
            if (this.currentZPoint < this.pointsOnTiltCircle.size() - 1) {
                this.currentZPoint++;
            }
        }
    }

    
}
