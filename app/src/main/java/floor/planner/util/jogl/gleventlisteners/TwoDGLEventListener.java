package floor.planner.util.jogl.gleventlisteners;

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

    private GLU glu;
    private FloorPlan floorPlan;
    private FloorPlan2DDrawerService drawerService = new FloorPlan2DDrawerService();

    public TwoDGLEventListener(FloorPlan floorPlan) {
        this.floorPlan = floorPlan;
    }

    public void init(final GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        this.glu = new GLU();
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glDepthFunc(GL.GL_LEQUAL);
        gl.glHint(GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
        gl.glShadeModel(GLLightingFunc.GL_SMOOTH);
    }

    public void reshape(
        final GLAutoDrawable drawable,
        final int x,
        final int y,
        final int width,
        final int height
    ) {
        GL2 gl = drawable.getGL().getGL2();
        // final float aspect = (float) width / (float) height;

        // set view port (display area) to cover entire window
        //gl.glViewport(-1, -1, width + 1, height + 1);

        // setup perspective projection (aspect ratio should match viewport)
        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
        gl.glLoadIdentity();  // reset projection matrix
        // glu.gluPerspective(45.0, aspect, 0.1, 100.0);

        // enable model-view transform
        // gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        // gl.glLoadIdentity();  // reset projection matrix
    }

    public void display(final GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        gl.glPushMatrix();

        // set up view directly above floor with spacing around it (for some
        // reason we don't need negative value on bottom...); also, padding
        // should match otherwise rendering is skewed
        glu.gluOrtho2D(
            -2,
            this.floorPlan.getWidth() + 2,
            0,
            this.floorPlan.getHeight() + 2
        );

        gl.glDisable(GL.GL_DEPTH_TEST);
        this.drawerService.drawFloor(gl, floorPlan, 0);

        gl.glPopMatrix();
    }

    public void dispose(final GLAutoDrawable drawable) {}
}
