package controller.admin;

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
import model.Admin;
import service.ServiceFactory;
import service.custom.AdminService;
import util.AlertType;
import util.Type;

import java.net.URL;
import java.util.ResourceBundle;

@Setter
public class EditAdminFormController implements Initializable {
    private final Admin selectedAdminToEdit = AdminDashboardFormController.getInstance().getSelectedAdminToEdit();
    private final AdminService adminService = ServiceFactory.getInstance().getServiceType(Type.ADMIN);
    private AdminDashboardFormController adminDashboardFormController;
    @FXML
    private Label lblAdminID;

    @FXML
    private TextField txtSetAdminContactNumber;

    @FXML
    private TextField txtSetAdminEmail;

    @FXML
    private TextField txtSetAdminFirstName;

    @FXML
    private TextField txtSetAdminLastName;

    @FXML
    private TextField txtSetAdminUserName;

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
        String firstName = txtSetAdminFirstName.getText().trim();
        String lastName = txtSetAdminLastName.getText().trim();
        String email = txtSetAdminEmail.getText().trim();
        String contactNumber = txtSetAdminContactNumber.getText().trim();
        String userName = txtSetAdminUserName.getText().trim();

        try {
            if (validateAdmin(firstName, lastName, email, contactNumber, userName)) {
                adminService.update(new Admin(
                        lblAdminID.getText(),
                        firstName,
                        lastName,
                        email,
                        userName,
                        contactNumber,
                        null
                ));
                showAlert(Alert.AlertType.INFORMATION, "Edit Admin", "Success!", "Selected admin details have been successfully updated.", AlertType.SHOWANDWAIT);

                adminDashboardFormController.loadAdminTable(adminService.getAll());
                btnCloseFormOnAction(event);
            }
        } catch (RepositoryException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", e.getMessage(), AlertType.SHOW);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeAdminDetails();
    }

    private void initializeAdminDetails() {
        lblAdminID.setText(selectedAdminToEdit.getAdminId() != null ? selectedAdminToEdit.getAdminId() : "");
        txtSetAdminFirstName.setText(selectedAdminToEdit.getFirstName() != null ? selectedAdminToEdit.getFirstName() : "");
        txtSetAdminLastName.setText(selectedAdminToEdit.getLastName() != null ? selectedAdminToEdit.getLastName() : "");
        txtSetAdminEmail.setText(selectedAdminToEdit.getEmail() != null ? selectedAdminToEdit.getEmail() : "");
        txtSetAdminContactNumber.setText(selectedAdminToEdit.getPhoneNumber() != null ? selectedAdminToEdit.getPhoneNumber() : "");
        txtSetAdminUserName.setText(selectedAdminToEdit.getUserName() != null ? selectedAdminToEdit.getUserName() : "");
    }

    private boolean validateAdmin(String firstName, String lastName, String email, String contactNumber, String userName) {

        if (firstName.isEmpty() && lastName.isEmpty() && email.isEmpty() && contactNumber.isEmpty() && userName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Empty Fields Found", "Please fill all the fields and try again.", AlertType.SHOW);
            return false;
        } else if (!adminService.isValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid Email", """
                    The email you entered is invalid.
                    
                    Hint: Ensure the email includes '@' and a domain like '.com'.
                    Example: user@example.com""", AlertType.SHOW);
            return false;
        } else if (!adminService.isValidContactNumber(contactNumber)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid Contact Number", """
                    The contact number you entered is invalid.
                    
                    Hint:
                    - Ensure the number contains exactly 10 digits.
                    - Optionally, you can include a country code at the beginning (e.g., +1 or +91).
                    - Example with country code: +1234567890
                    - Example without country code: 0123456789
                    """, AlertType.SHOW);
            return false;
        } else if (userName.length() < 8) {
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
        if (showType == AlertType.SHOW) {
            alert.show();
        } else {
            alert.showAndWait();
        }
    }
}
