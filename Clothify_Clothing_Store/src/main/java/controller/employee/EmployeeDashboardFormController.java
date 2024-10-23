package controller.employee;

import controller.HomeFormController;
import controller.product.AddProductFormController;
import controller.product.UpdateProductFormController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import model.Category;
import model.OrderItems;
import model.Product;
import model.Supplier;
import service.ServiceFactory;
import service.custom.ProductService;
import util.ActionTableType;
import util.DashboardViewType;
import util.Type;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;

public class EmployeeDashboardFormController implements Initializable {
    private static Stage employeeDashboardStage;

    private final ProductService productService=ServiceFactory.getInstance().getServiceType(Type.PRODUCT);
    private ObservableList<Product> allProducts= FXCollections.observableArrayList();
    private static Product selectedProductToEdit = new Product();
    private Product selectedProductToDelete = new Product();

    private List<OrderItems> orderItems=new ArrayList<>();

    public static Stage getEmployeeDashboardStage(){
        return employeeDashboardStage;
    }

    private void setEmployeeDashboardStage(MouseEvent event){
        employeeDashboardStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    private void setEmployeeDashboardStage(ActionEvent event){
        employeeDashboardStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    public static Product getSelectedProductToEdit(){
        return selectedProductToEdit;
    }

    @FXML
    public Button btnCheckOut;

    @FXML
    public Spinner<Integer> spinnerCatalogQuantity;

    @FXML
    public VBox screen;

    @FXML
    public TableView<Product> tblCatalogProducts;

    @FXML
    public Button btnAddToCart;

    @FXML
    private TableColumn<Product, Void> columnCatalogProductsAction;

    @FXML
    private TableColumn<Product, String> columnCatalogProductsCategoryID;

    @FXML
    private TableColumn<Product, String> columnCatalogProductsID;

    @FXML
    private TableColumn<Product, String> columnCatalogProductsName;

    @FXML
    private TableColumn<Product, Integer> columnCatalogProductsQuantity;

    @FXML
    private TableColumn<Product, String> columnCatalogProductsSupplierID;

    @FXML
    private TableColumn<Product, Double> columnCatalogProductsUnitPrice;

    @FXML
    private AnchorPane anchorPaneCatalog;

    @FXML
    private AnchorPane anchorPaneOrders;

    @FXML
    private AnchorPane anchorPaneReports;

    @FXML
    private AnchorPane anchorPaneSuppliers;

    @FXML
    private Button btnCatalog;

    @FXML
    private Button btnOrders;

    @FXML
    private Button btnReports;

    @FXML
    private Button btnSuppliers;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblTime;

    @FXML
    private ComboBox<String> cmbCatalogSize;

    @FXML
    private Label lblCatalogProductID;

    @FXML
    private Label lblCatalogProductName;

    @FXML
    private Label lblCatalogUnitPrice;

    @FXML
    void btnCloseOnAction(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
        HomeFormController.getHomeStage().show();
    }

    @FXML
    void btnCatalogOnAction(ActionEvent event) {
        handleDashboardSidePanelBtnClicks(DashboardViewType.CATALOG);
    }

    @FXML
    void btnOrdersOnAction(ActionEvent event) {
        handleDashboardSidePanelBtnClicks(DashboardViewType.ORDERS);
    }

    @FXML
    void btnReportsOnAction(ActionEvent event) {
        handleDashboardSidePanelBtnClicks(DashboardViewType.REPORTS);
    }

    @FXML
    void btnSuppliersOnAction(ActionEvent event) {
        handleDashboardSidePanelBtnClicks(DashboardViewType.SUPPLIERS);
    }

    @FXML
    void btnAddToCartOnAction(ActionEvent event) {
        try {
            new Alert(Alert.AlertType.INFORMATION).show();
            Product selectedProduct = null;
            L1:
            for (int index = 0; allProducts.size() > index; index++) {
                if (allProducts.get(index).getProductId().equals(lblCatalogProductID.getText())) {
                    selectedProduct = allProducts.get(index);
                    allProducts.get(index).setQuantity(allProducts.get(index).getQuantity() - spinnerCatalogQuantity.getValue());
                    break L1;
                }
            }
            for (int index = 0; orderItems.size() > index; index++) {
                boolean isProductIdMatches= orderItems.get(index).getProduct().getProductId().equals(lblCatalogProductID.getText());
                boolean isProductSizeMatches= orderItems.get(index).getSize().equals(cmbCatalogSize.getValue().toString());
                if (isProductSizeMatches && isProductIdMatches){
                    orderItems.get(index).setQuantity(orderItems.get(index).getQuantity()+spinnerCatalogQuantity.getValue());
                    return;
                }
            }
            orderItems.add(new OrderItems(null, selectedProduct, selectedProduct.getName(), spinnerCatalogQuantity.getValue(), spinnerCatalogQuantity.getValue() * selectedProduct.getUnitPrice(), cmbCatalogSize.getValue()));
        }finally {
            btnCheckOut.setDisable(false);
            loadCatalogProductsTable(allProducts);
            resetProductValuesDisplay();

//            System.out.println(orderItems);
//            System.out.println(orderItems.size());
//            System.out.println("====================================================");
        }
    }

    @FXML
    public void btnCheckoutOnAction(ActionEvent event) {
    }

    @FXML
    public void btnCatalogAddProductOnAction(ActionEvent event) {
        try {
            setEmployeeDashboardStage(event);
            Stage stage=new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../view/AddProducts.fxml"));
            Parent root = loader.load();
            AddProductFormController controller = loader.getController();
            controller.setMainController(this);
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
            stage.setResizable(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        enableScreen();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        allProducts=productService.getAllProducts();

        loadDateAndTime();
        loadCatalogProductsTable(allProducts);
        handleDashboardSidePanelBtnClicks(DashboardViewType.CATALOG);
        tblCatalogProducts.getSelectionModel().selectedItemProperty().addListener((observableValue, oldVal, newVal) -> {
            if (newVal != null) {
                addProductValuesToDisplay(newVal);
            }
        });
    }

    // MY IMPLEMENTATIONS

    private void handleDashboardSidePanelBtnClicks(DashboardViewType type){
        switch(type){
            case CATALOG:
//                HANDLE BUTTON STYLES WHEN CLICKED
                btnCatalog.setStyle("-fx-background-color: rgba(44, 37, 14, 0.4);");
                btnOrders.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
                btnSuppliers.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
                btnReports.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
//                ENABLE AND SHOW ANCHOR PANE CATALOG
                anchorPaneCatalog.setDisable(false);
                anchorPaneCatalog.setVisible(true);
//                DISABLE AND HIDE REMAINING ANCHOR PANES
                anchorPaneOrders.setDisable(true);
                anchorPaneReports.setDisable(true);
                anchorPaneSuppliers.setDisable(true);
                anchorPaneOrders.setVisible(false);
                anchorPaneReports.setVisible(false);
                anchorPaneSuppliers.setVisible(false);

                resetProductValuesDisplay();
                break;
            case ORDERS:
//                HANDLE BUTTON STYLES WHEN CLICKED
                btnOrders.setStyle("-fx-background-color: rgba(44, 37, 14, 0.4);");
                btnCatalog.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
                btnSuppliers.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
                btnReports.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
//                ENABLE AND SHOW ANCHOR PANE ORDERS
                anchorPaneOrders.setDisable(false);
                anchorPaneOrders.setVisible(true);
//                DISABLE AND HIDE REMAINING ANCHOR PANES
                anchorPaneCatalog.setDisable(true);
                anchorPaneReports.setDisable(true);
                anchorPaneSuppliers.setDisable(true);
                anchorPaneCatalog.setVisible(false);
                anchorPaneReports.setVisible(false);
                anchorPaneSuppliers.setVisible(false);

                resetProductValuesDisplay();
                break;
            case SUPPLIERS:
//                HANDLE BUTTON STYLES WHEN CLICKED
                btnSuppliers.setStyle("-fx-background-color: rgba(44, 37, 14, 0.4);");
                btnOrders.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
                btnCatalog.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
                btnReports.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
//                ENABLE AND SHOW ANCHOR PANE ORDERS
                anchorPaneSuppliers.setDisable(false);
                anchorPaneSuppliers.setVisible(true);
//                DISABLE AND HIDE REMAINING ANCHOR PANES
                anchorPaneOrders.setDisable(true);
                anchorPaneReports.setDisable(true);
                anchorPaneCatalog.setDisable(true);
                anchorPaneOrders.setVisible(false);
                anchorPaneReports.setVisible(false);
                anchorPaneCatalog.setVisible(false);

                resetProductValuesDisplay();
                break;
            case REPORTS:
//                HANDLE BUTTON STYLES WHEN CLICKED
                btnReports.setStyle("-fx-background-color: rgba(44, 37, 14, 0.4);");
                btnOrders.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
                btnSuppliers.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
                btnCatalog.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
//                ENABLE AND SHOW ANCHOR PANE ORDERS
                anchorPaneReports.setDisable(false);
                anchorPaneReports.setVisible(true);
//                DISABLE AND HIDE REMAINING ANCHOR PANES
                anchorPaneOrders.setDisable(true);
                anchorPaneCatalog.setDisable(true);
                anchorPaneSuppliers.setDisable(true);
                anchorPaneOrders.setVisible(false);
                anchorPaneCatalog.setVisible(false);
                anchorPaneSuppliers.setVisible(false);

                resetProductValuesDisplay();
                break;
        }
    }

    private void loadDateAndTime() {
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        lblDate.setText(f.format(date));

        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime now = LocalTime.now();
            lblTime.setText(now.getHour() + ":" + now.getMinute() + ":" + now.getSecond());
        }),
            new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void loadCatalogProductsTable(ObservableList<Product> allProducts) {
        columnCatalogProductsID.setCellValueFactory(new PropertyValueFactory<>("productId"));
        columnCatalogProductsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnCatalogProductsQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        columnCatalogProductsUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));

        columnCatalogProductsSupplierID.setCellValueFactory(cellData -> {
            Supplier supplier = cellData.getValue().getSupplier();
            return new SimpleStringProperty(supplier != null ? supplier.getSupplierId() : "-");
        });

        columnCatalogProductsCategoryID.setCellValueFactory(cellData -> {
            Category category = cellData.getValue().getCategory();
            return new SimpleStringProperty(category != null ? category.getCategoryId() : "-");
        });

        columnCatalogProductsID.setStyle("-fx-alignment:center;");
        columnCatalogProductsName.setStyle("-fx-alignment:center;");
        columnCatalogProductsCategoryID.setStyle("-fx-alignment:center;");
        columnCatalogProductsQuantity.setStyle("-fx-alignment:center;");
        columnCatalogProductsUnitPrice.setStyle("-fx-alignment:center;");
        columnCatalogProductsSupplierID.setStyle("-fx-alignment:center;");

        setIconsToTables(ActionTableType.PRODUCTS);
        tblCatalogProducts.setItems(allProducts);
    }

    private void setIconsToTables(ActionTableType type){
        Callback<TableColumn<Product, Void>, TableCell<Product, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Product, Void> call(final TableColumn<Product, Void> param) {
                return new TableCell<>() {
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            Button editIcon = new Button("Edit");
                            Button deleteIcon = new Button("Delete");

                            deleteIcon.setStyle("-fx-cursor: hand ;");
                            editIcon.setStyle("-fx-cursor: hand ;");

                            // Delete button action
                            deleteIcon.setOnMouseClicked(event -> {
                                switch(type){
                                    case PRODUCTS:
                                        handleDeleteActions(type, tblCatalogProducts.getItems().get(getIndex()));
                                        break;
                                }
                            });

                            // Edit button action
                            editIcon.setOnMouseClicked(event -> {
                                switch(type){
                                    case PRODUCTS:
                                        selectedProductToEdit = tblCatalogProducts.getItems().get(getIndex());
                                        break;
                                }
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
        switch(type){
            case PRODUCTS:
                columnCatalogProductsAction.setCellFactory(cellFactory);
                break;
            case SUPPLIERS:
//                columnCatalogProductsAction.setCellFactory(cellFactory);
                break;
        }
    }

    private void handleEditActions(ActionTableType type, MouseEvent event){
        setEmployeeDashboardStage(event);
        switch(type){
            case PRODUCTS:
                try {
                    Stage stage=new Stage();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../../view/UpdateProducts.fxml"));
                    Parent root = loader.load();
                    UpdateProductFormController controller = loader.getController();
                    controller.setMainController(this);
                    stage.setScene(new Scene(root));
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.show();
                    stage.setResizable(false);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                enableScreen();
                break;
            case SUPPLIERS:
                System.out.println("BBBB");
                break;
        }
    }

    private void enableScreen() {
        screen.setDisable(false);
        screen.setVisible(true);
    }

    private void disableScreen() {
        screen.setDisable(true);
        screen.setVisible(false);
    }

    private void handleDeleteActions(ActionTableType type, Product product){
        enableScreen();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Are you sure you want to delete the selected Item?");
        alert.setContentText("Please confirm your action.");
        Optional<ButtonType> result = alert.showAndWait();

        switch(type){
            case PRODUCTS:
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    productService.deleteProduct(product.getProductId());
                    loadCatalogProductsTable(productService.getAllProducts());
                    Alert completionAlert = new Alert(Alert.AlertType.INFORMATION);
                    completionAlert.setTitle("Deletion Successful");
                    completionAlert.setHeaderText(null);
                    completionAlert.setContentText("The product has been successfully deleted.");
                    completionAlert.showAndWait();
                    disableScreen();
                } else {
                    disableScreen();
                }
                break;
            case SUPPLIERS:
                System.out.println("BBBB");
                break;
        }
    }

    private void addProductValuesToDisplay(Product newVal) {
        lblCatalogProductID.setText(newVal.getProductId());
        lblCatalogProductName.setText(newVal.getName());
        lblCatalogUnitPrice.setText("Rs. "+newVal.getUnitPrice().toString());

        ObservableList<String> sizeList = FXCollections.observableArrayList();
        sizeList.add("XS");
        sizeList.add("S");
        sizeList.add("M");
        sizeList.add("L");
        sizeList.add("XL");
        sizeList.add("XXL");
        cmbCatalogSize.setItems(sizeList);
        cmbCatalogSize.setValue("M");
        cmbCatalogSize.setVisible(true);

        spinnerCatalogQuantity.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,newVal.getQuantity(),1));
        spinnerCatalogQuantity.setVisible(true);

        if (newVal.getQuantity()!=0){
            btnAddToCart.setDisable(false);
        }else{
            btnAddToCart.setDisable(true);
        }
    }

    private void resetProductValuesDisplay() {
        lblCatalogProductID.setText("");
        lblCatalogProductName.setText("");
        lblCatalogUnitPrice.setText("");
        cmbCatalogSize.setVisible(false);
        spinnerCatalogQuantity.setVisible(false);
        btnAddToCart.setDisable(true);
        tblCatalogProducts.getSelectionModel().clearSelection();
    }



}
