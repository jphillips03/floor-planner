package floor.planner.util.jogl.gleventlisteners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL2ES1;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.fixedfunc.GLLightingFunc;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.glu.GLU;

import floor.planner.models.FloorPlan;
import floor.planner.services.FloorPlan2DDrawerService;

public class TwoDGLEventListener implements GLEventListener {
    private static final Logger logger = LoggerFactory.getLogger(TwoDGLEventListener.class);

    private GLU glu;
    private GLWindow glWindow;
    private FloorPlan floorPlan;
    private FloorPlan2DDrawerService drawerService = new FloorPlan2DDrawerService();

    public TwoDGLEventListener(FloorPlan floorPlan, GLWindow glWindow) {
        this.floorPlan = floorPlan;
        this.glWindow = glWindow;
    }

    public void init(final GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glDepthFunc(GL.GL_LEQUAL);
        gl.glShadeModel(GLLightingFunc.GL_SMOOTH);
        gl.glHint(GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);

        this.glu = new GLU();
    }

    public void reshape(
        final GLAutoDrawable drawable,
        final int x,
        final int y,
        final int width,
        final int height
    ) {
        logger.info("Reshape: " + width + " " + height);
        GL2 gl = drawable.getGL().getGL2();

        // set view port (display area) to cover entire window
        gl.glViewport(0, 0, width, height);

        // // setup perspective projection (aspect ratio should match viewport)
        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
        gl.glLoadIdentity();  // reset projection matrix

        final float aspect = (float) width / (float) height;
        this.gluOrtho2D(width, height, aspect);

        // enable model-view transform and translate everything down (-1) and
        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        gl.glLoadIdentity();  // reset projection matrix

        // translate and scale image to fit to center of screen
        gl.glTranslatef(
            - (float) this.floorPlan.getWidth() * 0.5f / 2 + 0.5f,
            - (float) this.floorPlan.getHeight() * 0.5f / 2 + (0.5f / aspect),
            0f
        );
        gl.glScalef(0.5f / aspect, 0.5f / aspect, 0f);
    }

    public void display(final GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        this.drawerService.drawFloor(gl, floorPlan, 0);
    }

    private void gluOrtho2D(int width, int height, float aspect) {
        if (width <= height ) {
            this.glu.gluOrtho2D(-1, 1, -1 * aspect, 1 * aspect);
        } else {
            this.glu.gluOrtho2D(-1 * aspect, 1 * aspect, -1, 1);
        }
    }

    public void dispose(final GLAutoDrawable drawable) {}
}
