package controller.admin;

import controller.HomeFormController;
import controller.employee.EditEmployeeFormController;
import db.DBConnection;
import exceptions.NoAdminFoundException;
import exceptions.NoEmployeeFoundException;
import exceptions.RepositoryException;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;
import model.Admin;
import model.Employee;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import service.ServiceFactory;
import service.custom.AdminService;
import service.custom.EmployeeService;
import util.AdminDashboardViewType;
import util.AlertType;
import util.Type;
import util.UserType;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

@Getter
@Setter
public class AdminDashboardFormController implements Initializable {
    private Admin loggedAdmin;
    private final AdminService adminService = ServiceFactory.getInstance().getServiceType(Type.ADMIN);
    private final EmployeeService employeeService = ServiceFactory.getInstance().getServiceType(Type.EMPLOYEE);

    private Admin selectedAdminToEdit = new Admin();
    private Employee selectedEmployeeToEdit = new Employee();


    private Stage adminDashboardStage;

    private static AdminDashboardFormController instance;

    public static AdminDashboardFormController getInstance() {
        if (instance != null){
            return instance;
        }else{
            throw new IllegalStateException("Instance not created!");
        }
    }

    private void setEmployeeDashboardStage(MouseEvent event) {
        adminDashboardStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    public AdminDashboardFormController() {
        if (instance == null) instance = this;
    }

    @FXML
    private AnchorPane anchorPaneAddAdmin;

    @FXML
    private AnchorPane anchorPaneAddEmployee;

    @FXML
    private AnchorPane anchorPaneDetails;

    @FXML
    private AnchorPane anchorPaneEdit;

    @FXML
    private AnchorPane anchorPaneReports;

    @FXML
    private Button btnAddAdmin;

    @FXML
    private Button btnAddEmployee;

    @FXML
    private Button btnAdminDetails;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnReports;

    @FXML
    private Button btnResetPassword;

    @FXML
    private TableColumn<Admin, Void> columnAdminActions;

    @FXML
    private TableColumn<Admin, String> columnAdminAdminId;

    @FXML
    private TableColumn<Admin, String> columnAdminAdminName;

    @FXML
    private TableColumn<Employee, Void> columnEmployeeActions;

    @FXML
    private TableColumn<Employee, String> columnEmployeeEmployeeId;

    @FXML
    private TableColumn<Employee, String> columnEmployeeEmployeeName;

    @FXML
    private Label lblAdminEmail;

    @FXML
    private Label lblAdminId;

    @FXML
    private Label lblAdminName;

    @FXML
    private Label lblAdminPhoneNumber;

    @FXML
    private Label lblAdminUserName;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblTime;

    @FXML
    private PasswordField pwdAdminConfirmNewPassword;

    @FXML
    private Pane pwdAdminConfirmNewPasswordPane;

    @FXML
    private PasswordField pwdAdminSetNewPassword;

    @FXML
    private Pane pwdAdminSetNewPasswordPane;

    @FXML
    private PasswordField pwdEmployeeConfirmNewPassword;

    @FXML
    private Pane pwdEmployeeConfirmNewPasswordPane;

    @FXML
    private PasswordField pwdEmployeeSetNewPassword;

    @FXML
    private Pane pwdEmployeeSetNewPasswordPane;

    @FXML
    private PasswordField pwdResetPasswordConfirmNewPassword;

    @FXML
    private Pane pwdResetPasswordConfirmNewPasswordPane;

    @FXML
    private PasswordField pwdResetPasswordSetNewPassword;

    @FXML
    private Pane pwdResetPasswordSetNewPasswordPane;

    @FXML
    private Pane resetPasswordPane;

    @FXML
    private VBox screen;

    @FXML
    private TableView<Admin> tblAdmin;

    @FXML
    private TableView<Employee> tblEmployee;

    @FXML
    private TextField txtAdminConfirmNewPassword;

    @FXML
    private Pane txtAdminConfirmNewPasswordPane;

    @FXML
    private TextField txtAdminSetContactNumber;

    @FXML
    private TextField txtAdminSetEmail;

    @FXML
    private TextField txtAdminSetFirstName;

    @FXML
    private TextField txtAdminSetLastName;

    @FXML
    private TextField txtAdminSetNewPassword;

    @FXML
    private Pane txtAdminSetNewPasswordPane;

    @FXML
    private TextField txtAdminSetUserName;

    @FXML
    private TextField txtEmployeeConfirmNewPassword;

    @FXML
    private Pane txtEmployeeConfirmNewPasswordPane;

    @FXML
    private TextField txtEmployeeSetNewPassword;

    @FXML
    private Pane txtEmployeeSetNewPasswordPane;

    @FXML
    private TextField txtResetPasswordConfirmNewPassword;

    @FXML
    private Pane txtResetPasswordConfirmNewPasswordPane;

    @FXML
    private TextField txtResetPasswordSetNewPassword;

    @FXML
    private Pane txtResetPasswordSetNewPasswordPane;

    @FXML
    private TextField txtSetEmployeeAddress;

    @FXML
    private TextField txtSetEmployeeContactNumber;

    @FXML
    private TextField txtSetEmployeeEmail;

    @FXML
    private TextField txtSetEmployeeFirstName;

    @FXML
    private TextField txtSetEmployeeLastName;

    @FXML
    private TextField txtSetEmployeeNIC;

    @FXML
    private TextField txtSetEmployeeUserName;

    @FXML
    void btnAddAdminOnAction(ActionEvent event) {
        handleDashboardSidePanelBtnClicks(AdminDashboardViewType.ADMIN);
    }

    @FXML
    void btnAddEmployeeOnAction(ActionEvent event) {
        handleDashboardSidePanelBtnClicks(AdminDashboardViewType.EMPLOYEE);
    }

    @FXML
    void btnAdminDetailsOnAction(ActionEvent event) {
        handleDashboardSidePanelBtnClicks(AdminDashboardViewType.DETAILS);
    }

    @FXML
    void btnCancelResetPasswordOnAction(ActionEvent event) {
        setPwdInitializationStates();
    }

    @FXML
    void btnClearAdminOnAction(ActionEvent event) {
        resetAddAdminLabels();
    }

    @FXML
    void btnClearEmployeeOnAction(ActionEvent event) {
        resetAddEmployeeLabels();
    }

    @FXML
    void btnCloseOnAction(MouseEvent event) {
        closeAdminDashboard(event);
    }

    @FXML
    void btnConfirmResetPasswordOnAction(ActionEvent event) {
        if(pwdResetPasswordConfirmNewPassword.getText().equals(pwdResetPasswordSetNewPassword.getText())){
            if(!pwdResetPasswordSetNewPassword.getText().trim().isEmpty() && !pwdResetPasswordConfirmNewPassword.getText().trim().isEmpty()){
                try {
                    adminService.updateAdminPassword(loggedAdmin.getEmail(), pwdResetPasswordSetNewPassword.getText().trim());
                    showAlert(Alert.AlertType.INFORMATION, "Password Update", "Success!", "Your password has been successfully changed.", AlertType.SHOWANDWAIT);
                    setPwdInitializationStates();
                } catch (RepositoryException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", e.getMessage(),AlertType.SHOW);
                }
            }else{
                showAlert(Alert.AlertType.ERROR, "Error", "Password Field is Empty", "Please enter your password before proceeding.", AlertType.SHOWANDWAIT);
            }
        }else{
            showAlert(Alert.AlertType.ERROR, "Error", "Passwords Do Not Match","The passwords entered do not match. Please try again.",AlertType.SHOWANDWAIT);
        }
    }

    @FXML
    void btnEditOnAction(ActionEvent event) {
        handleDashboardSidePanelBtnClicks(AdminDashboardViewType.EDIT);
    }

    @FXML
    void btnLoadCustomerReportOnAction(ActionEvent event) {
        openReports("src/main/resources/reports/Customer.jrxml");
    }

    @FXML
    void btnLoadEmployeeReportOnAction(ActionEvent event) {
        openReports("src/main/resources/reports/Employee.jrxml");
    }

    @FXML
    void btnLoadInventoryReportOnAction(ActionEvent event) {
        openReports("src/main/resources/reports/Inventory.jrxml");
    }

    @FXML
    void btnLoadSalesReportOnAction(ActionEvent event) {

    }

    @FXML
    void btnReportsOnAction(ActionEvent event) {
        handleDashboardSidePanelBtnClicks(AdminDashboardViewType.REPORTS);
    }

    @FXML
    void btnResetPasswordOnAction(ActionEvent event) {
        btnResetPassword.setDisable(true);
        btnResetPassword.setVisible(false);
        enableResetPasswordPane(true);
    }

    @FXML
    void btnSaveAdminOnAction(ActionEvent event) {
        String firstName = txtAdminSetFirstName.getText().trim();
        String lastName = txtAdminSetLastName.getText().trim();
        String email = txtAdminSetEmail.getText().trim();
        String contactNumber = txtAdminSetContactNumber.getText().trim();
        String userName = txtAdminSetUserName.getText().trim();
        String newPassword = txtAdminSetNewPassword.getText().trim();
        String confirmNewPassword = txtAdminConfirmNewPassword.getText().trim();

        if(validateAddAdmin(firstName,lastName,email,contactNumber,userName,newPassword,confirmNewPassword)){
            try {
                adminService.findByUserName(userName);
                adminService.findByEmail(email);

                showAlert(Alert.AlertType.ERROR, "Error", "UserName/Email Is Already Taken", "Try a different email username combination.", AlertType.SHOW);

            } catch (NoAdminFoundException e) {
                try {
                    adminService.save(new Admin(
                            null,
                            firstName,
                            lastName,
                            email,
                            userName,
                            contactNumber,
                            newPassword
                    ));

                    showAlert(Alert.AlertType.INFORMATION, "Save Admin", "Success!", "A new admin is created under the details provided.",AlertType.SHOWANDWAIT);
                    resetAddAdminLabels();
                    loadAdminTable(adminService.getAll());

                } catch (RepositoryException ex) {
                    showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", ex.getMessage(),AlertType.SHOW);
                }
            } catch (RepositoryException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", e.getMessage(),AlertType.SHOW);
            }
        }
    }

    @FXML
    void btnSaveEmployeeOnAction(ActionEvent event) {
        String firstName = txtSetEmployeeFirstName.getText().trim();
        String lastName = txtSetEmployeeLastName.getText().trim();
        String email = txtSetEmployeeEmail.getText().trim();
        String contactNumber = txtSetEmployeeContactNumber.getText().trim();
        String nic = txtSetEmployeeNIC.getText().trim();
        String address = txtSetEmployeeAddress.getText().trim();
        String userName = txtAdminSetUserName.getText().trim();
        String newPassword = txtAdminSetNewPassword.getText().trim();
        String confirmNewPassword = txtAdminConfirmNewPassword.getText().trim();

        if(validateAddEmployee(firstName, lastName, email, contactNumber, nic, address, userName, newPassword, confirmNewPassword)){
            try {
                employeeService.findByUserName(userName);
                employeeService.findByEmail(email);

                showAlert(Alert.AlertType.ERROR, "Error", "UserName/Email Is Already Taken", "Try a different email username combination.", AlertType.SHOW);

            } catch (NoEmployeeFoundException e) {
                try {
                    employeeService.save(new Employee(
                            null,
                            firstName,
                            lastName,
                            email,
                            contactNumber,
                            userName,
                            nic,
                            address,
                            newPassword
                    ));

                    showAlert(Alert.AlertType.CONFIRMATION, "Save Employee", "Success!", "A new employee is created under the details provided.", AlertType.SHOWANDWAIT);
                    resetAddEmployeeLabels();
                    loadEmployeeTable(employeeService.getAll());

                } catch (RepositoryException ex) {
                    showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", ex.getMessage(),AlertType.SHOW);
                }

            } catch (RepositoryException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", e.getMessage(),AlertType.SHOW);
            }
        }
    }

    @FXML
    void openGithubOnAction(MouseEvent event) {
        try {
            URI uri = new URI("https://github.com/Tharush2002");
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(uri);
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", e.getMessage(),AlertType.SHOW);
        }
    }

    @FXML
    void openInstagramOnAction(MouseEvent event) {

    }

    @FXML
    void openLinkedinOnAction(MouseEvent event) {
        try {
            URI uri = new URI("https://www.linkedin.com/in/tharusha-gunarathne-5b0b88330/");
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(uri);
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", e.getMessage(),AlertType.SHOW);
        }
    }

    @FXML
    void showAdminPwdConfirmNewPasswordOnAction(MouseEvent event) {
        enableAdminPwdConfirmNewPassword(true);
        enableAdminTxtConfirmNewPassword(false);
        pwdAdminConfirmNewPassword.requestFocus();
        pwdAdminConfirmNewPassword.positionCaret(pwdAdminConfirmNewPassword.getText().length());
    }

    @FXML
    void showAdminPwdSetNewPasswordOnAction(MouseEvent event) {
        enableAdminPwdSetNewPassword(true);
        enableAdminTxtSetNewPassword(false);
        pwdAdminSetNewPassword.requestFocus();
        pwdAdminSetNewPassword.positionCaret(pwdAdminSetNewPassword.getText().length());
    }

    @FXML
    void showAdminTxtConfirmNewPasswordOnAction(MouseEvent event) {
        enableAdminPwdConfirmNewPassword(false);
        enableAdminTxtConfirmNewPassword(true);
        txtAdminConfirmNewPassword.requestFocus();
        txtAdminConfirmNewPassword.positionCaret(txtAdminConfirmNewPassword.getText().length());
    }

    @FXML
    void showAdminTxtSetNewPasswordOnAction(MouseEvent event) {
        enableAdminPwdSetNewPassword(false);
        enableAdminTxtSetNewPassword(true);
        txtAdminSetNewPassword.requestFocus();
        txtAdminSetNewPassword.positionCaret(txtAdminSetNewPassword.getText().length());
    }

    @FXML
    void showEmployeePwdConfirmNewPasswordOnAction(MouseEvent event) {
        enableEmployeePwdConfirmNewPassword(true);
        enableEmployeeTxtConfirmNewPassword(false);
        pwdEmployeeConfirmNewPassword.requestFocus();
        pwdEmployeeConfirmNewPassword.positionCaret(pwdEmployeeConfirmNewPassword.getText().length());
    }

    @FXML
    void showEmployeePwdSetNewPasswordOnAction(MouseEvent event) {
        enableEmployeePwdSetNewPassword(true);
        enableEmployeeTxtSetNewPassword(false);
        pwdEmployeeSetNewPassword.requestFocus();
        pwdEmployeeSetNewPassword.positionCaret(pwdEmployeeSetNewPassword.getText().length());
    }

    @FXML
    void showEmployeeTxtConfirmNewPasswordOnAction(MouseEvent event) {
        enableEmployeePwdConfirmNewPassword(false);
        enableEmployeeTxtConfirmNewPassword(true);
        txtEmployeeConfirmNewPassword.requestFocus();
        txtEmployeeConfirmNewPassword.positionCaret(txtEmployeeConfirmNewPassword.getText().length());
    }

    @FXML
    void showEmployeeTxtSetNewPasswordOnAction(MouseEvent event) {
        enableEmployeePwdSetNewPassword(false);
        enableEmployeeTxtSetNewPassword(true);
        txtEmployeeSetNewPassword.requestFocus();
        txtEmployeeSetNewPassword.positionCaret(txtEmployeeSetNewPassword.getText().length());
    }

    @FXML
    void showResetPasswordPwdConfirmNewPasswordOnAction(MouseEvent event) {
        enableResetPasswordPwdConfirmNewPassword(true);
        enableResetPasswordTxtConfirmNewPassword(false);
        pwdResetPasswordConfirmNewPassword.requestFocus();
        pwdResetPasswordConfirmNewPassword.positionCaret(pwdResetPasswordConfirmNewPassword.getText().length());
    }

    @FXML
    void showResetPasswordPwdSetNewPasswordOnAction(MouseEvent event) {
        enableResetPasswordPwdSetNewPassword(true);
        enableResetPasswordTxtSetNewPassword(false);
        pwdResetPasswordSetNewPassword.requestFocus();
        pwdResetPasswordSetNewPassword.positionCaret(pwdResetPasswordSetNewPassword.getText().length());
    }

    @FXML
    void showResetPasswordTxtConfirmNewPasswordOnAction(MouseEvent event) {
        enableResetPasswordPwdConfirmNewPassword(false);
        enableResetPasswordTxtConfirmNewPassword(true);
        txtResetPasswordConfirmNewPassword.requestFocus();
        txtResetPasswordConfirmNewPassword.positionCaret(txtResetPasswordConfirmNewPassword.getText().length());
    }

    @FXML
    void showResetPasswordTxtSetNewPasswordOnAction(MouseEvent event) {
        enableResetPasswordPwdSetNewPassword(false);
        enableResetPasswordTxtSetNewPassword(true);
        txtResetPasswordSetNewPassword.requestFocus();
        txtResetPasswordSetNewPassword.positionCaret(txtResetPasswordSetNewPassword.getText().length());
    }

    @FXML
    void validateAdminContactNumberOnAction(KeyEvent event) {
        String input = txtAdminSetContactNumber.getText();

        if (!input.matches("\\d*")) {
            txtAdminSetContactNumber.setText(input.replaceAll("[^\\d]", ""));
            txtAdminSetContactNumber.positionCaret(txtAdminSetContactNumber.getText().length()); // Keep cursor position
        }
    }

    @FXML
    void validateEmployeeContactNumberOnAction(KeyEvent event) {
        String input = txtSetEmployeeContactNumber.getText();

        if (!input.matches("\\d*")) {
            txtSetEmployeeContactNumber.setText(input.replaceAll("[^\\d]", ""));
            txtSetEmployeeContactNumber.positionCaret(txtSetEmployeeContactNumber.getText().length()); // Keep cursor position
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        handleDashboardSidePanelBtnClicks(AdminDashboardViewType.REPORTS);
        loadDateAndTime();
        loadTables();
        bindPasswordFields();
        setPwdInitializationStates();
    }

    private void loadTables() {
        try {
            loadAdminTable(adminService.getAll());
            loadEmployeeTable(employeeService.getAll());
        } catch (RepositoryException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", e.getMessage(),AlertType.SHOW);
        }
    }

    public void loadEmployeeTable(ObservableList<Employee> employeeObservableList) {
        if (!employeeObservableList.isEmpty()) {
            columnEmployeeEmployeeId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
            columnEmployeeEmployeeName.setCellValueFactory(cellData -> {
                String firstName = cellData.getValue().getFirstName() != null ? cellData.getValue().getFirstName() : "";
                String lastName = cellData.getValue().getLastName() != null ? cellData.getValue().getLastName() : "";
                return new SimpleStringProperty(String.format("%s %s",firstName,lastName));
            });

            columnEmployeeEmployeeId.setStyle("-fx-alignment:center;");
            columnEmployeeEmployeeName.setStyle("-fx-alignment:center;");

            setActionsToTables(UserType.EMPLOYEE);
            tblEmployee.setItems(employeeObservableList);
        }
    }

    public void loadAdminTable(ObservableList<Admin> adminObservableList) {
        if (!adminObservableList.isEmpty()) {
            columnAdminAdminId.setCellValueFactory(new PropertyValueFactory<>("adminId"));
            columnAdminAdminName.setCellValueFactory(cellData -> {
                String firstName = cellData.getValue().getFirstName() != null ? cellData.getValue().getFirstName() : "";
                String lastName = cellData.getValue().getLastName() != null ? cellData.getValue().getLastName() : "";
                return new SimpleStringProperty(String.format("%s %s",firstName,lastName));
            });

            columnAdminAdminId.setStyle("-fx-alignment:center;");
            columnAdminAdminName.setStyle("-fx-alignment:center;");

            setActionsToTables(UserType.ADMIN);
            tblAdmin.setItems(adminObservableList);
        }
    }

    private void loadDateAndTime() {
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        lblDate.setText(f.format(date));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime now = LocalTime.now();
            String formattedTime = now.format(formatter).replace("am", " A.M").replace("pm", " P.M");
            lblTime.setText(formattedTime);
        }),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void openReports(String fileName){
        try {
            JasperDesign jasperDesign = JRXmlLoader.load(fileName);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DBConnection.getInstance().getConnection());
            JasperViewer viewer = new JasperViewer(jasperPrint, false);
            viewer.setZoomRatio(0.5f);
            viewer.setVisible(true);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred while generating the report.", e.getMessage(),AlertType.SHOW);
        }
    }

    private void handleDashboardSidePanelBtnClicks(AdminDashboardViewType type) {
        switch (type) {
            case REPORTS:
//                HANDLE BUTTON STYLES WHEN CLICKED
                btnReports.setStyle("-fx-background-color: rgba(44, 37, 14, 0.4);");
                btnAddAdmin.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
                btnAddEmployee.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
                btnEdit.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
                btnAdminDetails.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
//                ENABLE AND SHOW ANCHOR PANE CATALOG
                anchorPaneReports.setDisable(false);
                anchorPaneReports.setVisible(true);
//                DISABLE AND HIDE REMAINING ANCHOR PANES
                anchorPaneAddAdmin.setDisable(true);
                anchorPaneAddEmployee.setDisable(true);
                anchorPaneEdit.setDisable(true);
                anchorPaneDetails.setDisable(true);

                anchorPaneAddAdmin.setVisible(false);
                anchorPaneAddEmployee.setVisible(false);
                anchorPaneEdit.setVisible(false);
                anchorPaneDetails.setVisible(false);
                break;
            case ADMIN:
//                HANDLE BUTTON STYLES WHEN CLICKED
                btnAddAdmin.setStyle("-fx-background-color: rgba(44, 37, 14, 0.4);");
                btnReports.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
                btnAddEmployee.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
                btnEdit.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
                btnAdminDetails.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
//                ENABLE AND SHOW ANCHOR PANE CATALOG
                anchorPaneAddAdmin.setDisable(false);
                anchorPaneAddAdmin.setVisible(true);
//                DISABLE AND HIDE REMAINING ANCHOR PANES
                anchorPaneReports.setDisable(true);
                anchorPaneAddEmployee.setDisable(true);
                anchorPaneEdit.setDisable(true);
                anchorPaneDetails.setDisable(true);

                anchorPaneReports.setVisible(false);
                anchorPaneAddEmployee.setVisible(false);
                anchorPaneEdit.setVisible(false);
                anchorPaneDetails.setVisible(false);

                resetAddAdminLabels();
                setAdminPwdInitializationStates();
                break;
            case EMPLOYEE:
//                HANDLE BUTTON STYLES WHEN CLICKED
                btnAddEmployee.setStyle("-fx-background-color: rgba(44, 37, 14, 0.4);");
                btnReports.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
                btnAddAdmin.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
                btnEdit.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
                btnAdminDetails.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
//                ENABLE AND SHOW ANCHOR PANE CATALOG
                anchorPaneAddEmployee.setDisable(false);
                anchorPaneAddEmployee.setVisible(true);
//                DISABLE AND HIDE REMAINING ANCHOR PANES
                anchorPaneReports.setDisable(true);
                anchorPaneAddAdmin.setDisable(true);
                anchorPaneEdit.setDisable(true);
                anchorPaneDetails.setDisable(true);

                anchorPaneReports.setVisible(false);
                anchorPaneAddAdmin.setVisible(false);
                anchorPaneEdit.setVisible(false);
                anchorPaneDetails.setVisible(false);

                resetAddEmployeeLabels();
                setEmployeePwdInitializationStates();
                break;
            case EDIT:
//                HANDLE BUTTON STYLES WHEN CLICKED
                btnEdit.setStyle("-fx-background-color: rgba(44, 37, 14, 0.4);");
                btnReports.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
                btnAddAdmin.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
                btnAddEmployee.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
                btnAdminDetails.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
//                ENABLE AND SHOW ANCHOR PANE CATALOG
                anchorPaneEdit.setDisable(false);
                anchorPaneEdit.setVisible(true);
//                DISABLE AND HIDE REMAINING ANCHOR PANES
                anchorPaneReports.setDisable(true);
                anchorPaneAddAdmin.setDisable(true);
                anchorPaneAddEmployee.setDisable(true);
                anchorPaneDetails.setDisable(true);

                anchorPaneReports.setVisible(false);
                anchorPaneAddAdmin.setVisible(false);
                anchorPaneAddEmployee.setVisible(false);
                anchorPaneDetails.setVisible(false);
                break;
            case DETAILS:
//                HANDLE BUTTON STYLES WHEN CLICKED
                btnAdminDetails.setStyle("-fx-background-color: rgba(44, 37, 14, 0.4);");
                btnReports.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
                btnAddAdmin.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
                btnAddEmployee.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
                btnEdit.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
//                ENABLE AND SHOW ANCHOR PANE CATALOG
                anchorPaneDetails.setDisable(false);
                anchorPaneDetails.setVisible(true);
//                DISABLE AND HIDE REMAINING ANCHOR PANES
                anchorPaneReports.setDisable(true);
                anchorPaneAddAdmin.setDisable(true);
                anchorPaneAddEmployee.setDisable(true);
                anchorPaneEdit.setDisable(true);

                anchorPaneReports.setVisible(false);
                anchorPaneAddAdmin.setVisible(false);
                anchorPaneAddEmployee.setVisible(false);
                anchorPaneEdit.setVisible(false);

                setPwdInitializationStates();
                break;
        }
        resetTables();
    }

    private void setActionsToTables(UserType type) {
        Callback<TableColumn<Admin, Void>, TableCell<Admin, Void>> adminCellFactory = new Callback<>() {
            @Override
            public TableCell<Admin, Void> call(final TableColumn<Admin, Void> param) {
                return new TableCell<>() {
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            Admin currentAdmin = tblAdmin.getItems().get(getIndex());

                            Button editIcon = new Button("Edit");
                            Button deleteIcon = new Button("Delete");

                            deleteIcon.setStyle("-fx-cursor: hand ;");
                            editIcon.setStyle("-fx-cursor: hand ;");

                            deleteIcon.setOnMouseClicked(event -> handleDeleteActions(type, tblAdmin.getItems().get(getIndex())));

                            editIcon.setOnMouseClicked(event -> {
                                selectedAdminToEdit = tblAdmin.getItems().get(getIndex());
                                handleEditActions(type, event);
                            });

                            HBox managebtn = new HBox();
                            managebtn.setStyle("-fx-alignment:center");
                            managebtn.setSpacing(8);

                            if (!currentAdmin.getAdminId().equals(loggedAdmin.getAdminId())) {
                                managebtn.getChildren().addAll(editIcon, deleteIcon);
                            } else {
                                managebtn.getChildren().add(editIcon);
                            }

                            setGraphic(managebtn);
                        }
                    }
                };
            }
        };

        Callback<TableColumn<Employee, Void>, TableCell<Employee, Void>> employeeCellFactory = new Callback<>() {
            @Override
            public TableCell<Employee, Void> call(final TableColumn<Employee, Void> param) {
                return new TableCell<>() {
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            Button editIcon = new Button("Edit");
                            Button deleteIcon = new Button("Delete");

                            deleteIcon.setStyle("-fx-cursor: hand;");
                            editIcon.setStyle("-fx-cursor: hand;");

                            deleteIcon.setOnMouseClicked(event -> handleDeleteActions(type, tblEmployee.getItems().get(getIndex())));

                            editIcon.setOnMouseClicked(event -> {
                                selectedEmployeeToEdit = tblEmployee.getItems().get(getIndex());
                                handleEditActions(type, event);
                            });

                            HBox managebtn = new HBox(editIcon, deleteIcon);
                            managebtn.setStyle("-fx-alignment:center");
                            managebtn.setSpacing(8);
                            setGraphic(managebtn);
                        }
                    }
                };
            }
        };

        if(type == UserType.ADMIN){
            columnAdminActions.setCellFactory(adminCellFactory);
        }else{
            columnEmployeeActions.setCellFactory(employeeCellFactory);
        }
    }

    private void handleDeleteActions(UserType type, Object object) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Are you sure you want to delete the selected?");
        alert.setContentText("Please confirm your action.");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try{
                if (type == UserType.ADMIN) {
                    Admin admin = (Admin) object;
                    adminService.deleteById(admin.getAdminId());
                    loadAdminTable(adminService.getAll());
                } else if (type == UserType.EMPLOYEE) {
                    Employee employee = (Employee) object;
                    employeeService.deleteById(employee.getEmployeeId());
                    loadEmployeeTable(employeeService.getAll());
                }

                showAlert(Alert.AlertType.INFORMATION, "Deletion Successful", null, "Selected property has been successfully deleted.", AlertType.SHOW);

            } catch (RepositoryException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", e.getMessage(),AlertType.SHOW);
            }
        }
    }

    private void handleEditActions(UserType type, MouseEvent event) {
        setEmployeeDashboardStage(event);
        try {
            Stage stage = new Stage();
            new FXMLLoader();
            FXMLLoader loader = switch (type) {
                case ADMIN -> new FXMLLoader(getClass().getResource("../../view/EditAdmin.fxml"));
                case EMPLOYEE -> new FXMLLoader(getClass().getResource("../../view/EditEmployee.fxml"));
            };
            Parent root = loader.load();
            if (type == UserType.ADMIN) {
                EditAdminFormController editAdminFormController = loader.getController();
                editAdminFormController.setAdminDashboardFormController(this);
            } else {
                EditEmployeeFormController editEmployeeFormController = loader.getController();
                editEmployeeFormController.setAdminDashboardFormController(this);
            }

            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
            stage.setResizable(false);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", e.getMessage(),AlertType.SHOW);
        }
        enableScreen();
    }

    private void enableScreen() {
        screen.setDisable(false);
        screen.setVisible(true);
    }

    private void closeAdminDashboard(MouseEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
        HomeFormController.getInstance().getHomeStage().show();
    }

    private void resetTables() {
        tblAdmin.getSelectionModel().clearSelection();
        tblEmployee.getSelectionModel().clearSelection();
    }

    public void loadAdminDetails(Admin admin) {
        loggedAdmin = admin;
        lblAdminId.setText(admin.getAdminId() != null ? admin.getAdminId():"");
        lblAdminUserName.setText(admin.getUserName() != null ? admin.getUserName():"");
        lblAdminName.setText(getAdminName(admin));
        lblAdminEmail.setText(admin.getEmail() != null ? admin.getEmail():"");
        lblAdminPhoneNumber.setText(admin.getPhoneNumber() != null ? admin.getPhoneNumber():"");
    }

    private String getAdminName(Admin admin){
        String firstName = admin.getFirstName() != null ? admin.getFirstName() : "";
        String lastName = admin.getLastName() != null ? admin.getLastName() : "";
        return firstName + " " + lastName;
    }

    private void resetAddAdminLabels(){
        txtAdminSetFirstName.setText("");
        txtAdminSetLastName.setText("");
        txtAdminSetEmail.setText("");
        txtAdminSetContactNumber.setText("");
        txtAdminSetUserName.setText("");
        pwdAdminSetNewPassword.setText("");
        pwdAdminConfirmNewPassword.setText("");
    }

    private void resetAddEmployeeLabels(){
        txtSetEmployeeFirstName.setText("");
        txtSetEmployeeLastName.setText("");
        txtSetEmployeeEmail.setText("");
        txtSetEmployeeContactNumber.setText("");
        txtSetEmployeeNIC.setText("");
        txtSetEmployeeAddress.setText("");
        txtSetEmployeeUserName.setText("");
        pwdEmployeeSetNewPassword.setText("");
        pwdEmployeeConfirmNewPassword.setText("");
    }

    //  PASSWORD VALIDATIONS - RESET PASSWORD

    private void enableResetPasswordTxtSetNewPassword(boolean state){
        txtResetPasswordSetNewPasswordPane.setDisable(!state);
        txtResetPasswordSetNewPasswordPane.setVisible(state);
    }

    private void enableResetPasswordTxtConfirmNewPassword(boolean state){
        txtResetPasswordConfirmNewPasswordPane.setDisable(!state);
        txtResetPasswordConfirmNewPasswordPane.setVisible(state);
    }

    private void enableResetPasswordPwdSetNewPassword(boolean state){
        pwdResetPasswordSetNewPasswordPane.setDisable(!state);
        pwdResetPasswordSetNewPasswordPane.setVisible(state);
    }

    private void enableResetPasswordPwdConfirmNewPassword(boolean state){
        pwdResetPasswordConfirmNewPasswordPane.setDisable(!state);
        pwdResetPasswordConfirmNewPasswordPane.setVisible(state);
    }

    private void enableResetPasswordPane(boolean state){
        resetPasswordPane.setDisable(!state);
        resetPasswordPane.setVisible(state);
    }

    private void setResetPasswordPwdInitializationStates(){
        btnResetPassword.setDisable(false);
        btnResetPassword.setVisible(true);
        enableResetPasswordPane(false);
        enableResetPasswordTxtConfirmNewPassword(false);
        enableResetPasswordTxtSetNewPassword(false);
        enableResetPasswordPwdConfirmNewPassword(true);
        enableResetPasswordPwdSetNewPassword(true);
        pwdResetPasswordConfirmNewPassword.setText("");
        pwdResetPasswordSetNewPassword.setText("");
    }

    // ------------------------------------------------------------

    //  PASSWORD VALIDATIONS - ADD ADMIN

    private void enableAdminTxtSetNewPassword(boolean state){
        txtAdminSetNewPasswordPane.setDisable(!state);
        txtAdminSetNewPasswordPane .setVisible(state);
    }

    private void enableAdminTxtConfirmNewPassword(boolean state){
        txtAdminConfirmNewPasswordPane.setDisable(!state);
        txtAdminConfirmNewPasswordPane.setVisible(state);
    }

    private void enableAdminPwdSetNewPassword(boolean state){
        pwdAdminSetNewPasswordPane.setDisable(!state);
        pwdAdminSetNewPasswordPane.setVisible(state);
    }

    private void enableAdminPwdConfirmNewPassword(boolean state){
        pwdAdminConfirmNewPasswordPane.setDisable(!state);
        pwdAdminConfirmNewPasswordPane.setVisible(state);
    }

    private void setAdminPwdInitializationStates(){
        enableAdminTxtConfirmNewPassword(false);
        enableAdminTxtSetNewPassword(false);
        enableAdminPwdConfirmNewPassword(true);
        enableAdminPwdSetNewPassword(true);
        pwdAdminConfirmNewPassword.setText("");
        pwdAdminSetNewPassword.setText("");
    }

    // ------------------------------------------------------------

    //  PASSWORD VALIDATIONS - ADD EMPLOYEE

    private void enableEmployeeTxtSetNewPassword(boolean state){
        txtEmployeeSetNewPasswordPane.setDisable(!state);
        txtEmployeeSetNewPasswordPane .setVisible(state);
    }

    private void enableEmployeeTxtConfirmNewPassword(boolean state){
        txtEmployeeConfirmNewPasswordPane.setDisable(!state);
        txtEmployeeConfirmNewPasswordPane.setVisible(state);
    }

    private void enableEmployeePwdSetNewPassword(boolean state){
        pwdEmployeeSetNewPasswordPane.setDisable(!state);
        pwdEmployeeSetNewPasswordPane.setVisible(state);
    }

    private void enableEmployeePwdConfirmNewPassword(boolean state){
        pwdEmployeeConfirmNewPasswordPane.setDisable(!state);
        pwdEmployeeConfirmNewPasswordPane.setVisible(state);
    }

    private void setEmployeePwdInitializationStates(){
        enableEmployeeTxtConfirmNewPassword(false);
        enableEmployeeTxtSetNewPassword(false);
        enableEmployeePwdConfirmNewPassword(true);
        enableEmployeePwdSetNewPassword(true);
        pwdEmployeeConfirmNewPassword.setText("");
        pwdEmployeeSetNewPassword.setText("");
    }

    // ------------------------------------------------------------

    private void setPwdInitializationStates(){
        setResetPasswordPwdInitializationStates();
        setAdminPwdInitializationStates();
        setEmployeePwdInitializationStates();
    }

    private void bindPasswordFields(){
        txtResetPasswordSetNewPassword.textProperty().bindBidirectional(pwdResetPasswordSetNewPassword.textProperty());
        txtResetPasswordConfirmNewPassword.textProperty().bindBidirectional(pwdResetPasswordConfirmNewPassword.textProperty());

        txtEmployeeSetNewPassword.textProperty().bindBidirectional(pwdEmployeeSetNewPassword.textProperty());
        txtEmployeeConfirmNewPassword.textProperty().bindBidirectional(pwdEmployeeConfirmNewPassword.textProperty());

        txtAdminSetNewPassword.textProperty().bindBidirectional(pwdAdminSetNewPassword.textProperty());
        txtAdminConfirmNewPassword.textProperty().bindBidirectional(pwdAdminConfirmNewPassword.textProperty());
    }

    private boolean validateAddEmployee(String firstName, String lastName, String email, String contactNumber, String nic, String address, String userName, String newPassword, String confirmNewPassword){

        if(firstName.isEmpty() && lastName.isEmpty() && email.isEmpty() && contactNumber.isEmpty() && nic.isEmpty() && address.isEmpty() && userName.isEmpty() && newPassword.isEmpty() && confirmNewPassword.isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Error", "Empty Fields Found", "Please fill all the fields and try again.", AlertType.SHOW);
            return false;
        } else if(!newPassword.equals(confirmNewPassword)){
            showAlert(Alert.AlertType.ERROR, "Error", "Passwords Do Not Match", "The passwords entered do not match. Please try again.", AlertType.SHOW);
            return false;
        } else if(!employeeService.isValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid Email", """
                    The email you entered is invalid.
                    
                    Hint: Ensure the email includes '@' and a domain like '.com'.
                    Example: user@example.com""", AlertType.SHOW);
            return false;
        } else if(!employeeService.isValidContactNumber(contactNumber)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid Contact Number", """
                    The contact number you entered is invalid.
                    
                    Hint:
                    - Ensure the number contains exactly 10 digits.
                    - Optionally, you can include a country code at the beginning (e.g., +1 or +91).
                    - Example with country code: +1234567890
                    - Example without country code: 0123456789
                    """, AlertType.SHOW);
            return false;
        } else if(!employeeService.isValidNIC(nic)){
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid NIC", """
                    The NIC you entered is invalid.
                    
                    Hint:
                    - NIC should either:
                      - Contain exactly 13 digits (e.g., 1234567890123), or
                      - Contain 9 digits followed by "V" or "v" (e.g., 123456789V or 123456789v).
                    - Ensure there are no spaces or special characters.
                    """, AlertType.SHOW);
            return false;
        } else if(userName.length()<8){
            showAlert(Alert.AlertType.ERROR, "Error", "UserName Is Too Short", "The minimum username length is 8 characters.", AlertType.SHOW);
            return false;
        }else if(newPassword.length()<8) {
            showAlert(Alert.AlertType.ERROR, "Error", "Password Is Too Short", "The minimum password length is 8 characters.", AlertType.SHOW);
            return false;
        }
        return true;
    }

    private boolean validateAddAdmin(String firstName, String lastName, String email, String contactNumber, String userName, String newPassword, String confirmNewPassword){

        if(firstName.isEmpty() && lastName.isEmpty() && email.isEmpty() && contactNumber.isEmpty() && userName.isEmpty() && newPassword.isEmpty() && confirmNewPassword.isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Error", "Empty Fields Found", "Please fill all the fields and try again.", AlertType.SHOW);
            return false;
        } else if(!newPassword.equals(confirmNewPassword)){
            showAlert(Alert.AlertType.ERROR, "Error", "Passwords Do Not Match", "The passwords entered do not match. Please try again.", AlertType.SHOW);
            return false;
        } else if(!adminService.isValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid Email", """
                    The email you entered is invalid.
                    
                    Hint: Ensure the email includes '@' and a domain like '.com'.
                    Example: user@example.com""", AlertType.SHOW);
            return false;
        } else if(!adminService.isValidContactNumber(contactNumber)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid Contact Number", """
                    The contact number you entered is invalid.
                    
                    Hint:
                    - Ensure the number contains exactly 10 digits.
                    - Optionally, you can include a country code at the beginning (e.g., +1 or +91).
                    - Example with country code: +1234567890
                    - Example without country code: 0123456789
                    """, AlertType.SHOW);
            return false;
        } else if(userName.length()<8){
            showAlert(Alert.AlertType.ERROR, "Error", "UserName Is Too Short", "The minimum username length is 8 characters.", AlertType.SHOW);
            return false;
        }else if(newPassword.length()<8) {
            showAlert(Alert.AlertType.ERROR, "Error", "Password Is Too Short", "The minimum password length is 8 characters.", AlertType.SHOW);
            return false;
        }
        return true;
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
