package controller.supplier;

import com.jfoenix.controls.JFXTextField;
import controller.employee.EmployeeDashboardFormController;
import exceptions.RepositoryException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lombok.Setter;
import model.Supplier;
import service.ServiceFactory;
import service.custom.ProductService;
import service.custom.SupplierService;
import util.AlertType;
import util.Type;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Setter
public class EditSupplierFormController implements Initializable {
    private EmployeeDashboardFormController employeeDashboardFormController;

    private final Supplier selectedSupplierToEdit = EmployeeDashboardFormController.getInstance().getSelectedSupplierToEdit();
    private final SupplierService supplierService = ServiceFactory.getInstance().getServiceType(Type.SUPPLIER);
    private final ProductService productService = ServiceFactory.getInstance().getServiceType(Type.PRODUCT);
    private final String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    @FXML
    private JFXTextField txtSetSetSupplierCompany;

    @FXML
    private JFXTextField txtSetSupplierEmail;

    @FXML
    private JFXTextField txtSetSupplierName;

    @FXML
    void btnCancelEditSupplierOnAction(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
        Scene scene = EmployeeDashboardFormController.getInstance().getEmployeeDashboardStage().getScene();
        AnchorPane root = (AnchorPane) scene.getRoot();
        VBox vbox = (VBox) root.getChildren().get(7);
        vbox.setVisible(false);
        vbox.setDisable(true);
    }

    @FXML
    void btnSaveSupplierOnAction(ActionEvent event) {
        try{
            String name = txtSetSupplierName.getText().trim();
            String company = txtSetSetSupplierCompany.getText().trim();
            String email = txtSetSupplierEmail.getText().trim();
            if(!name.isEmpty() && isValidEmail(email) && !company.isEmpty()){
                selectedSupplierToEdit.setName(name);
                selectedSupplierToEdit.setCompany(company);
                selectedSupplierToEdit.setEmail(email);
                supplierService.updateSupplier(selectedSupplierToEdit);
                btnCancelEditSupplierOnAction(event);
                employeeDashboardFormController.loadSuppliersTable(supplierService.getAllSuppliers());
                employeeDashboardFormController.loadCatalogProductsTable(productService.getAllProducts());
                employeeDashboardFormController.setSuppliersPaneLabels();
                return;
            }
            showAlert(Alert.AlertType.ERROR,"Error","Invallid Data !","Please Enter Correct Data",AlertType.SHOW);
            setInitialState();
        } catch (RepositoryException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", e.getMessage(),AlertType.SHOW);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setInitialState();
    }

    private void setInitialState() {
        txtSetSupplierName.setText(selectedSupplierToEdit.getName());
        txtSetSetSupplierCompany.setText(selectedSupplierToEdit.getCompany());
        txtSetSupplierEmail.setText(selectedSupplierToEdit.getEmail());
    }

    private boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
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
