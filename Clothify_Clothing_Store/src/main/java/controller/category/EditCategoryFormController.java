package controller.category;

import controller.employee.EmployeeDashboardFormController;
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
import model.Category;
import model.Supplier;
import service.ServiceFactory;
import service.custom.CategoryService;
import service.custom.SupplierService;
import util.AlertType;
import util.Type;

import java.net.URL;
import java.util.ResourceBundle;

@Setter
public class EditCategoryFormController implements Initializable {
    private EmployeeDashboardFormController employeeDashboardFormController;

    private final Category selectedCategoryToEdit = EmployeeDashboardFormController.getInstance().getSelectedCategoryToEdit();
    private final CategoryService categoryService = ServiceFactory.getInstance().getServiceType(Type.CATEGORY);

    @FXML
    private Label lblCategoryId;

    @FXML
    private TextField txtSetCategoryName;

    @FXML
    void btnCancelEditCategoryOnAction(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
        Scene scene = EmployeeDashboardFormController.getInstance().getEmployeeDashboardStage().getScene();
        AnchorPane root = (AnchorPane) scene.getRoot();
        VBox vbox = (VBox) root.getChildren().get(8);
        vbox.setVisible(false);
        vbox.setDisable(true);
    }

    @FXML
    void btnSaveCategoryOnAction(ActionEvent event) {
        try{
            String name = txtSetCategoryName.getText().trim();

            if(!name.isEmpty()){
                selectedCategoryToEdit.setName(name);

                categoryService.updateCategory(selectedCategoryToEdit);

                btnCancelEditCategoryOnAction(event);

                employeeDashboardFormController.loadCategoriesTable(categoryService.getAllCategories());
                employeeDashboardFormController.setCategoriesPaneLabels();
                return;
            }
            showAlert(Alert.AlertType.ERROR,"Error","Invallid Data !","Please Enter Correct Data", AlertType.SHOW);
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
        lblCategoryId.setText(selectedCategoryToEdit.getCategoryId());
        txtSetCategoryName.setText(selectedCategoryToEdit.getName());
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
