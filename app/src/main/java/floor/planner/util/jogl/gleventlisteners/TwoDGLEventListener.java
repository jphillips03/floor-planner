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

import floor.planner.models.FloorPlan;
import floor.planner.services.FloorPlan2DDrawerService;

public class TwoDGLEventListener implements GLEventListener {
    private static final Logger logger = LoggerFactory.getLogger(TwoDGLEventListener.class);

    private GLU glu;
    private FloorPlan floorPlan;
    private FloorPlan2DDrawerService drawerService = new FloorPlan2DDrawerService();

    public TwoDGLEventListener(FloorPlan floorPlan, GLWindow glWindow) {
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
        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
        gl.glLoadIdentity();  // reset projection matrix
        this.gluOrtho2D(width, height);
    }

    public void display(final GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        this.drawerService.drawFloor(gl, floorPlan, 0);
    }

    public void dispose(final GLAutoDrawable drawable) {}

    private void gluOrtho2D(int width, int height) {
        float aspect = (float) width / (float) height;
        int gcd = this.gcd(width, height);
        float left = (float) this.floorPlan.getWidth() / 2 - (width / gcd);
        float right = (float) this.floorPlan.getWidth() / 2 + (width / gcd);
        float bottom = (float) this.floorPlan.getHeight() / 2 - (height / gcd);
        float top = (float) this.floorPlan.getHeight() / 2 + (height / gcd);
        logger.info("Aspect: " + aspect);
        logger.info("GCD: " + gcd);
        logger.info("Width: " + (width / gcd));
        logger.info("Height: " + (height / gcd));
        logger.info("Left: " + left);
        logger.info("Right: " + right);
        logger.info("Bottom: " + bottom);
        logger.info("Top: " + top);
        this.glu.gluOrtho2D(left, right, bottom, top);
    }

    private int gcd(int a, int b) {
        // if (b == 0) return a;
        // return gcd(b, a % b);
        return 200;
    }
}
