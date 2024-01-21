package floor.planner.controllers;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import floor.planner.models.FloorPlan;
import floor.planner.services.FloorPlanService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Window;

public class NewDialogController extends Dialog<FloorPlan> {
    private static final Logger logger = LoggerFactory.getLogger(NewDialogController.class);

    private FloorPlanService floorPlanService = new FloorPlanService();

    @FXML
    private DialogPane dialogPane;

    @FXML
    private TextField widthTextField;

    @FXML
    private TextField heightTextField;

    @FXML
    private TextField numFloorsTextField;

    public NewDialogController(Window owner) {
        try {
            FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemClassLoader().getResource("views/newDialog.fxml"));
            loader.setController(this);
            this.dialogPane = loader.load();

            this.dialogPane.getButtonTypes().addAll(
                ButtonType.APPLY,
                ButtonType.CANCEL
            );

            initOwner(owner);
            initModality(Modality.APPLICATION_MODAL);
            setResizable(true);
            setTitle("Create New Floor Plan");
            setDialogPane(dialogPane);
            setResultConverter(buttonType -> {
                // filter out CANCEL button clicks and return null...
                if(buttonType == ButtonType.CANCEL) {
                    return null;
                }

                // create a new empty floor plan
                int width = Integer.parseInt(this.widthTextField.getText());
                int height = Integer.parseInt(this.heightTextField.getText());
                int numFloors = Integer.parseInt(this.numFloorsTextField.getText());
                return this.floorPlanService.create(numFloors, height, width);
            });
        }
        catch (IOException e) {
            logger.error("Exception initializing new floor plan", e);
            throw new RuntimeException(e);
        }
    }
}
