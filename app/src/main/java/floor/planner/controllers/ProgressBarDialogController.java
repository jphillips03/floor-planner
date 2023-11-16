package floor.planner.controllers;

import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProgressBarDialogController {

    private final Stage dialogStage;
    private final ProgressBar progressBar = new ProgressBar();
    private final ProgressIndicator progressIndicator = new ProgressIndicator();

    ProgressBarDialogController(Stage stage) {
        this.dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Ray Casting Progress");
        dialogStage.setWidth(200);

        progressBar.setProgress(-1f);
        progressIndicator.setProgress(-1f);

        final HBox hbox = new HBox();
        hbox.setSpacing(5);
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(progressIndicator);

        Scene scene = new Scene(hbox);
        this.dialogStage.setScene(scene);
    }

    public void activateProgressBar(final Task<?> task) {
        this.progressBar.progressProperty().bind(task.progressProperty());
        this.progressIndicator.progressProperty().bind(task.progressProperty());
        this.dialogStage.show();
    }

    public Stage getDialogStage() {
        return this.dialogStage;
    }
}
