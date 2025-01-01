package controller.auth;

import auth.Email;
import auth.OTP;
import com.jfoenix.controls.JFXTextField;
import controller.admin.AdminLoginFormController;
import controller.employee.EmployeeLoginFormController;
import exceptions.NoAdminFoundException;
import exceptions.NoEmployeeFoundException;
import exceptions.RepositoryException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.Setter;
import service.ServiceFactory;
import service.custom.AdminService;
import service.custom.EmployeeService;
import util.AlertType;
import util.Type;
import util.UserType;

import java.net.URL;
import java.util.ResourceBundle;

@Setter
public class ForgotPasswordController implements Initializable {
    private AdminLoginFormController adminLoginFormController;
    private EmployeeLoginFormController employeeLoginFormController;
    private UserType userType;
    private final EmployeeService employeeService = ServiceFactory.getInstance().getServiceType(Type.EMPLOYEE);
    private final AdminService adminService = ServiceFactory.getInstance().getServiceType(Type.ADMIN);
    private int timeLeft = 60;
    private Timeline timeline;
    private String generatedOTP;
    private String otp;

    @FXML
    private Button btnSendOtp;

    @FXML
    private TextField otpCode1;

    @FXML
    private TextField otpCode2;

    @FXML
    private TextField otpCode3;

    @FXML
    private TextField otpCode4;

    @FXML
    private Label otpLabelStatus;

    @FXML
    private Label otpTimer;

    @FXML
    private PasswordField pwdConfirmNewPassword;

    @FXML
    private Pane pwdConfirmNewPasswordPane;

    @FXML
    private PasswordField pwdSetNewPassword;

    @FXML
    private Pane pwdSetNewPasswordPane;

    @FXML
    private VBox resetPasswordPane;

    @FXML
    private Pane showPwdConfirmNewPasswordPane;

    @FXML
    private Pane showPwdSetNewPasswordPane;

    @FXML
    private Pane showTxtConfirmNewPasswordPane;

    @FXML
    private Pane showTxtSetNewPasswordPane;

    @FXML
    private TextField txtConfirmNewPassword;

    @FXML
    private Pane txtConfirmNewPasswordPane;

    @FXML
    private JFXTextField txtInputEmail;

    @FXML
    private TextField txtSetNewPassword;

    @FXML
    private Pane txtSetNewPasswordPane;

    @FXML
    void btnCloseOnAction(MouseEvent event) {
        closeForgotPassword(event);
    }

    @FXML
    void verifyEmail(KeyEvent event) {
        validateEmailBasedOnUser();
    }

    @FXML
    public void btnSendOtpOnAction(ActionEvent event) {
        btnSendOtp.setDisable(true);
        pwdInitialize();
        String email = txtInputEmail.getText().trim();
        try{
            if(userType==UserType.ADMIN){
                adminService.findByEmail(email);
                sendOTP(email);
            }else{
                employeeService.findByEmail(email);
                sendOTP(email);
            }
        } catch (NoAdminFoundException | NoEmployeeFoundException e) {
            noAccountFound();
        } catch (RepositoryException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", e.getMessage(),AlertType.SHOW);
        }
    }

    @FXML
    void otpTypedOnAction(KeyEvent event) {
        otp = otpCode1.getText()+otpCode2.getText()+otpCode3.getText()+otpCode4.getText();
        if(isOTPValid()){
            otpLabelStatus.setStyle("-fx-text-fill: #409443;");
            otpLabelStatus.setText("OTP Confirmed");
            enableResetPasswordPane(true);
        }else{
            otpLabelStatus.setStyle("-fx-text-fill: #d12f2f;");
            otpLabelStatus.setText("OTP Is Invalid");
            enableResetPasswordPane(false);
        }
    }

    @FXML
    void showPwdConfirmNewPasswordOnAction(MouseEvent event) {
        enablePwdConfirmNewPassword(true);
        enableTxtConfirmNewPassword(false);
        pwdConfirmNewPassword.requestFocus();
        pwdConfirmNewPassword.positionCaret(pwdConfirmNewPassword.getText().length());
    }

    @FXML
    void showPwdSetNewPasswordOnAction(MouseEvent event) {
        enablePwdSetNewPassword(true);
        enableTxtSetNewPassword(false);
        pwdSetNewPassword.requestFocus();
        pwdSetNewPassword.positionCaret(pwdSetNewPassword.getText().length());
    }

    @FXML
    void showTxtConfirmNewPasswordOnAction(MouseEvent event) {
        enablePwdConfirmNewPassword(false);
        enableTxtConfirmNewPassword(true);
        txtConfirmNewPassword.requestFocus();
        txtConfirmNewPassword.positionCaret(txtConfirmNewPassword.getText().length());
    }

    @FXML
    void showTxtSetNewPasswordOnAction(MouseEvent event) {
        enablePwdSetNewPassword(false);
        enableTxtSetNewPassword(true);
        txtSetNewPassword.requestFocus();
        txtSetNewPassword.positionCaret(txtSetNewPassword.getText().length());
    }

    @FXML
    void btnConfirmChangePasswordOnAction(ActionEvent event) {
        try{
            if(pwdSetNewPassword.getText().equals(pwdConfirmNewPassword.getText())){
                if(!pwdSetNewPassword.getText().trim().isEmpty() && !pwdConfirmNewPassword.getText().trim().isEmpty()){

                    showAlert(Alert.AlertType.INFORMATION, "Password Update", "Success!", "Your password has been successfully changed.", AlertType.SHOWANDWAIT);

                    if(userType.equals(UserType.EMPLOYEE)){
                        employeeService.updateEmployeePassword(txtInputEmail.getText().trim(), pwdSetNewPassword.getText().trim());
                    }else{
                        adminService.updateAdminPassword(txtInputEmail.getText().trim(), pwdSetNewPassword.getText().trim());
                    }
                    closeForgotPassword(event);
                }else{
                    showAlert(Alert.AlertType.ERROR, "Error", "Password Field is Empty", "Please enter your password before proceeding.", AlertType.SHOWANDWAIT);
                }
            }else{
                showAlert(Alert.AlertType.ERROR, "Error", "Passwords Do Not Match", "The passwords entered do not match. Please try again.", AlertType.SHOWANDWAIT);
            }
        } catch (RepositoryException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", e.getMessage(),AlertType.SHOW);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        resetPasswordPane.setDisable(true);
        otpInitialize();
        pwdInitialize();
    }

    private void validateEmailBasedOnUser() {
        if(userType==UserType.ADMIN){
            btnSendOtp.setDisable(!adminService.isValidEmail(txtInputEmail.getText().trim()));
        }else{
            btnSendOtp.setDisable(!employeeService.isValidEmail(txtInputEmail.getText().trim()));
        }
    }

    private void enableResetPasswordPane(boolean state){
        resetPasswordPane.setDisable(!state);
        pwdSetNewPassword.setDisable(!state);
        pwdConfirmNewPassword.setDisable(!state);
    }

    private void noAccountFound(){
        String accountType = userType.equals(UserType.EMPLOYEE) ? "employee":"admin";

        showAlert(Alert.AlertType.ERROR, String.format("No %s Found",accountType), "Error", String.format("No %s account found under the provided email", accountType), AlertType.SHOW);

        txtInputEmail.setText("");
        resetOTPLabels();
        enableResetPasswordPane(false);
    }

//    OTP Generation Checks & Timer

    private void sendOTP(String email) {
        resetOTPLabels();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("OTP Sent");
        alert.setHeaderText(null);
        alert.setContentText("The OTP is being sent to your email. Please wait...");
        alert.show();

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                enableOTPTimer();
                generatedOTP = OTP.generateOTP();
                Email.SendEmail(email, generatedOTP);
                return null;
            }

            @Override
            protected void succeeded() {
                Platform.runLater(() -> {
                    alert.setContentText("The OTP has been sent successfully to your email!");
                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    setOTPTextStatus(true);
                });
            }

            @Override
            protected void failed() {
                Platform.runLater(() -> {
                    alert.setContentText("Failed to send the OTP. Please try again.");
                    alert.setAlertType(Alert.AlertType.ERROR);
                });
            }
        };
        new Thread(task).start();
    }

    private void enableOTPTimer(){
        Platform.runLater(()->{
            btnSendOtp.setDisable(true);
            timeLeft = 60;
            updateTimerDisplay();
            timeline = new Timeline(
                    new KeyFrame(Duration.seconds(1), e -> updateTimer()));
            timeline.setCycleCount(Timeline.INDEFINITE);  // Run indefinitely
            timeline.play();
        });
    }

    private void updateTimer() {
        if (timeLeft > 0) {
            timeLeft--;
            updateTimerDisplay();
        } else {
            otpTimer.setText("");
            timeline.stop();
            validateEmailBasedOnUser();
        }
    }

    private void updateTimerDisplay() {
        otpTimer.setText("Send another OTP in: " + timeLeft + " s");
    }

    private void setOTPTextStatus(boolean b){
        otpCode1.setDisable(!b);
        otpCode2.setDisable(!b);
        otpCode3.setDisable(!b);
        otpCode4.setDisable(!b);
    }

    private void validateOTP(TextField otpCode){
        otpCode.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d?")) {
                otpCode.setText(oldValue);
            }
        });
    }

    private void resetOTPLabels(){
        otpCode1.setText("");
        otpCode2.setText("");
        otpCode3.setText("");
        otpCode4.setText("");
        txtSetNewPassword.setText("");
        txtConfirmNewPassword.setText("");
        otpLabelStatus.setText("");
    }

    private boolean isOTPValid(){
        return generatedOTP.equals(otp);
    }

    private void otpInitialize(){
        validateOTP(otpCode1);
        validateOTP(otpCode2);
        validateOTP(otpCode3);
        validateOTP(otpCode4);

        otpLabelStatus.setText("");
        btnSendOtp.setDisable(true);
        setOTPTextStatus(false);
        otpTimer.setText("");
    }

//    Password Fields

    private void enableTxtSetNewPassword(boolean state){
        txtSetNewPasswordPane.setDisable(!state);
        txtSetNewPasswordPane.setVisible(state);
        showPwdSetNewPasswordPane.setDisable(!state);
    }

    private void enableTxtConfirmNewPassword(boolean state){
        txtConfirmNewPasswordPane.setDisable(!state);
        txtConfirmNewPasswordPane.setVisible(state);
        showPwdConfirmNewPasswordPane.setDisable(!state);
    }

    private void enablePwdSetNewPassword(boolean state){
        pwdSetNewPasswordPane.setDisable(!state);
        pwdSetNewPasswordPane.setVisible(state);
        showTxtSetNewPasswordPane.setDisable(!state);
    }

    private void enablePwdConfirmNewPassword(boolean state){
        pwdConfirmNewPasswordPane.setDisable(!state);
        pwdConfirmNewPasswordPane.setVisible(state);
        showTxtConfirmNewPasswordPane.setDisable(!state);
    }

    private void pwdInitialize(){
        enablePwdSetNewPassword(true);
        enablePwdConfirmNewPassword(true);
        enableTxtSetNewPassword(false);
        enableTxtConfirmNewPassword(false);

        pwdConfirmNewPassword.setDisable(true);
        showTxtConfirmNewPasswordPane.setDisable(true);
        pwdSetNewPassword.setDisable(true);
        showTxtSetNewPasswordPane.setDisable(true);

        txtSetNewPassword.textProperty().bindBidirectional(pwdSetNewPassword.textProperty());
        txtConfirmNewPassword.textProperty().bindBidirectional(pwdConfirmNewPassword.textProperty());
    }

//  CLOSE FORGOT PASSWORD

    private void closeForgotPassword(MouseEvent event) {
        ((Stage) ((Node) (event.getSource())).getScene().getWindow()).close();
        if(userType.equals(UserType.ADMIN)){
            AdminLoginFormController.getInstance().disableScreen();
        }else{
            EmployeeLoginFormController.getInstance().disableScreen();
        }
    }

    private void closeForgotPassword(ActionEvent event) {
        ((Stage) ((Node) (event.getSource())).getScene().getWindow()).close();
        if(userType.equals(UserType.ADMIN)){
            AdminLoginFormController.getInstance().disableScreen();
        }else{
            EmployeeLoginFormController.getInstance().disableScreen();
        }
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
