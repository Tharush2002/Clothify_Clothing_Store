package controller.admin;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import controller.HomeFormController;
import controller.auth.ForgotPasswordController;
import controller.employee.EmployeeDashboardFormController;
import controller.product.AddProductsBySupplierFormController;
import exceptions.EmptyFieldsException;
import exceptions.NoAdminFoundException;
import exceptions.NoPasswordMatchFoundException;
import exceptions.RepositoryException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import model.Admin;
import service.ServiceFactory;
import service.custom.AdminService;
import util.AlertType;
import util.Type;
import util.UserType;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Getter
public class AdminLoginFormController implements Initializable {
    private Stage adminLoginStage;
    private static AdminLoginFormController instance;
    private final AdminService adminService = ServiceFactory.getInstance().getServiceType(Type.ADMIN);

    public static AdminLoginFormController getInstance() {
        if (instance != null){
            return instance;
        }else{
            throw new IllegalStateException("Instance not created!");
        }
    }

    private void setAdminLoginStage(ActionEvent event) {
        adminLoginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    public AdminLoginFormController() {
        instance = this;
    }

    @FXML
    private JFXPasswordField pwdPassword;

    @FXML
    private VBox screen;

    @FXML
    private JFXCheckBox showPasswordCheckBox;

    @FXML
    private JFXTextField txtPassword;

    @FXML
    private JFXTextField txtUserName;

    @FXML
    void btnCloseOnAction(MouseEvent event) {
        ((Stage) ((Node) (event.getSource())).getScene().getWindow()).close();
        HomeFormController.getInstance().getHomeStage().show();
    }

    @FXML
    public void openForgotPasswordOnAction(ActionEvent event) {
        try {
            setAdminLoginStage(event);
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../view/ForgotPassword.fxml"));
            Parent root = loader.load();
            ForgotPasswordController controller = loader.getController();
            controller.setAdminLoginFormController(this);
            controller.setUserType(UserType.ADMIN);
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
            stage.setResizable(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        enableScreen();
    }

    @FXML
    void showPasswordOnAction(ActionEvent event) {
        if(showPasswordCheckBox.isSelected()){
            enableTxtPassword(true);
            enablePwdPassword(false);
            txtPassword.requestFocus();
            txtPassword.positionCaret(txtPassword.getText().length());
        }else{
            enableTxtPassword(false);
            enablePwdPassword(true);
            pwdPassword.requestFocus();
            pwdPassword.positionCaret(pwdPassword.getText().length());
        }
    }

    @FXML
    public void btnLoginOnAction(ActionEvent event) {
        try {
            if(pwdPassword.getText().isEmpty() || txtUserName.getText().isEmpty()) throw new EmptyFieldsException("Please Enter user-name password to continue");
            Admin admin = adminService.findByUserName(txtUserName.getText().trim());
            if(!admin.getPassword().equals(pwdPassword.getText().trim())) throw new NoPasswordMatchFoundException("No Matched Password Found");

            ((Stage) ((Node) (event.getSource())).getScene().getWindow()).close();
            Stage stage=new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../view/AdminDashboard.fxml"));
            Parent root = loader.load();
            AdminDashboardFormController controller = loader.getController();
            controller.loadAdminDetails(admin);
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
            stage.setResizable(false);

        } catch (NoAdminFoundException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "No Admin Found", "No admin account found under the provided user name", AlertType.SHOW);
        } catch (NoPasswordMatchFoundException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid Password !", "Password is invalid for the specific account", AlertType.SHOW);
        }catch (EmptyFieldsException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Input Username & Password !", "Please input username & password to continue", AlertType.SHOW);
        } catch (RepositoryException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", e.getMessage(), AlertType.SHOW);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pwdPassword.textProperty().bindBidirectional(txtPassword.textProperty());
        enablePwdPassword(true);
        enableTxtPassword(false);
    }

    private void enableScreen(){
        screen.setVisible(true);
        screen.setDisable(false);
    }

    public void disableScreen(){
        screen.setVisible(false);
        screen.setDisable(true);
    }

    private void enablePwdPassword(boolean state){
        pwdPassword.setDisable(!state);
        pwdPassword.setVisible(state);
    }

    private void enableTxtPassword(boolean state){
        txtPassword.setDisable(!state);
        txtPassword.setVisible(state);
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
