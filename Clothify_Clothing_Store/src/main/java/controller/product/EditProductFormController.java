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
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lombok.Setter;
import model.Product;
import service.ServiceFactory;
import service.custom.CategoryService;
import service.custom.ProductService;
import service.custom.SupplierService;
import util.AlertType;
import util.Type;

import java.net.URL;
import java.util.ResourceBundle;

@Setter
public class EditProductFormController implements Initializable {
    private EmployeeDashboardFormController employeeDashboardFormController;

    private final SupplierService supplierService = ServiceFactory.getInstance().getServiceType(Type.SUPPLIER);
    private final ProductService productService = ServiceFactory.getInstance().getServiceType(Type.PRODUCT);
    private final CategoryService categoryService = ServiceFactory.getInstance().getServiceType(Type.CATEGORY);
    private final Product selectedProductToEdit = EmployeeDashboardFormController.getInstance().getSelectedProductToEdit();

    @FXML
    public JFXComboBox<String> cmbEditProductCategory;

    @FXML
    private JFXComboBox<String> cmbEditProductSupplier;

    @FXML
    private Label lblEditProductID;

    @FXML
    private Spinner<Integer> spinnerEditProductQuantity;

    @FXML
    private JFXTextField txtEditProductName;

    @FXML
    private JFXTextField txtEditProductUnitPrice;

    @FXML
    void btnRemoveSupplierOnAction(ActionEvent event) {
        cmbEditProductSupplier.setValue("");
    }

    @FXML
    void btnCancelEditProductsOnAction(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
        Scene scene = EmployeeDashboardFormController.getInstance().getEmployeeDashboardStage().getScene();
        AnchorPane root = (AnchorPane) scene.getRoot();
        VBox vbox = (VBox) root.getChildren().get(7);
        vbox.setVisible(false);
        vbox.setDisable(true);
    }

    @FXML
    void btnSaveChangesOnAction(ActionEvent event) {
        try {
            String name = txtEditProductName.getText().trim();
            String unitPrice = txtEditProductUnitPrice.getText().trim();
            String categoryName = cmbEditProductCategory.getValue().trim();
            String supplierName = cmbEditProductSupplier.getValue().trim();
            if (!name.isEmpty() && !categoryName.isEmpty() && !unitPrice.isEmpty()) {
                selectedProductToEdit.setName(name);
                selectedProductToEdit.setQuantity(spinnerEditProductQuantity.getValue());
                selectedProductToEdit.setUnitPrice(Double.parseDouble(unitPrice));
                selectedProductToEdit.setCategory(categoryService.findCategoryByName(categoryName));

                if (!supplierName.isEmpty()) {
                    selectedProductToEdit.setSupplier(supplierService.findSupplierByName(cmbEditProductSupplier.getValue()));
                } else {
                    selectedProductToEdit.setSupplier(null);
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(null);
                alert.setContentText("Please Enter All the Fields with Correct Data");
                alert.show();
                return;
            }
            productService.update(selectedProductToEdit);
            btnCancelEditProductsOnAction(event);
            employeeDashboardFormController.loadCatalogProductsTable(productService.getAllProducts());
            employeeDashboardFormController.setCatalogPaneLabels();
        } catch (RepositoryException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", e.getMessage(),AlertType.SHOW);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR,"Error","Invalid Data !", "Please Enter Correct Data", AlertType.SHOW);
            setInitialProductData();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setInitialProductData();
    }

    private void setInitialProductData(){
        try{
            lblEditProductID.setText(selectedProductToEdit.getProductId());
            txtEditProductName.setText(selectedProductToEdit.getName());
            spinnerEditProductQuantity.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,750,selectedProductToEdit.getQuantity()));
            txtEditProductUnitPrice.setText(selectedProductToEdit.getUnitPrice().toString());

            ObservableList<String> categoryList= FXCollections.observableArrayList();
            categoryService.getAllCategories().forEach(category -> categoryList.add(category.getName()));
            cmbEditProductCategory.setItems(categoryList);
            if(selectedProductToEdit.getCategory()!=null) cmbEditProductCategory.setValue(selectedProductToEdit.getCategory().getName());

            ObservableList<String> supplierList= FXCollections.observableArrayList();
            supplierService.getAllSuppliers().forEach(supplier -> supplierList.add(supplier.getName()));
            cmbEditProductSupplier.setItems(supplierList);
            if(selectedProductToEdit.getSupplier()!=null) cmbEditProductSupplier.setValue(selectedProductToEdit.getSupplier().getName());
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
