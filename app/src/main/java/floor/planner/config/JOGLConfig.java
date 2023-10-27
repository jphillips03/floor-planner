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

import javafx.beans.value.ChangeListener;
import javafx.scene.layout.BorderPane;

public class JOGLConfig {
    private static final Logger logger = LoggerFactory.getLogger(JOGLConfig.class);
    private static final double minHeight = 600;
    private static final double minWidth = 1000;

    private Animator animator;
    private NewtCanvasJFX glCanvas;
    private GLWindow glWindow;
    private BorderPane rootPane;
    private Screen screen;

    public ChangeListener<Number> resizeHeightListener = (observable, oldValue, newValue) -> {
        if (this.screen.getHeight() > 0) {
            this.glCanvas.setHeight(this.screen.getHeight());
        } else {
            // if screen height is 0 and glCanvas height set to that, canvas
            // height remains 0; so default to center pane height
            Double height = this.rootPane.getCenter().boundsInParentProperty().getValue().getHeight();
            this.glCanvas.setHeight(height);
        }  
    };

    public ChangeListener<Number> resizeWidthListener = (observable, oldValue, newValue) -> {
        this.glCanvas.setWidth(newValue.doubleValue());
    };

    public GLWindow getGlWindow() {
        return this.glWindow;
    }

    public JOGLConfig(BorderPane pane) throws IOException {
        this.rootPane = pane;
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
        this.rootPane.setCenter(this.glCanvas);

        animator = new Animator(this.glWindow);
        animator.start();
        logger.info("JOGL Display Initialized Successfully");
    }

    public void stop() {
        this.animator.stop();
    }
}
