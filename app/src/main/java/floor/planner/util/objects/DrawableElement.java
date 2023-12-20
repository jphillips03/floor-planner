package floor.planner.util.objects;

import com.jogamp.opengl.GL2;

/**
 * So far a drawable element is one that should define a draw method for
 * rendering things in JOGL...
 */
public interface DrawableElement {

    /**
     * Draws the points for the element.
     *
     * @param gl JOGL object used to do the drawing.
     */
    public abstract void draw(GL2 gl);
}
