package controller.product;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import controller.employee.EmployeeDashboardFormController;
import exceptions.RepositoryException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lombok.Setter;
import model.Category;
import model.Product;
import model.Supplier;
import service.ServiceFactory;
import service.custom.CategoryService;
import service.custom.ProductService;
import service.custom.SupplierService;
import util.AlertType;
import util.Type;

import java.net.URL;
import java.util.ResourceBundle;

@Setter
public class AddProductFormController implements Initializable {
    private EmployeeDashboardFormController employeeDashboardFormController;
    private final SupplierService supplierService = ServiceFactory.getInstance().getServiceType(Type.SUPPLIER);
    private final ProductService productService = ServiceFactory.getInstance().getServiceType(Type.PRODUCT);
    private final CategoryService categoryService = ServiceFactory.getInstance().getServiceType(Type.CATEGORY);

    @FXML
    private JFXComboBox<String> cmbSetProductSupplier;

    @FXML
    private Spinner<Integer> spinnerSetProductQuantity;

    @FXML
    private JFXTextField txtSetProductCategory;

    @FXML
    private JFXTextField txtSetProductName;

    @FXML
    private JFXTextField txtSetProductUnitPrice;

    @FXML
    void btnAddProductOnAction(ActionEvent event) {
        try {
            String name = txtSetProductName.getText().trim();
            String category = txtSetProductCategory.getText().trim();
            Double unitPrice = Double.parseDouble(txtSetProductUnitPrice.getText().trim());
            String supplier = cmbSetProductSupplier.getValue().trim();
            Integer quantity = spinnerSetProductQuantity.getValue();
            if (!name.isEmpty() && !category.isEmpty()) {
                Supplier supplierObject = new Supplier();
                if (!supplier.isEmpty()) {
                    supplierObject = supplierService.findSupplierByName(supplier);
                }

                Category newCategory = categoryService.findCategoryByName(category);
                if (newCategory.getCategoryId() == null) {
                    newCategory = new Category();
                    newCategory.setName(category);
                }

                productService.addProduct(new Product(null, name, newCategory, quantity, unitPrice, supplierObject));
                employeeDashboardFormController.loadCatalogProductsTable(productService.getAllProducts());
                employeeDashboardFormController.setCatalogPaneLabels();
                btnCancelAddProductsOnAction(event);
                return;
            }
            showAlert(Alert.AlertType.ERROR,"Error","Empty Fields Found !","Please Enter All the Fields with Correct Data", AlertType.SHOW);
        } catch (RepositoryException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", e.getMessage(), AlertType.SHOW);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR,"Error","Invalid Data !","Please Enter Correct Data",AlertType.SHOW);
        }
    }

    @FXML
    void btnCancelAddProductsOnAction(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
        Scene scene = EmployeeDashboardFormController.getInstance().getEmployeeDashboardStage().getScene();
        AnchorPane root = (AnchorPane) scene.getRoot();
        VBox vbox = (VBox) root.getChildren().get(7);
        vbox.setVisible(false);
        vbox.setDisable(true);
    }

    @FXML
    void btnRemoveSupplierOnAction(ActionEvent event) {
        cmbSetProductSupplier.setValue("");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            ObservableList<Supplier> supplierObservableList=supplierService.getAllSuppliers();
            ObservableList<String> supplierList= FXCollections.observableArrayList();
            supplierObservableList.forEach(supplier -> supplierList.add(supplier.getName()));
            cmbSetProductSupplier.setItems(supplierList);

            spinnerSetProductQuantity.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,750,1));
            cmbSetProductSupplier.setValue("");
        } catch (RepositoryException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", e.getMessage(),AlertType.SHOW);
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
