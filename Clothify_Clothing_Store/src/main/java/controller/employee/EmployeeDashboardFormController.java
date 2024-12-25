package controller.employee;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import controller.CheckOutFormController;
import controller.HomeFormController;
import controller.product.AddProductFormController;
import controller.product.AddProductsBySupplierFormController;
import controller.product.EditProductFormController;
import controller.supplier.EditSupplierFormController;
import db.DBConnection;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;
import model.*;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import service.ServiceFactory;
import service.custom.*;
import util.ActionTableType;
import util.DashboardViewType;
import util.Type;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;

import static util.ActionTableType.PRODUCTS;
import static util.ActionTableType.SUPPLIERS;

@Getter
@Setter
public class EmployeeDashboardFormController implements Initializable {
    private Employee loggedEmployee;
    private Stage employeeDashboardStage;

    private static EmployeeDashboardFormController instance;

    public static EmployeeDashboardFormController getInstance() {
        if (instance != null){
            return instance;
        }else{
            throw new IllegalStateException("Instance not created!");
        }
    }

    private void setEmployeeDashboardStage(MouseEvent event) {
        employeeDashboardStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    private void setEmployeeDashboardStage(ActionEvent event) {
        employeeDashboardStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    public EmployeeDashboardFormController() {
        if (instance == null) instance = this;
    }

    //VARIABLES ===============

    private Product selectedProductToEdit = new Product();
    private Supplier selectedSupplierToEdit = new Supplier();
    private ObservableList<Product> suppliedProducts = FXCollections.observableArrayList();

    private ObservableList<Product> allProducts = FXCollections.observableArrayList();
    private ObservableList<Supplier> allSuppliers = FXCollections.observableArrayList();
    private ObservableList<Order> allOrders = FXCollections.observableArrayList();
    private final List<TempOrderItem> tempOrderItemList = new ArrayList<>();

    //SERVICE-FACTORIES
    private final ProductService productService = ServiceFactory.getInstance().getServiceType(Type.PRODUCT);
    private final SupplierService supplierService = ServiceFactory.getInstance().getServiceType(Type.SUPPLIER);
    private final OrderItemsService orderItemsService = ServiceFactory.getInstance().getServiceType(Type.ORDERITEMS);
    private final OrderService orderService = ServiceFactory.getInstance().getServiceType(Type.ORDER);
    private final CustomerService customerService = ServiceFactory.getInstance().getServiceType(Type.CUSTOMER);
    //=================

    @FXML
    private AnchorPane anchorPaneCatalog;

    @FXML
    private AnchorPane anchorPaneOrders;

    @FXML
    private AnchorPane anchorPaneReports;

    @FXML
    private AnchorPane anchorPaneSuppliers;

    @FXML
    public Button btnAddToCart;

    @FXML
    private Button btnCatalog;

    @FXML
    public Button btnCheckOut;

    @FXML
    private Button btnOrders;

    @FXML
    private Button btnReports;

    @FXML
    private Button btnSuppliers;

    @FXML
    private ComboBox<String> cmbCatalogSize;

    @FXML
    public JFXButton btnCloseSearch;

    @FXML
    private JFXTextField txtAddSupplierCompany;

    @FXML
    private JFXTextField txtAddSupplierEmail;

    @FXML
    private JFXTextField txtAddSupplierName;

    @FXML
    private Label lblCatalogProductID;

    @FXML
    private Label lblCatalogProductName;

    @FXML
    private Label lblCatalogUnitPrice;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblEmployeeAddress;

    @FXML
    private Label lblEmployeeContact;

    @FXML
    private Label lblEmployeeEmail;

    @FXML
    private Label lblEmployeeID;

    @FXML
    private Label lblEmployeeNIC;

    @FXML
    private Label lblEmployeeName;

    @FXML
    private Label lblTime;

    @FXML
    private Label lblTotalCompanies;

    @FXML
    private Label lblTotalCustomerCount;

    @FXML
    private Label lblTotalOrdersCatalog;

    @FXML
    private Label lblTotalOrdersOrders;

    @FXML
    private Label lblTotalProducts;

    @FXML
    private Label lblTotalReturnOrderCount;

    @FXML
    private Label lblTotalStock;

    @FXML
    private Label lblTotalSuppliers;

    @FXML
    private Label lblWelcomeEmployee;

    @FXML
    public Spinner<Integer> spinnerCatalogQuantity;

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
    private TableColumn<Supplier, Void> columnSuppliersAction;

    @FXML
    private TableColumn<Product, String> columnSuppliersAddProductsProductName;

    @FXML
    private TableColumn<Product, String> columnSuppliersAddProductsCategoryName;

    @FXML
    private TableColumn<Supplier, String> columnSuppliersEmail;

    @FXML
    private TableColumn<Supplier, String> columnSuppliersCompany;

    @FXML
    private TableColumn<Supplier, String> columnSuppliersID;

    @FXML
    private TableColumn<Supplier, String> columnSuppliersName;

    @FXML
    private TableColumn<Product, String> columnSuppliersSuppliedProductsID;

    @FXML
    private TableColumn<Product, String> columnSuppliersSuppliedProductsName;

    @FXML
    private TableColumn<Product, String> columnSuppliersSuppliedProductsSupplierID;

    @FXML
    private TableColumn<OrderItem, String> columnOrderItemsProductID;

    @FXML
    private TableColumn<OrderItem, String> columnOrderItemsProductName;

    @FXML
    private TableColumn<OrderItem, String> columnOrderItemsProductSize;

    @FXML
    private TableColumn<Order, String> columnOrdersCustomerID;

    @FXML
    private TableColumn<Order, LocalDate> columnOrdersDate;

    @FXML
    private TableColumn<Order, String> columnOrdersID;

    @FXML
    private TableColumn<Order, Integer> columnOrdersItemCount;

    @FXML
    private TableColumn<Order, String> columnOrdersPaymentType;

    @FXML
    private TableColumn<Order, LocalTime> columnOrdersTime;

    @FXML
    private TableColumn<Order, Double> columnOrdersTotal;

    @FXML
    public TableView<Product> tblCatalogProducts;

    @FXML
    private TableView<Supplier> tblSuppliers;

    @FXML
    private TableView<Product> tblSuppliersAddProducts;

    @FXML
    private TableView<Product> tblSuppliersSuppliedProducts;

    @FXML
    private TableView<OrderItem> tblOrderItems;

    @FXML
    private TableView<Order> tblOrders;

    @FXML
    public TextField searchInput;

    @FXML
    public VBox screen;

    @FXML
    void btnCloseOnAction(MouseEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
        HomeFormController.getInstance().getHomeStage().show();
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
    void btnAddSupplierOnAction(ActionEvent event) {
        String name = txtAddSupplierName.getText().trim();
        String email = txtAddSupplierEmail.getText().trim();
        String company = txtAddSupplierCompany.getText().trim();
        if (!name.isEmpty() && supplierService.isValidEmail(email) && !company.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Supplier Added");
            alert.setHeaderText(null);
            alert.setContentText("Supplier added successfully!");
            alert.show();
            if (!suppliedProducts.isEmpty()) {
                suppliedProducts.forEach(product -> {
                    product.setSupplier(new Supplier(null, name, company, email));
                    productService.addProduct(product);
                });
            } else {
                supplierService.addSupplier(new Supplier(null, name, company, email));
            }
            loadSuppliersTable(supplierService.getAllSuppliers());
            loadCatalogProductsTable(productService.getAllProducts());
            setSuppliersPaneLabels();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(null);
            alert.setContentText("Please Enter All the Fields with Correct Data");
            alert.show();
            txtAddSupplierName.setText("");
            txtAddSupplierCompany.setText("");
            txtAddSupplierEmail.setText("");
        }
    }

    @FXML
    void btnAddToCartOnAction(ActionEvent event) {
        try {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Product Added");
            alert.setHeaderText(null);
            alert.setContentText("Product added successfully!");
            alert.show();
            Product selectedProduct = null;

            for (Product product : allProducts) {
                if (product.getProductId().equals(lblCatalogProductID.getText())) {
                    selectedProduct = product;
                    product.setQuantity(product.getQuantity() - spinnerCatalogQuantity.getValue());
                    break;
                }
            }
            for (TempOrderItem tempOrderItem : tempOrderItemList) {
                boolean isProductIdMatches = tempOrderItem.getOrderItem().getProductId().equals(lblCatalogProductID.getText());
                boolean isProductSizeMatches = tempOrderItem.getOrderItem().getSize().equals(cmbCatalogSize.getValue().trim());
                if (isProductSizeMatches && isProductIdMatches) {
                    tempOrderItem.setQuantity(tempOrderItem.getQuantity() + spinnerCatalogQuantity.getValue());
                    return;
                }
            }
            if (selectedProduct != null) {
                tempOrderItemList.add(new TempOrderItem(
                        new OrderItem(
                                new Order(null, null, null, spinnerCatalogQuantity.getValue() * selectedProduct.getUnitPrice(), null, new Customer(), null),
                                selectedProduct.getName(),
                                selectedProduct.getProductId(),
                                selectedProduct.getCategory().getCategoryId(),
                                selectedProduct.getCategory().getName(),
                                selectedProduct.getSupplier().getSupplierId(),
                                selectedProduct.getSupplier().getName(),
                                selectedProduct.getUnitPrice(),
                                cmbCatalogSize.getValue().trim()),
                        spinnerCatalogQuantity.getValue())
                );
            }
        } finally {
            btnCheckOut.setDisable(false);
            loadCatalogProductsTable(allProducts);
            resetProductValuesDisplay();
        }
    }

    @FXML
    public void btnCheckoutOnAction(ActionEvent event) {
        try {
            setEmployeeDashboardStage(event);
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../view/CheckOut.fxml"));
            Parent root = loader.load();
            CheckOutFormController controller = loader.getController();
            controller.setCheckOutButton(btnCheckOut);
            controller.setEmployeeDashboardFormController(this);
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
            stage.setResizable(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        enableScreen();
    }

    @FXML
    public void btnCatalogAddProductOnAction(ActionEvent event) {
        try {
            setEmployeeDashboardStage(event);
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../view/AddProducts.fxml"));
            Parent root = loader.load();
            AddProductFormController controller = loader.getController();
            controller.setEmployeeDashboardFormController(this);
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
            stage.setResizable(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        enableScreen();
    }

    @FXML
    public void btnAddProductForTheSupplierOnAction(ActionEvent event) {
        try {
            setEmployeeDashboardStage(event);
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../view/AddProductsBySupplier.fxml"));
            Parent root = loader.load();
            AddProductsBySupplierFormController controller = loader.getController();
            controller.setEmployeeDashboardFormController(this);
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
            stage.setResizable(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        enableScreen();
    }

    @FXML
    public void btnClearAddSupplierOnAction(ActionEvent event) {
        suppliedProducts = FXCollections.observableArrayList();
        tblSuppliersAddProducts.getItems().clear();
        txtAddSupplierName.setText("");
        txtAddSupplierCompany.setText("");
        txtAddSupplierEmail.setText("");
    }

    @FXML
    void btnLoadCustomerReportOnAction(ActionEvent event) {
        openReports("src/main/resources/reports/customer.jrxml");
    }

    @FXML
    void btnLoadEmployeeReportOnAction(ActionEvent event) {
        openReports("src/main/resources/reports/employee.jrxml");
    }

    @FXML
    void btnLoadInventoryReportOnAction(ActionEvent event) {
        openReports("src/main/resources/reports/products.jrxml");
    }

    @FXML
    public void searchProduct(KeyEvent keyEvent) {
        String search = searchInput.getText().trim();
        boolean found = false;

        if (!search.isEmpty()) {
            enableCloseSearch();

            for (Product product : tblCatalogProducts.getItems()) {
                if (product.getProductId().equalsIgnoreCase(search)) {
                    int index = tblCatalogProducts.getItems().indexOf(product); // Get the index of the matching item
                    Platform.runLater(() -> {
                        tblCatalogProducts.getSelectionModel().clearAndSelect(index); // Clear and select the row
                        tblCatalogProducts.scrollTo(index); // Scroll to the selected row
                        tblCatalogProducts.requestFocus(); // Ensure the table has focus
                    });
                    found = true;
                    break;
                }
            }
            if (!found) {
                tblCatalogProducts.getSelectionModel().clearSelection();
                tblCatalogProducts.scrollTo(0);
                resetProductValuesDisplay();
            }
        }else{
            resetProductValuesDisplay();
            disableCloseSearch();
        }
    }

    @FXML
    public void btnCloseSearchOnAction(ActionEvent event) {
        searchInput.setText("");
        btnCloseSearch.setDisable(true);
        btnCloseSearch.setVisible(false);
        tblCatalogProducts.getSelectionModel().clearSelection();
        tblCatalogProducts.scrollTo(0);
        resetProductValuesDisplay();
    }

    @FXML
    public void openInstagramOnAction(MouseEvent event) {
    }

    @FXML
    public void openLinkedinOnAction(MouseEvent event) {
        try {
            URI uri = new URI("https://www.linkedin.com/in/tharusha-gunarathne-5b0b88330/");
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(uri);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void openGithubOnAction(MouseEvent event) {
        try {
            URI uri = new URI("https://github.com/Tharush2002");
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(uri);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDateAndTime();
        loadTables();
        setAllLabels();
        disableCloseSearch();
        setTablesInitializationStates();
    }

    //====================== MY IMPLEMENTATIONS =======================

    //LOAD TABLES

    private void loadTables() {
        loadCatalogProductsTable(productService.getAllProducts());
        loadSuppliersTable(supplierService.getAllSuppliers());
        loadOrdersTable(orderService.getAllOrders());
    }

    public void loadOrdersTable(ObservableList<Order> allOrders) {
        if (allOrders != null) {
            this.allOrders = allOrders;

            columnOrdersID.setCellValueFactory(new PropertyValueFactory<>("orderId"));
            columnOrdersTime.setCellValueFactory(new PropertyValueFactory<>("time"));
            columnOrdersDate.setCellValueFactory(new PropertyValueFactory<>("date"));
            columnOrdersPaymentType.setCellValueFactory(new PropertyValueFactory<>("paymentType"));
            columnOrdersItemCount.setCellValueFactory(new PropertyValueFactory<>("orderItemCount"));
            columnOrdersTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

            columnOrdersCustomerID.setCellValueFactory(cellData -> {
                Customer customer = cellData.getValue().getCustomer();
                return new SimpleStringProperty(customer != null ? customer.getCustomerId() : "-");
            });

            columnOrdersID.setStyle("-fx-alignment:center;");
            columnOrdersTime.setStyle("-fx-alignment:center;");
            columnOrdersDate.setStyle("-fx-alignment:center;");
            columnOrdersPaymentType.setStyle("-fx-alignment:center;");
            columnOrdersItemCount.setStyle("-fx-alignment:center;");
            columnOrdersTotal.setStyle("-fx-alignment:center;");
            columnOrdersCustomerID.setStyle("-fx-alignment:center;");

            tblOrders.setItems(allOrders);
        }
    }

    private void loadOrderItemsTable(ObservableList<OrderItem> orderItems) {
        if (orderItems != null) {
            columnOrderItemsProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
            columnOrderItemsProductSize.setCellValueFactory(new PropertyValueFactory<>("size"));
            columnOrderItemsProductID.setCellValueFactory(new PropertyValueFactory<>("productId"));

            columnOrderItemsProductName.setStyle("-fx-alignment:center;");
            columnOrderItemsProductSize.setStyle("-fx-alignment:center;");
            columnOrderItemsProductID.setStyle("-fx-alignment:center;");

            tblOrderItems.setItems(orderItems);
        }
    }

    private void loadSuppliedProductsTable(ObservableList<Product> suppliedProducts) {
        if (suppliedProducts != null) {
            columnSuppliersSuppliedProductsName.setCellValueFactory(new PropertyValueFactory<>("name"));
            columnSuppliersSuppliedProductsID.setCellValueFactory(new PropertyValueFactory<>("productId"));

            columnSuppliersSuppliedProductsSupplierID.setCellValueFactory(cellData -> {
                Supplier supplier = cellData.getValue().getSupplier();
                return new SimpleStringProperty(supplier != null ? supplier.getSupplierId() : "-");
            });

            columnSuppliersSuppliedProductsName.setStyle("-fx-alignment:center;");
            columnSuppliersSuppliedProductsID.setStyle("-fx-alignment:center;");
            columnSuppliersSuppliedProductsSupplierID.setStyle("-fx-alignment:center;");

            tblSuppliersSuppliedProducts.setItems(suppliedProducts);
        }
    }

    public void loadSuppliersTable(ObservableList<Supplier> allSuppliers) {
        if (allSuppliers != null) {
            this.allSuppliers = allSuppliers;

            columnSuppliersID.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
            columnSuppliersName.setCellValueFactory(new PropertyValueFactory<>("name"));
            columnSuppliersCompany.setCellValueFactory(new PropertyValueFactory<>("company"));
            columnSuppliersEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

            columnSuppliersID.setStyle("-fx-alignment:center;");
            columnSuppliersName.setStyle("-fx-alignment:center;");
            columnSuppliersCompany.setStyle("-fx-alignment:center;");
            columnSuppliersEmail.setStyle("-fx-alignment:center;");

            setActionsToTables(SUPPLIERS);
            tblSuppliers.setItems(allSuppliers);
        }
    }

    public void loadSuppliersAddProductsTable() {
        if (!suppliedProducts.isEmpty()) {
            columnSuppliersAddProductsProductName.setCellValueFactory(new PropertyValueFactory<>("name"));

            columnSuppliersAddProductsCategoryName.setCellValueFactory(cellData -> {
                Category category = cellData.getValue().getCategory();
                return new SimpleStringProperty(category != null ? category.getName() : "-");
            });
            tblSuppliersAddProducts.setItems(suppliedProducts);
            columnSuppliersAddProductsProductName.setStyle("-fx-alignment:center;");
            columnSuppliersAddProductsCategoryName.setStyle("-fx-alignment:center;");
        }
    }

    public void loadCatalogProductsTable(ObservableList<Product> allProducts) {
        if (allProducts != null) {
            this.allProducts = allProducts;

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

            setActionsToTables(PRODUCTS);
            tblCatalogProducts.setItems(allProducts);
        }
    }

    //=============


    //OTHER IMPLEMENTATIONS

    private void handleDashboardSidePanelBtnClicks(DashboardViewType type) {
        switch (type) {
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
                resetSearch();
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
                resetSearch();
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
                resetSearch();
                break;
        }
        resetTables();
    }

    private void loadDateAndTime() {
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        lblDate.setText(f.format(date));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime now = LocalTime.now();
            String formattedTime = now.format(formatter).replace("am", " A.M").replace("pm", " P.M");
            lblTime.setText(formattedTime);
        }),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void setActionsToTables(ActionTableType type) {
        Callback<TableColumn<Product, Void>, TableCell<Product, Void>> productCellFactory = new Callback<>() {
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

                            deleteIcon.setOnMouseClicked(event -> {
                                handleDeleteActions(type, tblCatalogProducts.getItems().get(getIndex()));
                            });

                            editIcon.setOnMouseClicked(event -> {
                                selectedProductToEdit = tblCatalogProducts.getItems().get(getIndex());
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

        Callback<TableColumn<Supplier, Void>, TableCell<Supplier, Void>> supplierCellFactory = new Callback<>() {
            @Override
            public TableCell<Supplier, Void> call(final TableColumn<Supplier, Void> param) {
                return new TableCell<>() {
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            Button editIcon = new Button("Edit");
                            Button deleteIcon = new Button("Delete");

                            deleteIcon.setStyle("-fx-cursor: hand;");
                            editIcon.setStyle("-fx-cursor: hand;");

                            deleteIcon.setOnMouseClicked(event -> {
                                handleDeleteActions(type, tblSuppliers.getItems().get(getIndex()));
                            });

                            editIcon.setOnMouseClicked(event -> {
                                selectedSupplierToEdit = tblSuppliers.getItems().get(getIndex());
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

        switch (type) {
            case PRODUCTS:
                columnCatalogProductsAction.setCellFactory(productCellFactory);
                break;
            case SUPPLIERS:
                columnSuppliersAction.setCellFactory(supplierCellFactory);
                break;
        }
    }

    private void handleEditActions(ActionTableType type, MouseEvent event) {
        setEmployeeDashboardStage(event);
        try {
            Stage stage = new Stage();
            new FXMLLoader();
            FXMLLoader loader = switch (type) {
                case PRODUCTS -> new FXMLLoader(getClass().getResource("../../view/EditProducts.fxml"));
                case SUPPLIERS -> new FXMLLoader(getClass().getResource("../../view/EditSupplier.fxml"));
            };
            Parent root = loader.load();
            if (type == PRODUCTS) {
                EditProductFormController editProductFormController = loader.getController();
                editProductFormController.setEmployeeDashboardFormController(this);
            } else {
                EditSupplierFormController editSupplierFormController = loader.getController();
                editSupplierFormController.setEmployeeDashboardFormController(this);
            }

            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
            stage.setResizable(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        enableScreen();
    }

    private void handleDeleteActions(ActionTableType type, Object object) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Are you sure you want to delete the selected Item?");
        alert.setContentText("Please confirm your action.");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (type == PRODUCTS) {
                Product product = (Product) object;
                productService.deleteProduct(product.getProductId());
                loadCatalogProductsTable(productService.getAllProducts());
            } else if (type == SUPPLIERS) {
                Supplier supplier = (Supplier) object;
                supplierService.deleteSupplier(supplier.getSupplierId());
                loadSuppliersTable(supplierService.getAllSuppliers());
                loadCatalogProductsTable(productService.getAllProducts());
            }

            Alert completionAlert = new Alert(Alert.AlertType.INFORMATION);
            completionAlert.setTitle("Deletion Successful");
            completionAlert.setHeaderText(null);
            completionAlert.setContentText("Selected property has been successfully deleted.");
            completionAlert.showAndWait();
        }
    }

    private void enableScreen() {
        screen.setDisable(false);
        screen.setVisible(true);
    }

    private void addProductValuesToDisplay(Product newVal) {
        lblCatalogProductID.setText(newVal.getProductId());
        lblCatalogProductName.setText(newVal.getName());
        lblCatalogUnitPrice.setText("Rs. " + newVal.getUnitPrice().toString());

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

        spinnerCatalogQuantity.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, newVal.getQuantity(), 1));
        spinnerCatalogQuantity.setVisible(true);

        btnAddToCart.setDisable(newVal.getQuantity() == 0);
    }

    private void resetTables() {
        resetProductValuesDisplay();
        resetSuppliersTables();
        resetOrdersTables();
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

    private void resetSuppliersTables() {
        tblSuppliers.getSelectionModel().clearSelection();
        tblSuppliersSuppliedProducts.getItems().clear();
        tblSuppliersAddProducts.getItems().clear();
        suppliedProducts.clear();
        txtAddSupplierEmail.setText("");
        txtAddSupplierCompany.setText("");
        txtAddSupplierName.setText("");
    }

    private void resetOrdersTables() {
        tblOrderItems.getItems().clear();
        tblOrders.getSelectionModel().clearSelection();
    }

    private void setAllLabels() {
        setCatalogPaneLabels();
        setSuppliersPaneLabels();
        setOrdersPaneLabels();
    }

    public void setCatalogPaneLabels() {
        lblTotalOrdersCatalog.setText(allOrders != null ? String.valueOf(allOrders.size()) : "");
        if (allProducts != null) {
            lblTotalProducts.setText(String.valueOf(allProducts.size()));

            Integer totalStock = 0;
            for (Product allProduct : allProducts) {
                totalStock += allProduct.getQuantity();
            }
            lblTotalStock.setText(String.valueOf(totalStock));
        } else {
            lblTotalProducts.setText("");
            lblTotalStock.setText("");
        }
    }

    public void setSuppliersPaneLabels() {
        if (allSuppliers != null) {
            lblTotalSuppliers.setText(String.valueOf(allSuppliers.size()));
            lblTotalCompanies.setText(String.valueOf(allSuppliers.size()));
        } else {
            lblTotalSuppliers.setText("");
            lblTotalCompanies.setText("");
        }
    }

    public void setOrdersPaneLabels() {
        ObservableList<Customer> customers = customerService.getAllCustomers();
        lblTotalCustomerCount.setText(customers != null ? String.valueOf(customers.size()) : "");
        lblTotalOrdersOrders.setText(allOrders != null ? String.valueOf(allOrders.size()) : "");
    }

    private void enableCloseSearch(){
        if(btnCloseSearch.isDisable() || !btnCloseSearch.isVisible()){
            btnCloseSearch.setDisable(false);
            btnCloseSearch.setVisible(true);
        }
    }

    private void disableCloseSearch(){
        if(!btnCloseSearch.isDisable() || btnCloseSearch.isVisible()){
            btnCloseSearch.setDisable(true);
            btnCloseSearch.setVisible(false);
        }
    }

    private void resetSearch(){
        searchInput.setText("");
        disableCloseSearch();
    }

    private void setTablesInitializationStates(){
        handleDashboardSidePanelBtnClicks(DashboardViewType.CATALOG);
        tblCatalogProducts.getSelectionModel().selectedItemProperty().addListener((observableValue, oldVal, newVal) -> {
            if (newVal != null) {
                resetSearch();
                addProductValuesToDisplay(newVal);
            }
        });
        tblSuppliers.getSelectionModel().selectedItemProperty().addListener((observableValue, oldVal, newVal) -> {
            if (newVal != null) {
                loadSuppliedProductsTable(productService.findProductsBySupplierID(newVal.getSupplierId()));
            }
        });
        tblOrders.getSelectionModel().selectedItemProperty().addListener((observableValue, oldVal, newVal) -> {
            if (newVal != null) {
                loadOrderItemsTable(orderItemsService.findOrderItemsByOrderID(newVal.getOrderId()));
            }
        });
    }

    public void loadEmployeeDetails(Employee employee) {
        lblWelcomeEmployee.setText(String.format("Welcome Back, %s",getEmployeeName(employee)));
        lblEmployeeName.setText(getEmployeeName(employee));
        lblEmployeeID.setText(employee.getEmployeeId() != null ? employee.getEmployeeId():"");
        lblEmployeeNIC.setText(employee.getNic() != null ? employee.getNic():"");
        lblEmployeeEmail.setText(employee.getEmail() != null ? employee.getEmail():"");
        lblEmployeeContact.setText(employee.getPhoneNumber() != null ? employee.getPhoneNumber():"");
        lblEmployeeAddress.setText(employee.getAddress() != null ? employee.getAddress():"");
    }

    private String getEmployeeName(Employee employee){
        String firstName = employee.getFirstName() != null ? employee.getFirstName() : "";
        String lastName = employee.getLastName() != null ? employee.getLastName() : "";
        return firstName + " " + lastName;
    }

    private void openReports(String fileName){
        try {
            JasperDesign jasperDesign = JRXmlLoader.load(fileName);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DBConnection.getInstance().getConnection());
            JasperViewer viewer = new JasperViewer(jasperPrint, false);
            viewer.setZoomRatio(0.5f);
            viewer.setVisible(true);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while generating the report.", e);
        }
    }
}
