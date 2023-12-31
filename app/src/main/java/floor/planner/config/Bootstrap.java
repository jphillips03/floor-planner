package floor.planner.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import floor.planner.controllers.MainController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
        } catch (Exception ex) {
            logger.error("Exception starting app", ex);
            this.stop();
        }
    }

    private void initializeMainPanel() throws IOException {
        try {
            // load the main panel
            FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemClassLoader().getResource("views/main.fxml"));
            this.parent = loader.load();
            this.main = (MainController) loader.getController();
        } catch (IOException ex) {
            logger.error("Fatal Error: Issue loading main panel; Shutting down...", ex);
            throw ex;
        }
    }

    private void initializeJOGL() throws IOException {
        try {
            this.joglConfig = new JOGLConfig(this.main.getOpenGLPane());
            this.joglConfig.initialize();

            // wait until javafx initialization is complete to run the following
            // otherwise scene will be null
            Platform.runLater(() -> {
                this.joglConfig.setMenuBarHeight(this.main.getMenuBar().getHeight());
            });
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

        this.main.setStage(stage);
        this.main.setWindow(scene.getWindow());

        // setup listeners for screen resizing
        ChangeListener<Number> widthListener = (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            this.joglConfig.resizeWindow(newValue.intValue(), this.joglConfig.getOpenGLPane().getHeight());

        };
        ChangeListener<Number> heightListener = (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            this.joglConfig.resizeWindow(this.joglConfig.getOpenGLPane().getWidth(), newValue.intValue());
        };
        this.joglConfig.getOpenGLPane().widthProperty().addListener(widthListener);
        this.joglConfig.getOpenGLPane().heightProperty().addListener(heightListener);

        // set window dimensions needed for handling display scaling...
        this.joglConfig.setWindowDimensions(this.main.getOpenGLPane().getWidth(), this.main.getOpenGLPane().getHeight());

        // set current display scale in JOGL config
        this.joglConfig.setScaleX(stage.getRenderScaleX());
        this.joglConfig.setScaleY(stage.getRenderScaleY());

        // set up listeners for changes to display scale
        ChangeListener<Number> scaleXListener = (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            this.joglConfig.setScaleX(newValue.doubleValue());
        };
        ChangeListener<Number> scaleYListener = (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            this.joglConfig.setScaleY(newValue.doubleValue());
        };
        stage.outputScaleXProperty().addListener(scaleXListener);
        stage.outputScaleYProperty().addListener(scaleYListener);
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
