package floor.planner.models;

import floor.planner.util.math.Point3D;

/**
 * The Camera class defines properties and methods needed for positioning and
 * moving the camera in floor plan in 3D.
 */
public class Camera {
    private int width;
    private int height;
    private Point3D center;
    private Point3D position;
    private Point3D up;
    private float rotateX;
    private float rotateZ;

    public Camera(int width, int height) {
        this.width = width;
        this.height = height;
        this.init();
    }

    public Point3D getCenter() {
        return this.center;
    }
    public void setCenter(Point3D center) {
        this.center = center;
    }

    public Point3D getPosition() {
        return this.position;
    }
    public void setPosition(Point3D pos) {
        this.position = pos;
    }

    public Point3D getUp() {
        return this.up;
    }
    public void setUp(Point3D up) {
        this.up = up;
    }

    public void reset() {
        this.init();
    }

    private void init() {
        this.position = new Point3D(1, 1, 8);
        this.center = new Point3D((float) width / 2f, (float) height / 2f, 0);
        this.up = new Point3D(0f, 1f, 0f);
        this.rotateX = -50;
        this.rotateZ = 0;
    }

    public float getRotateX() {
        return this.rotateX;
    }

    public void rotateX(float deg) {
        this.rotateX += deg;
    }

    public float getRotateZ() {
        return this.rotateZ;
    }

    public void rotateZ(float deg) {
        if(this.rotateZ == 360 && deg == 5) {
			this.rotateZ = 5;
		}
		else if(this.rotateZ == 0 && deg == -5) {
			this.rotateZ = 355;
		}
		else {
			this.rotateZ += deg;
		}
    }
}
