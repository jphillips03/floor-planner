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
import floor.planner.util.jogl.drawers.Drawer3D;

public class GLEventListener3D implements GLEventListener {
    private static final Logger logger = LoggerFactory.getLogger(GLEventListener3D.class);

    private GLU glu;
    private FloorPlan floorPlan;
    private Drawer3D drawer;

    public GLEventListener3D(FloorPlan floorPlan, GLWindow glWindow) {
        this.floorPlan = floorPlan;
        this.drawer = new Drawer3D();
    }

    public void init(final GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL2.GL_DEPTH_TEST);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glLoadIdentity();

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

        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glPushMatrix();
        final float h = width / height;
        glu.gluPerspective(45, h, 0.1, 100.0);

        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void display(final GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        glu.gluLookAt(
            this.floorPlan.getCamera().getPosition().getX(),
            this.floorPlan.getCamera().getPosition().getY(),
            this.floorPlan.getCamera().getPosition().getZ(),
            this.floorPlan.getCamera().getCenter().getX(),
            this.floorPlan.getCamera().getCenter().getY(),
            this.floorPlan.getCamera().getCenter().getZ(),
            0,
            1,
            0
        );

        gl.glRotatef(this.floorPlan.getCamera().getRotateX(), 1, 0, 0);
        gl.glRotatef(this.floorPlan.getCamera().getRotateZ(), 0, 0, 1);
        gl.glTranslatef(0, 0, floorPlan.getUp());
        gl.glScalef(floorPlan.getZoom(), floorPlan.getZoom(), floorPlan.getZoom());

        this.drawer.draw(gl, this.glu, floorPlan);
        gl.glPopMatrix();
    }

    public void dispose(final GLAutoDrawable drawable) {}
}
