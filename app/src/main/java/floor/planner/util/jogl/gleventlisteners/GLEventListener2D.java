package floor.planner.util.jogl.gleventlisteners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.glu.GLU;

import floor.planner.models.ClippingPlane;
import floor.planner.models.FloorPlan;
import floor.planner.services.FloorPlan2DDrawerService;

public class GLEventListener2D implements GLEventListener {
    private static final Logger logger = LoggerFactory.getLogger(GLEventListener2D.class);

    private GLU glu;
    private FloorPlan floorPlan;
    private FloorPlan2DDrawerService drawerService = new FloorPlan2DDrawerService();

    public GLEventListener2D(FloorPlan floorPlan, GLWindow glWindow) {
        this.floorPlan = floorPlan;
    }

    public void init(final GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glDepthFunc(GL.GL_LEQUAL);
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
        gl.glShadeModel(GL2.GL_SMOOTH);

        this.glu = new GLU();
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
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        this.drawerService.drawFloor(gl, floorPlan, floorPlan.getCurrentFloor());
    }

    public void dispose(final GLAutoDrawable drawable) {}
}
