package floor.planner.models;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import floor.planner.util.jogl.objects.obj2d.ClippingPlane;

public class FloorPlan {
    private static final Logger logger = LoggerFactory.getLogger(FloorPlan.class);

    private Camera camera;
    /** The clipping plane for the 2D rendering. */
    private ClippingPlane clippingPlane;
    /** The current floor to render in 2D (defaults to first floor i.e. 0). */
    private int currentFloor = 0;
    private Light light;
    /** The width of the floor plan. */
    private int width;
    /** The height of the floor plan. */
    private int height;
    /** The number of floors. */
    private int numFloors = 0;
    /** The floor plan text */
    private String floorPlanText;
    /** The floors in the floor plan. */
    private ArrayList<Floor> floors;
    private boolean render2D = true;
    /** The model view matrix. */
	private float[][] modelView;

    public float[][] getModelView() {
        return this.modelView;
    }

    public void setModelView(float[][] val) {
        this.modelView = val;
    }

    /**
     * Constructor for the floor plan class. Sets the first floor to be the 
     * current floor, creates a new array list of floors, and sets up the floor
     * matrix.
     */
    public FloorPlan() {
        floors = new ArrayList<Floor>();
    }

    public Camera getCamera() {
        return this.camera;
    }

    public ClippingPlane getClippingPlane() {
        return this.clippingPlane;
    }
    public void setClippingPlane(ClippingPlane plane) {
        this.clippingPlane = plane;
    }

    /**
     * Returns the current floor to render in 2D.
     *
     * @return The current floor to render in 2D.
     */
    public int getCurrentFloor() {
        return this.currentFloor;
    }
    /**
     * Sets the current floor to render in 2D.
     *
     * @param val The value for the current floor.
     */
    public void setCurrentFloor(int val) {
        this.currentFloor = val;
    }

    public Light getLight() {
        return this.light;
    }

    public List<Floor> getFloors() {
        return this.floors;
    }

    /**
     * Returns the width of this floor plan. The width is defined to be
     * the side along the y-axis.
     *
     * @return The width of the current floor plan.
     */
    public int getWidth() {
        return width;
    }

    /**
     * The height of this floor plan. The height is defined to be the
     * side along the x-axis.
     *
     * @return The height of the current floor plan.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the number of floors in this floor plan.
     *
     * @return The number of floors in this floor plan. 
     */
    public int getFloorNumbers() {
        return numFloors;
    }

    /**
     * Returns the specified floor in the current floor plan.
     *
     * @param number The desired floor in the floor plan.
     * @return The desired floor in the floor plan.
     */
    public Floor getFloor(int number) {
        return floors.get(number);
    }

    /**
     * Returns the entire floor plan as a string.
     *
     * @return The entire floor plan as a string.
     */
    public String toString() {
        String floor = "";
        for(int i = 0; i < floors.size(); i++) {
            Floor f = floors.get(i);
            floor += f.toString();
        }
        return floor;
    }

    /**
     * Sets the dimensions of this floor plan.
     *
     * @param height The height of the floor plan.
     * @param width The width of the floor plan.
     * @param numFloors The number of floors in this floor plan.
     */
    public void setDimensions(int height, int width, int numFloors) {
        this.height = height;
        this.width = width;
        this.numFloors = numFloors;
        this.camera = new Camera(width, height);
        this.light = new Light(width, height, numFloors);
    }

    public boolean getRender2D() {
        return this.render2D;
    }
    public void setRender2D(boolean val) {
        this.render2D = val;
    }
}
