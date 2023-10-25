package floor.planner.config;

import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.Screen;
import com.jogamp.newt.javafx.NewtCanvasJFX;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL2ES1;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.fixedfunc.GLLightingFunc;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.util.Animator;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.scene.layout.BorderPane;

public class JOGLConfig {
    private static final Logger logger = LoggerFactory.getLogger(JOGLConfig.class);

    private BorderPane rootPane;
    private Animator animator;

    public JOGLConfig(BorderPane pane) throws IOException {
        this.rootPane = pane;
    }

    public void initialize() {
        logger.info("Initializing JOGL Display...");
        Display jfxNewtDisplay = NewtFactory.createDisplay(null, false);
        final Screen screen = NewtFactory.createScreen(jfxNewtDisplay, 0);
        final GLCapabilities caps = new GLCapabilities(GLProfile.getMaxFixedFunc(true));
        final GLWindow glWindow1 = GLWindow.create(screen, caps);
        this.addGLEventListeners(glWindow1);
        
        final NewtCanvasJFX glCanvas = new NewtCanvasJFX(glWindow1);
        glCanvas.setWidth(1000);
        glCanvas.setHeight(600);
        this.rootPane.setCenter(glCanvas);
        animator = new Animator(glWindow1);
        animator.start();
        logger.info("JOGL Display Initialized Successfully");
    }

    private void addGLEventListeners(GLWindow glWindow1) {
        glWindow1.addGLEventListener(new GLEventListener() {
            private float rotateT = 0.0f;

            public void init(final GLAutoDrawable drawable) {
                GL2 gl = drawable.getGL().getGL2();
                gl.glShadeModel(GLLightingFunc.GL_SMOOTH);
                gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
                gl.glClearDepth(1.0f);
                gl.glEnable(GL.GL_DEPTH_TEST);
                gl.glDepthFunc(GL.GL_LEQUAL);
                gl.glHint(GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
            }

            public void reshape(final GLAutoDrawable drawable, final int x, final int y, final int width, final int height) {
                GL2 gl = drawable.getGL().getGL2();
                final float aspect = (float) width / (float) height;
                gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
                gl.glLoadIdentity();
                final float fh = 0.5f;
                final float fw = fh * aspect;
                gl.glFrustumf(-fw, fw, -fh, fh, 1.0f, 1000.0f);
                gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
                gl.glLoadIdentity();
            }

            public void display(final GLAutoDrawable drawable) {
                final GL2 gl = drawable.getGL().getGL2();
                gl.glClear(GL.GL_COLOR_BUFFER_BIT);
                gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
                gl.glLoadIdentity();
                gl.glTranslatef(0.0f, 0.0f, -5.0f);
                gl.glRotatef(rotateT, 1.0f, 0.0f, 0.0f);
                gl.glRotatef(rotateT, 0.0f, 1.0f, 0.0f);
                gl.glRotatef(rotateT, 0.0f, 0.0f, 1.0f);
                gl.glBegin(GL2.GL_QUADS);       
                gl.glColor3f(0.0f, 1.0f, 1.0f);
                gl.glVertex3f(-1.0f, 1.0f, 0.0f);
                gl.glVertex3f( 1.0f, 1.0f, 0.0f);
                gl.glVertex3f( 1.0f,-1.0f, 0.0f);
                gl.glVertex3f(-1.0f,-1.0f, 0.0f);
                gl.glEnd();                  
                rotateT += 0.2f; 
            }

            public void dispose(final GLAutoDrawable drawable) {}
        });
    }

    public void stop() {
        this.animator.stop();
    }
}
