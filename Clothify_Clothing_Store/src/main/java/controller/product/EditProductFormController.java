package controller.product;

import controller.employee.EmployeeDashboardFormController;
import exceptions.RepositoryException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
public class EditProductFormController implements Initializable {
    private EmployeeDashboardFormController employeeDashboardFormController;

    private final SupplierService supplierService = ServiceFactory.getInstance().getServiceType(Type.SUPPLIER);
    private final ProductService productService = ServiceFactory.getInstance().getServiceType(Type.PRODUCT);
    private final CategoryService categoryService = ServiceFactory.getInstance().getServiceType(Type.CATEGORY);
    private final Product selectedProductToEdit = EmployeeDashboardFormController.getInstance().getSelectedProductToEdit();

    @FXML
    private ComboBox<Category> cmbEditProductCategory;

    @FXML
    private ComboBox<Supplier> cmbEditProductSupplier;

    @FXML
    private Label lblEditProductID;

    @FXML
    private Spinner<Integer> spinnerEditProductQuantity;

    @FXML
    private TextField txtEditProductName;

    @FXML
    private TextField txtEditProductUnitPrice;

    @FXML
    void btnCancelEditProductsOnAction(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
        Scene scene = EmployeeDashboardFormController.getInstance().getEmployeeDashboardStage().getScene();
        AnchorPane root = (AnchorPane) scene.getRoot();
        VBox vbox = (VBox) root.getChildren().get(8);
        vbox.setVisible(false);
        vbox.setDisable(true);
    }

    @FXML
    void btnSaveChangesOnAction(ActionEvent event) {
        try {
            String name = txtEditProductName.getText().trim();
            Double unitPrice = Double.parseDouble(txtEditProductUnitPrice.getText().trim());
            Category category = cmbEditProductCategory.getValue();
            Supplier supplier = cmbEditProductSupplier.getValue();
            if (!name.isEmpty()) {
                selectedProductToEdit.setName(name);
                selectedProductToEdit.setQuantity(spinnerEditProductQuantity.getValue());
                selectedProductToEdit.setUnitPrice(unitPrice);

                if (category != null) {
                    selectedProductToEdit.setCategory(categoryService.findByCategoryId(category.getCategoryId()));
                } else {
                    selectedProductToEdit.setCategory(new Category());
                }

                if (supplier != null) {
                    selectedProductToEdit.setSupplier(supplierService.findBySupplierId(supplier.getSupplierId()));
                } else {
                    selectedProductToEdit.setSupplier(new Supplier());
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
        initializeCategoryComboBox();
        initializeSupplierComboBox();
        initializeUnitPrice();
        setInitialProductData();
    }

    private void initializeUnitPrice() {
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*(\\.\\d*)?")) {
                return change;
            }
            return null;
        });

        txtEditProductUnitPrice.setTextFormatter(formatter);
    }

    private void setInitialProductData(){
        lblEditProductID.setText(selectedProductToEdit.getProductId());
        txtEditProductName.setText(selectedProductToEdit.getName());
        spinnerEditProductQuantity.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,750,selectedProductToEdit.getQuantity()));
        txtEditProductUnitPrice.setText(selectedProductToEdit.getUnitPrice().toString());

        int categoryIndex = 0;
        for (Category cmbEditProductCategoryItem : cmbEditProductCategory.getItems()) {
            if (cmbEditProductCategoryItem.getCategoryId().equals(selectedProductToEdit.getCategory().getCategoryId()))
                break;
            categoryIndex++;
        }
        cmbEditProductCategory.getSelectionModel().select(categoryIndex);

        int supplierIndex = 0;
        for (Supplier supplier : cmbEditProductSupplier.getItems()) {
            if (supplier.getSupplierId().equals(selectedProductToEdit.getSupplier().getSupplierId())) {
                break;
            }
            supplierIndex++;
        }
        cmbEditProductSupplier.getSelectionModel().select(supplierIndex);
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

    private void initializeSupplierComboBox() {
        try{
            cmbEditProductSupplier.setItems(supplierService.getAll());

            cmbEditProductSupplier.setCellFactory(listView -> new javafx.scene.control.ListCell<Supplier>() {
                @Override
                protected void updateItem(Supplier supplier, boolean empty) {
                    super.updateItem(supplier, empty);
                    setText((supplier == null || empty) ? null : supplier.getName());
                }
            });

            cmbEditProductSupplier.setButtonCell(new javafx.scene.control.ListCell<Supplier>() {
                @Override
                protected void updateItem(Supplier supplier, boolean empty) {
                    super.updateItem(supplier, empty);
                    setText((supplier == null || empty) ? null : supplier.getName());
                }
            });
        } catch (RepositoryException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", e.getMessage(),AlertType.SHOW);
        }
    }

    private void initializeCategoryComboBox() {
        try{
            cmbEditProductCategory.setItems(categoryService.getAllCategories());

            cmbEditProductCategory.setCellFactory(listView -> new javafx.scene.control.ListCell<Category>() {
                @Override
                protected void updateItem(Category category, boolean empty) {
                    super.updateItem(category, empty);
                    setText((category == null || empty) ? null : category.getName());
                }
            });

            cmbEditProductCategory.setButtonCell(new javafx.scene.control.ListCell<Category>() {
                @Override
                protected void updateItem(Category category, boolean empty) {
                    super.updateItem(category, empty);
                    setText((category == null || empty) ? null : category.getName());
                }
            });
        } catch (RepositoryException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", e.getMessage(),AlertType.SHOW);
        }
    }
}
