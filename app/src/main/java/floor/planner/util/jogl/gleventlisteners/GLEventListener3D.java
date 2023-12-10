package floor.planner.util.jogl.gleventlisteners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.glu.GLU;

import floor.planner.models.Camera;
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
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, floorPlan.getLight().getDiffuse(), 0); // Set the diffuse lighting for LIGHT0
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, floorPlan.getLight().getSpecular(), 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, floorPlan.getLight().getAmbient(), 0); // Set the ambient lighting for LIGHT0
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
        final double aspect = (double) width / (double) height;
        glu.gluPerspective(40, aspect, 0.1, height + 1);
    }

    public void display(final GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glPushMatrix();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        
        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        gl.glLoadIdentity();
        Camera camera = this.floorPlan.getCamera();
        glu.gluLookAt(
            camera.getPosX(),
            camera.getPosY(),
            camera.getPosZ(),
            camera.getLookAtX(),
            camera.getLookAtY(),
            camera.getLookAtZ(),
            0,
            0,
            1
        );

        // set light position before any rotations/transformations so it stays
        // in a fixed position instead of moving with the camera; see below URL
        // https://eng.libretexts.org/Bookshelves/Computer_Science/Applied_Programming/Book%3A_Introduction_to_Computer_Graphics_(Eck)/04%3A_OpenGL_1.1-_Light_and_Material/4.02%3A_Light_and_Material_in_OpenGL_1.1
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, floorPlan.getLight().getPosition(), 0); // Set the position for LIGHT0

        // Original code used to "move" around in the scene, kept in case I
        // want to go back to that instead of relying on gluLookAt, which I'm
        // not sure is the correct way to actually move (although it makes the
        // most sense to me since in the real world you move the camera, and 
        // not the world around it...)
        // // Rotate up and down to look up and down
        // gl.glRotatef(floorPlan.getCamera().getLookUpAngle(), 0f, 1f, 0f);
        // // Player at headingY. Rotate the scene by -headingY instead (add 360 to get a
        // // positive angle)
        // gl.glRotatef(360.0f - floorPlan.getCamera().getHeadingZ(), 0, 0f, 1f);
        // // Player is at (posX, 0, posZ). Translate the scene to (-posX, 0, -posZ)
        // // instead.
        // gl.glTranslatef(-floorPlan.getCamera().getPosX(), -floorPlan.getCamera().getPosY(), 0);

        this.drawer.draw(gl, this.glu, floorPlan);
        gl.glPopMatrix();
        gl.glFlush();
    }

    public void dispose(final GLAutoDrawable drawable) {}
}
