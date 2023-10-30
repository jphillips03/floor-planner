package floor.planner.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import floor.planner.controllers.MainController;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
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
        } catch (IOException ex) {
            logger.error("Fatal Error: Issue loading JOGL... shutting down...", ex);
            throw ex;
        }
    }

    private void initializeScene(Stage stage) {
        Scene scene = new Scene(this.parent);
        stage.setTitle("Floor Planner");
        stage.setScene(scene);
        stage.show();

        // setup listener for screen resizing
        ChangeListener<Number> stageSizeListener = (obs, prev, next) -> {
            this.joglConfig.resizeWindow(scene.getWidth(), scene.getHeight());
        };
        scene.heightProperty().addListener(stageSizeListener);
        scene.widthProperty().addListener(stageSizeListener);
    }

    private void initializeControllerConfigs() {
        this.main.setGLWindow(this.joglConfig.getGlWindow());
    }

    @Override
    public void stop() {
        logger.info("Application shutdown initiated");
        if (this.joglConfig != null) {
            this.joglConfig.stop(); // stop JOGL animations
        }

        System.exit(0);
    }

    public static void main(String[] args) {
        launch();
    }
}
