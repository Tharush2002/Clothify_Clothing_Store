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
//        ((Stage) ((Node) (event.getSource())).getScene().getWindow()).close();
//        try {
//            Stage stage=new Stage();
//            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../../view/AdminDashboard.fxml"))));
//            stage.initStyle(StageStyle.UNDECORATED);
//            stage.show();
//            stage.setResizable(false);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        try {
            if(pwdPassword.getText().isEmpty() || txtUserName.getText().isEmpty()) throw new EmptyFieldsException("Please Enter user-name password to continue");
            Admin admin = adminService.findByUserName(txtUserName.getText().trim());
            if(!admin.getPassword().equals(pwdPassword.getText().trim())) throw new NoPasswordMatchFoundException("No Matched Password Found");

            ((Stage) ((Node) (event.getSource())).getScene().getWindow()).close();
            Stage stage=new Stage();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../../view/AdminDashboard.fxml"))));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
            stage.setResizable(false);

        } catch (NoAdminFoundException e) {
            alert.setTitle("No Admin Found");
            alert.setContentText("No admin account found under the provided user name");
            alert.show();
        } catch (NoPasswordMatchFoundException e) {
            alert.setTitle("Invalid Password !");
            alert.setContentText("Password is invalid for the specific account");
            alert.show();
        }catch (EmptyFieldsException e){
            alert.setTitle("Input Username & Password !");
            alert.setContentText("Please input username & password to continue");
            alert.show();
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
}
