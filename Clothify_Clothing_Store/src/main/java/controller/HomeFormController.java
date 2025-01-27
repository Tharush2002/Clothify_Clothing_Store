package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import util.AlertType;

import java.io.IOException;

@Getter
public class HomeFormController{

    private static HomeFormController instance;
    private Stage homeStage;

    public static HomeFormController getInstance() {
        return instance==null ? new HomeFormController():instance;
    }

    public HomeFormController(){
        instance=this;
    }

    private void hideHomeStage(ActionEvent event){
        homeStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        homeStage.hide();
    }

    @FXML
    void btnCloseOnAction(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    void btnAdminLoginFormOnAction(ActionEvent event) {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("../view/AdminLogin.fxml"));
            Scene scene = new Scene(root);

            final double[] xOffset = {0};
            final double[] yOffset = {0};

            root.setOnMousePressed(e -> {
                xOffset[0] = e.getSceneX();
                yOffset[0] = e.getSceneY();
            });

            root.setOnMouseDragged(e -> {
                stage.setX(e.getScreenX() - xOffset[0]);
                stage.setY(e.getScreenY() - yOffset[0]);
            });

            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
            stage.setResizable(false);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", e.getMessage(), AlertType.SHOW);
        }
        hideHomeStage(event);

    }

    @FXML
    void btnEmployeeLoginFormOnAction(ActionEvent event) {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("../view/EmployeeLogin.fxml"));
            Scene scene = new Scene(root);

            final double[] xOffset = {0};
            final double[] yOffset = {0};

            root.setOnMousePressed(e -> {
                xOffset[0] = e.getSceneX();
                yOffset[0] = e.getSceneY();
            });

            root.setOnMouseDragged(e -> {
                stage.setX(e.getScreenX() - xOffset[0]);
                stage.setY(e.getScreenY() - yOffset[0]);
            });

            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
            stage.setResizable(false);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", e.getMessage(), AlertType.SHOW);
        }
        hideHomeStage(event);
    }

    private void showAlert(Alert.AlertType alertType, String title, String headerText, String message, AlertType showType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(message);
        if(showType==AlertType.SHOW){
            alert.show();
        }else{
            alert.showAndWait();
        }
    }
}
