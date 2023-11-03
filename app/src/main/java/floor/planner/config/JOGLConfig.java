package floor.planner.config;

import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.Screen;
import com.jogamp.newt.javafx.NewtCanvasJFX;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.Animator;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.scene.layout.StackPane;

public class JOGLConfig {
    private static final Logger logger = LoggerFactory.getLogger(JOGLConfig.class);
    private static final double minHeight = 600;
    private static final double minWidth = 1000;
    

    private Animator animator;
    private NewtCanvasJFX glCanvas;
    private GLWindow glWindow;
    private double menuBarHeight;
    private StackPane openGLPane;
    private Screen screen;

    public GLWindow getGlWindow() {
        return this.glWindow;
    }

    public StackPane getOpenGLPane() {
        return this.openGLPane;
    }

    public void setMenuBarHeight(double height) {
        this.menuBarHeight = height;
    }

    public JOGLConfig(StackPane pane) throws IOException {
        this.openGLPane = pane;
    }

    public void initialize() {
        logger.info("Initializing JOGL Display...");
        Display jfxNewtDisplay = NewtFactory.createDisplay(null, false);
        this.screen = NewtFactory.createScreen(jfxNewtDisplay, 0);
        final GLCapabilities caps = new GLCapabilities(GLProfile.getMaxFixedFunc(true));
        this.glWindow = GLWindow.create(screen, caps);
 
        this.glCanvas = new NewtCanvasJFX(this.glWindow);
        this.glCanvas.setWidth(minWidth);
        this.glCanvas.setHeight(minHeight);
        this.openGLPane.getChildren().add(0, this.glCanvas);

        animator = new Animator(this.glWindow);
        animator.start();
        logger.info("JOGL Display Initialized Successfully");
    }

    /**
     * Resizes the window for the canvas to given width and height. Need to
     * manually factor out height of menu bar since given height is full
     * height of window.
     * 
     * @param width The width to set for window.
     * @param height The height to set for window.
     */
    public void resizeWindow(double width, double height) {
        logger.info("Resize Window (WxH): " + width + " x " + height);
        this.glWindow.setSize((int) width, (int) height);

        // reposition window to top of parent pane, otherwise window shifts
        // down and to right every time this method is called...
        this.glWindow.setPosition(0, (int) menuBarHeight);
    }

    public void stop() {
        this.animator.stop();
    }
}
