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
public class AddProductFormController implements Initializable {
    private EmployeeDashboardFormController employeeDashboardFormController;
    private final SupplierService supplierService = ServiceFactory.getInstance().getServiceType(Type.SUPPLIER);
    private final ProductService productService = ServiceFactory.getInstance().getServiceType(Type.PRODUCT);
    private final CategoryService categoryService = ServiceFactory.getInstance().getServiceType(Type.CATEGORY);

    @FXML
    private ComboBox<Category> cmbSetProductCategory;

    @FXML
    private ComboBox<Supplier> cmbSetProductSupplier;

    @FXML
    private Spinner<Integer> spinnerSetProductQuantity;

    @FXML
    private TextField txtSetProductName;

    @FXML
    private TextField txtSetProductUnitPrice;

    @FXML
    void btnAddProductOnAction(ActionEvent event) {
        try {
            String name = txtSetProductName.getText().trim();
            Category category = cmbSetProductCategory.getValue();
            Double unitPrice = Double.parseDouble(txtSetProductUnitPrice.getText().trim());
            Supplier supplier = cmbSetProductSupplier.getValue();
            Integer quantity = spinnerSetProductQuantity.getValue();
            if (!name.isEmpty()) {
                if (supplier==null) supplier=new Supplier();
                if (category==null) category=new Category();

                productService.addProduct(new Product(null, name, category, quantity, unitPrice, supplier));
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
        VBox vbox = (VBox) root.getChildren().get(8);
        vbox.setVisible(false);
        vbox.setDisable(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        spinnerSetProductQuantity.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,750,1));

        initializeCategoryComboBox();
        initializeSupplierComboBox();
    }

    private void initializeSupplierComboBox() {
        try{
            cmbSetProductSupplier.setItems(supplierService.getAll());

            cmbSetProductSupplier.setCellFactory(listView -> new javafx.scene.control.ListCell<Supplier>() {
                @Override
                protected void updateItem(Supplier supplier, boolean empty) {
                    super.updateItem(supplier, empty);
                    setText((supplier == null || empty) ? null : supplier.getName());
                }
            });

            cmbSetProductSupplier.setButtonCell(new javafx.scene.control.ListCell<Supplier>() {
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
            cmbSetProductCategory.setItems(categoryService.getAllCategories());

            cmbSetProductCategory.setCellFactory(listView -> new javafx.scene.control.ListCell<Category>() {
                @Override
                protected void updateItem(Category category, boolean empty) {
                    super.updateItem(category, empty);
                    setText((category == null || empty) ? null : category.getName());
                }
            });

            cmbSetProductCategory.setButtonCell(new javafx.scene.control.ListCell<Category>() {
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
