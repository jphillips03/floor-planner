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
        GL2 gl = drawable.getGL().getGL2();

        // set view port (display area) to cover entire window
        gl.glViewport(0, 0, width, height);

        // // setup perspective projection (aspect ratio should match viewport)
        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
        gl.glLoadIdentity();  // reset projection matrix

        final float aspect = (float) width / (float) height;
        if (width <= height ) {
            gl.glOrthof(-1, 1, -1 * aspect, 1 * aspect, -1, 1);
        } else {
            gl.glOrthof(-1 * aspect, 1 * aspect, -1, 1, -1, 1);
        }

        // enable model-view transform and translate everything down (-1) and
        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        gl.glLoadIdentity();  // reset projection matrix

        // translate and scale the image to fit; still need to figure out how
        // to properly calculate float values for translate and scale, these
        // are just best guesses and what works to get a 5x4 floor plan in the
        // middle of the screen...
        // final float floorAspect = (float) this.floorPlan.getWidth() / (float) this.floorPlan.getHeight();
        gl.glTranslatef(-1.15f, -1.35f, 0f);
        gl.glScalef(0.45f, 0.45f, 0f);
    }

    public void display(final GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        this.drawerService.drawFloor(gl, floorPlan, 0);
    }

    // TODO figure out how to take into account aspect of floor plan because
    // things are definitely still skewed when there is more than 2 delta
    // between width and height
    private void gluOrtho2D(GL2 gl, int width, int height) {
        final float aspect = (float) width / (float) height;
        float canvasAspect = (float) this.floorPlan.getWidth() / (float) this.floorPlan.getHeight();
        float scalex, scaley;
        if (canvasAspect < aspect) {
            scalex = canvasAspect / aspect;
            scaley = 1.0f;
        } else {
            scalex = 1.0f;
            scaley = aspect / canvasAspect;
        }
        if (width <= height ) {
            // if (this.floorPlan.getWidth() <= this.floorPlan.getHeight()) {
            //     this.glu.gluOrtho2D( -1f, this.floorPlan.getWidth() + 1f, -1f / aspect, (this.floorPlan.getHeight() + floorAspect + 1f) * aspect );
            // } else {
            //     this.glu.gluOrtho2D( -1f, this.floorPlan.getWidth() + floorAspect + 1f, -1f / aspect, this.floorPlan.getHeight() + 1f );
            // }
            //this.glu.gluOrtho2D( -1f, this.floorPlan.getWidth() + 1f, -1f / aspect, (this.floorPlan.getHeight() + 1f) * aspect );
            //this.glu.gluOrtho2D(-1f, this.floorPlan.getWidth() + 1f, -1f / scaley, (this.floorPlan.getHeight() + 1f) * scaley);
            gl.glOrthof(-1, 1, -1 * aspect, 1 * aspect, -1, 1);
        } else {
            gl.glOrthof(-1 * aspect, 1 * aspect, -1, 1, -1, 1);
        }
        //this.glu.gluPerspective(45.0, aspect, 0.1, 100.0);
        //this.glu.gluOrtho2D(0f, Float.valueOf(width), 0f, Float.valueOf(height));
    }

    public void dispose(final GLAutoDrawable drawable) {}
}
