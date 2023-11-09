package floor.planner.util.jogl.objects;

import com.jogamp.opengl.GL2;

/**
 * 
 */
public abstract class DrawableElement {
 
    public DrawableElement() {}

    /**
     * Draws the points for the element as a GL_POLYGON.
     *
     * @param gl JOGL object used to do the drawing.
     */
    public abstract void draw(GL2 gl);

    /**
     * Initializes the points for the element representing a polygon.
     */
    // public abstract void initPoints();
}
