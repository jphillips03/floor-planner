package floor.planner.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import floor.planner.controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * The main entry point for the application.
 */
public class Bootstrap extends Application {

    private static Logger logger = LoggerFactory.getLogger(Bootstrap.class);
    private JOGLConfig joglConfig;
    private BorderPane parent;
    private MainController main;

    @Override
    public void start(Stage stage) {
        logger.info("Application Starting...");
        try {
            this.initializeMainPanel();
            this.initializeJOGL();
            this.initializeScene(stage);
            this.initializeControllerConfigs();
            logger.info("Application Initialized");
        } catch (IOException ex) {
            this.stop();
        }
    }

    private void initializeMainPanel() throws IOException {
        try {
            // load the main panel
            FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemClassLoader().getResource("main.fxml"));
            this.parent = loader.load();
            this.main = (MainController) loader.getController();
        } catch (IOException ex) {
            logger.error("Fatal Error: Issue loading main panel; Shutting down...", ex);
            throw ex;
        }
    }

    private void initializeJOGL() throws IOException {
        try {
            this.joglConfig = new JOGLConfig(this.parent);
            this.joglConfig.initialize();

            // TODO figure out resizing...
            // this.parent.widthProperty().addListener(this.joglConfig.resizeWidthListener);
            // this.parent.heightProperty().addListener(this.joglConfig.resizeHeightListener);
        } catch (IOException ex) {
            logger.error("Fatal Error: Issue loading JOGL... shutting down...", ex);
            throw ex;
        }
    }

    private void initializeScene(Stage stage) {
        stage.setTitle("Floor Planner");
        stage.setScene(new Scene(this.parent));
        stage.show();
    }

    private void initializeControllerConfigs() {
        this.main.setGLWindow(this.joglConfig.getGlWindow());
    }

    @Override
    public void stop() {
        logger.info("Application shutdown");
        if (this.joglConfig != null) {
            this.joglConfig.stop(); // stop JOGL animations
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
