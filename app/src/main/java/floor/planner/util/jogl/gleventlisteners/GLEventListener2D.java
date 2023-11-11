package floor.planner.util.jogl.gleventlisteners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.glu.GLU;

import floor.planner.models.FloorPlan;
import floor.planner.util.jogl.drawers.Drawer2D;
import floor.planner.util.jogl.objects.obj2d.ClippingPlane;

public class GLEventListener2D implements GLEventListener {
    private static final Logger logger = LoggerFactory.getLogger(GLEventListener2D.class);

    private Drawer2D drawer;
    private GLU glu;
    private FloorPlan floorPlan;

    public GLEventListener2D(FloorPlan floorPlan, GLWindow glWindow) {
        this.drawer = new Drawer2D();
        this.floorPlan = floorPlan;
    }

    public void init(final GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glDepthFunc(GL2.GL_LEQUAL);
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glLoadIdentity();

        this.glu = new GLU();
        // Disble 3D lighting
        gl.glDisable(GL2.GL_LIGHT0);
        gl.glDisable(GL2.GL_LIGHTING);
    }

    public void reshape(
        final GLAutoDrawable drawable,
        final int x,
        final int y,
        final int width,
        final int height
    ) {
        logger.info("Reshape (WxH): " + width + " " + height);
        GL2 gl = drawable.getGL().getGL2();

        // set view port (display area) to cover entire window
        gl.glViewport(0, 0, width, height);

        // setup perspective projection (aspect ratio should match viewport)
        ClippingPlane plane = this.floorPlan.getClippingPlane();
        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
        gl.glLoadIdentity();  // reset projection matrix

        this.glu.gluOrtho2D(
            plane.getLeft(),
            plane.getRight(),
            plane.getBottom(),
            plane.getTop()
        );
    }

    public void display(final GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        this.drawer.draw(gl, floorPlan, floorPlan.getCurrentFloor());
        gl.glFlush();
    }

    public void dispose(final GLAutoDrawable drawable) {}
}
