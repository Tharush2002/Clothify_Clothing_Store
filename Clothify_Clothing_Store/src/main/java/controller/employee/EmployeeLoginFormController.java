package controller.employee;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import controller.HomeFormController;
import controller.auth.ForgotPasswordController;
import exceptions.*;
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
import model.Employee;
import service.ServiceFactory;
import service.custom.EmployeeService;
import util.AlertType;
import util.Type;
import util.UserType;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Getter
public class EmployeeLoginFormController implements Initializable {
    private Stage employeeLoginStage;
    private static EmployeeLoginFormController instance;
    private final EmployeeService employeeService = ServiceFactory.getInstance().getServiceType(Type.EMPLOYEE);

    public static EmployeeLoginFormController getInstance() {
        if (instance != null){
            return instance;
        }else{
            throw new IllegalStateException("Instance not created!");
        }
    }

    private void setEmployeeLoginStage(ActionEvent event) {
        employeeLoginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    public EmployeeLoginFormController() {
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
    public void btnLoginOnAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        try {
            if(pwdPassword.getText().isEmpty() || txtUserName.getText().isEmpty()) throw new EmptyFieldsException("Please Enter user-name password to continue");
            Employee employee = employeeService.findByUserName(txtUserName.getText().trim());
            if(!employee.getPassword().equals(pwdPassword.getText().trim())) throw new NoPasswordMatchFoundException("No Matched Password Found");

            ((Stage) ((Node) (event.getSource())).getScene().getWindow()).close();
            Stage stage=new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../view/EmployeeDashboard.fxml"));
            Parent root = loader.load();
            EmployeeDashboardFormController controller = loader.getController();
            controller.loadEmployeeDetails(employee);
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
            stage.setResizable(false);

        } catch (NoEmployeeFoundException e) {
            alert.setTitle("No Employee Found");
            alert.setContentText("No employee account found under the provided user name");
            alert.show();
        } catch (NoPasswordMatchFoundException e) {
            alert.setTitle("Invalid Password !");
            alert.setContentText("Password is invalid for the specific account");
            alert.show();
        }catch (EmptyFieldsException e) {
            alert.setTitle("Input Username & Password !");
            alert.setContentText("Please input username & password to continue");
            alert.show();
        } catch (RepositoryException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", e.getMessage(),AlertType.SHOW);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void openForgotPasswordOnAction(ActionEvent event) {
        try {
            setEmployeeLoginStage(event);
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../view/ForgotPassword.fxml"));
            Parent root = loader.load();
            ForgotPasswordController controller = loader.getController();
            controller.setEmployeeLoginFormController(this);
            controller.setUserType(UserType.EMPLOYEE);
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
            stage.setResizable(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        enableScreen();
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pwdPassword.textProperty().bindBidirectional(txtPassword.textProperty());
        enablePwdPassword(true);
        enableTxtPassword(false);
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
