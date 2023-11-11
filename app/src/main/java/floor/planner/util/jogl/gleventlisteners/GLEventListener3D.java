package floor.planner.util.jogl.gleventlisteners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.fixedfunc.GLLightingFunc;
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

        // Set up lighting
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, floorPlan.getLight().getAmbient(), 0); // Set the ambient lighting for LIGHT0
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, floorPlan.getLight().getDiffuse(), 0); // Set the diffuse lighting for LIGHT0
        //gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, floorPlan.getLight().getSpecular(), 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, floorPlan.getLight().getPosition(), 0); // Set the position for LIGHT0
        gl.glEnable(GL2.GL_LIGHT0);   // Enable LIGHT0
        gl.glEnable(GL2.GL_LIGHTING); // Enable Lighting

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
        final float h = width / height;
        glu.gluPerspective(50, h, 0.1, 100.0);

        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void display(final GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glPushMatrix();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        glu.gluLookAt(
            this.floorPlan.getWidth() + 1,
            0,
            this.floorPlan.getFloorNumbers(),
            this.floorPlan.getWidth() / 2,
            this.floorPlan.getHeight() / 2,
            1,
            0,
            0,
            1
        );

        // Rotate up and down to look up and down
        gl.glRotatef(floorPlan.getCamera().getLookUpAngle(), 1.0f, 0, 0);

        // Player at headingY. Rotate the scene by -headingY instead (add 360 to get a
        // positive angle)
        gl.glRotatef(360.0f - floorPlan.getCamera().getHeadingY(), 0, 1.0f, 0);

        // Player is at (posX, 0, posZ). Translate the scene to (-posX, 0, -posZ)
        // instead.
        gl.glTranslatef(-floorPlan.getCamera().getPosX(), 0, floorPlan.getCamera().getPosZ());
        // gl.glPopMatrix();

        //gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, floorPlan.getLight().getDiffuse(), 0); // Set the diffuse lighting for LIGHT0
        //gl.glPushMatrix();
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, floorPlan.getLight().getPosition(), 0); // Set the position for LIGHT0
        //gl.glPopMatrix();

        this.drawer.draw(gl, this.glu, floorPlan);
        gl.glPopMatrix();
        gl.glFlush();
    }

    public void dispose(final GLAutoDrawable drawable) {}
}
