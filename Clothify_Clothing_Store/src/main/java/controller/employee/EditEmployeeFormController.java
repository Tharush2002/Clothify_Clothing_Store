package controller.employee;

import controller.admin.AdminDashboardFormController;
import exceptions.RepositoryException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lombok.Setter;
import model.Employee;
import service.ServiceFactory;
import service.custom.EmployeeService;
import util.AlertType;
import util.Type;

import java.net.URL;
import java.util.ResourceBundle;

@Setter
public class EditEmployeeFormController implements Initializable {
    private AdminDashboardFormController adminDashboardFormController;

    private final Employee selectedEmployeeToEdit = AdminDashboardFormController.getInstance().getSelectedEmployeeToEdit();

    private final EmployeeService employeeService = ServiceFactory.getInstance().getServiceType(Type.EMPLOYEE);

    @FXML
    private Label lblEmployeeID;

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
    void btnCloseFormOnAction(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
        Scene scene = AdminDashboardFormController.getInstance().getAdminDashboardStage().getScene();
        AnchorPane root = (AnchorPane) scene.getRoot();
        VBox vbox = (VBox) root.getChildren().get(8);
        vbox.setVisible(false);
        vbox.setDisable(true);
    }

    @FXML
    void btnSaveChangesOnAction(ActionEvent event) {
        String firstName = txtSetEmployeeFirstName.getText().trim();
        String lastName = txtSetEmployeeLastName.getText().trim();
        String email = txtSetEmployeeEmail.getText().trim();
        String contactNumber = txtSetEmployeeContactNumber.getText().trim();
        String address = txtSetEmployeeAddress.getText().trim();
        String userName = txtSetEmployeeUserName.getText().trim();

        try{
            if(validateEmployee(firstName,lastName,email,contactNumber,address,userName)){
                employeeService.update(new Employee(
                        lblEmployeeID.getText(),
                        firstName,
                        lastName,
                        email,
                        contactNumber,
                        userName,
                        selectedEmployeeToEdit.getNic(),
                        address,
                        selectedEmployeeToEdit.getPassword()
                ));
                showAlert(Alert.AlertType.INFORMATION, "Edit Employee", "Success!", "Selected employee details have been successfully updated.", AlertType.SHOWANDWAIT);
                adminDashboardFormController.loadEmployeeTable(employeeService.getAll());
                btnCloseFormOnAction(event);
            }
        } catch (RepositoryException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", e.getMessage(),AlertType.SHOW);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeEmployeeDetails();
    }

    private void initializeEmployeeDetails(){
        lblEmployeeID.setText(selectedEmployeeToEdit.getEmployeeId() != null ? selectedEmployeeToEdit.getEmployeeId():"");
        txtSetEmployeeFirstName.setText(selectedEmployeeToEdit.getFirstName() != null ? selectedEmployeeToEdit.getFirstName():"");
        txtSetEmployeeLastName.setText(selectedEmployeeToEdit.getLastName() != null ? selectedEmployeeToEdit.getLastName():"");
        txtSetEmployeeEmail.setText(selectedEmployeeToEdit.getEmail() != null ? selectedEmployeeToEdit.getEmail():"");
        txtSetEmployeeContactNumber.setText(selectedEmployeeToEdit.getPhoneNumber() != null ? selectedEmployeeToEdit.getPhoneNumber():"");
        txtSetEmployeeNIC.setText(selectedEmployeeToEdit.getNic() != null ? selectedEmployeeToEdit.getNic():"");
        txtSetEmployeeAddress.setText(selectedEmployeeToEdit.getAddress() != null ? selectedEmployeeToEdit.getAddress():"");
        txtSetEmployeeUserName.setText(selectedEmployeeToEdit.getUserName() != null ? selectedEmployeeToEdit.getUserName():"");
    }

    private boolean validateEmployee(String firstName, String lastName, String email, String contactNumber, String address, String userName){
        if(firstName.isEmpty() && lastName.isEmpty() && email.isEmpty() && contactNumber.isEmpty() && address.isEmpty() && userName.isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Error", "Empty Fields Found", "Please fill all the fields and try again.", AlertType.SHOW);
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
        } else if(userName.length()<8) {
            showAlert(Alert.AlertType.ERROR, "Error", "UserName Is Too Short", "The minimum username length is 8 characters.", AlertType.SHOW);
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
