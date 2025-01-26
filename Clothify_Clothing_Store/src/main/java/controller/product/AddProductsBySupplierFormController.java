package controller.product;

import com.jfoenix.controls.JFXTextField;
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
import util.AlertType;
import util.Type;

import java.net.URL;
import java.util.ResourceBundle;

@Setter
public class AddProductsBySupplierFormController implements Initializable {
    private EmployeeDashboardFormController employeeDashboardFormController;
    private final CategoryService categoryService = ServiceFactory.getInstance().getServiceType(Type.CATEGORY);

    @FXML
    private ComboBox<Category> cmbSetProductCategory;

    @FXML
    private Spinner<Integer> spinnerSetProductQuantity;

    @FXML
    private TextField txtSetProductName;

    @FXML
    private TextField txtSetProductUnitPrice;


    @FXML
    void btnAddProductOnAction(ActionEvent event) {
        try {
            String name = txtSetProductName.getText();
            Category category = cmbSetProductCategory.getValue();
            Integer quantity = spinnerSetProductQuantity.getValue();
            Double unitPrice = Double.parseDouble(txtSetProductUnitPrice.getText());
            if (!name.isEmpty()) {
                if(category==null) category=new Category();
                EmployeeDashboardFormController.getInstance().getSuppliedProducts().add(new Product(null, name, category, quantity, unitPrice, new Supplier()));
                employeeDashboardFormController.loadSuppliersAddProductsTable();
                btnCancelAddProductsOnAction(event);
            } else {
                showAlert(Alert.AlertType.ERROR,"Error","Empty Fields Found !","Please Enter Data For All Fields",AlertType.SHOW);
            }
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
        initializeCategoryComboBox();
        setSpinnerInitialValues();
    }

    private void setSpinnerInitialValues() {
        spinnerSetProductQuantity.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,750,1));
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
}
