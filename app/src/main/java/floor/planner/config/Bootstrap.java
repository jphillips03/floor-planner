package floor.planner.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
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

    @Override
    public void start(Stage stage) {
        logger.info("Application Starting...");
        try {
            this.initializeMainPanel();
            this.initializeJOGL();
            this.initializeScene(stage);
            logger.info("Application Initialized");
        } catch (IOException ex) {
            this.stop();
        }
    }

    private void initializeMainPanel() throws IOException {
        try {
            // load the main panel
            this.parent = FXMLLoader.load(ClassLoader.getSystemClassLoader().getResource("main.fxml"));
        } catch (IOException ex) {
            logger.error("Fatal Error: Issue loading main panel; Shutting down...");
            throw ex;
        }
    }

    private void initializeJOGL() throws IOException {
        try {
            joglConfig = new JOGLConfig(this.parent);
            joglConfig.initialize();
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

    @Override
    public void stop() {
        logger.info("Application shutdown");
        this.joglConfig.stop(); // stop JOGL animations
    }

    public static void main(String[] args) {
        launch();
    }
}
