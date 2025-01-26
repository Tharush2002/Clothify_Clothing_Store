package controller.employee;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import controller.CheckOutFormController;
import controller.HomeFormController;
import controller.category.EditCategoryFormController;
import controller.product.AddProductFormController;
import controller.product.AddProductsByCategoryFormController;
import controller.product.AddProductsBySupplierFormController;
import controller.product.EditProductFormController;
import controller.returnOrders.ReturnOrderFormController;
import controller.supplier.EditSupplierFormController;
import db.DBConnection;
import exceptions.RepositoryException;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
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
import util.AlertType;
import util.EmployeeDashboardViewType;
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
@Slf4j
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
    private Category selectedCategoryToEdit = new Category();

    private ObservableList<Product> suppliedProducts = FXCollections.observableArrayList();
    private ObservableList<Product> categorizedAddProducts = FXCollections.observableArrayList();

    private ObservableList<Product> allProducts = FXCollections.observableArrayList();
    private ObservableList<Supplier> allSuppliers = FXCollections.observableArrayList();
    private ObservableList<Order> allOrders = FXCollections.observableArrayList();
    private ObservableList<ReturnOrder> allReturnOrderItems = FXCollections.observableArrayList();
    private ObservableList<Category> allCategories = FXCollections.observableArrayList();
    private final List<OrderItemWithQuantity> orderItemWithQuantityList = new ArrayList<>();

    //SERVICE-FACTORIES
    private final ProductService productService = ServiceFactory.getInstance().getServiceType(Type.PRODUCT);
    private final SupplierService supplierService = ServiceFactory.getInstance().getServiceType(Type.SUPPLIER);
    private final OrderItemsService orderItemsService = ServiceFactory.getInstance().getServiceType(Type.ORDERITEMS);
    private final OrderService orderService = ServiceFactory.getInstance().getServiceType(Type.ORDER);
    private final CustomerService customerService = ServiceFactory.getInstance().getServiceType(Type.CUSTOMER);
    private final EmployeeService employeeService = ServiceFactory.getInstance().getServiceType(Type.EMPLOYEE);
    private final ReturnOrderService returnOrderService = ServiceFactory.getInstance().getServiceType(Type.RETURNORDER);
    private final CategoryService categoryService = ServiceFactory.getInstance().getServiceType(Type.CATEGORY);
    //=================

    @FXML
    private AnchorPane anchorPaneCatalog;

    @FXML
    private AnchorPane anchorPaneCategories;

    @FXML
    private AnchorPane anchorPaneOrders;

    @FXML
    private AnchorPane anchorPaneReports;

    @FXML
    private AnchorPane anchorPaneSuppliers;

    @FXML
    private Button btnAddToCart;

    @FXML
    private Button btnCatalog;

    @FXML
    private Button btnCategories;

    @FXML
    private Button btnCheckOut;

    @FXML
    private JFXButton btnCloseSearch;

    @FXML
    private Button btnOrders;

    @FXML
    private Button btnReports;

    @FXML
    private Button btnResetPassword;

    @FXML
    private Button btnSuppliers;

    @FXML
    private JFXComboBox<String> cmbCatalogSize;

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
    private TableColumn<Category, Void> columnCategoriesAction;

    @FXML
    private TableColumn<Product, String> columnCategoriesAddProductsProduct;

    @FXML
    private TableColumn<Product, String> columnCategoriesAddProductsSupplier;

    @FXML
    private TableColumn<Category, String> columnCategoriesId;

    @FXML
    private TableColumn<Category, String> columnCategoriesName;

    @FXML
    private TableColumn<Product, String> columnCategorizedProductsProductName;

    @FXML
    private TableColumn<Product, String> columnCategorizedProductsCategoryId;

    @FXML
    private TableColumn<ReturnOrder, String> columnReturnsOrderID;

    @FXML
    private TableColumn<ReturnOrder, Double> columnReturnsPrice;

    @FXML
    private TableColumn<ReturnOrder, String> columnReturnsProductID;

    @FXML
    private TableColumn<ReturnOrder, LocalDate> columnReturnsReturnDate;

    @FXML
    private TableColumn<ReturnOrder, String> columnReturnsReturnID;

    @FXML
    private TableColumn<ReturnOrder, String> columnReturnsSize;

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
    private Label lblEmployeeUserName;

    @FXML
    private Label lblReturnedItemsAvailableForTheOrder;

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
    private Label lblTotalProductsCatalog;

    @FXML
    private Label lblTotalProductsCategories;

    @FXML
    private Label lblTotalReturnOrderCount;

    @FXML
    private Label lblTotalStock;

    @FXML
    private Label lblTotalSuppliers;

    @FXML
    private Label lblTotalCategories;

    @FXML
    private Label lblWelcomeEmployee;

    @FXML
    private HBox notInStock;

    @FXML
    private HBox inStock;

    @FXML
    private PasswordField pwdConfirmNewPassword;

    @FXML
    private Pane pwdConfirmNewPasswordPane;

    @FXML
    private PasswordField pwdSetNewPassword;

    @FXML
    private Pane pwdSetNewPasswordPane;

    @FXML
    private Pane resetPasswordPane;

    @FXML
    private VBox screen;

    @FXML
    private TextField searchInput;

    @FXML
    private Spinner<Integer> spinnerCatalogQuantity;

    @FXML
    public TableView<Product> tblCatalogProducts;

    @FXML
    private TableView<Category> tblCategories;

    @FXML
    private TableView<Product> tblCategoriesAddProducts;

    @FXML
    private TableView<Product> tblCategoriesCategorizedProducts;

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
    private TableView<ReturnOrder> tblReturnOrders;

    @FXML
    private TextField txtAddCategoryName;

    @FXML
    private TextField txtAddSupplierCompany;

    @FXML
    private TextField txtAddSupplierEmail;

    @FXML
    private TextField txtAddSupplierName;

    @FXML
    private TextField txtConfirmNewPassword;

    @FXML
    private Pane txtConfirmNewPasswordPane;

    @FXML
    private TextField txtSetNewPassword;

    @FXML
    private Pane txtSetNewPasswordPane;

    @FXML
    void btnCloseOnAction(MouseEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
        HomeFormController.getInstance().getHomeStage().show();
    }

    @FXML
    void btnCatalogOnAction(ActionEvent event) {
        handleDashboardSidePanelBtnClicks(EmployeeDashboardViewType.CATALOG);
    }

    @FXML
    void btnOrdersOnAction(ActionEvent event) {
        handleDashboardSidePanelBtnClicks(EmployeeDashboardViewType.ORDERS);
    }

    @FXML
    void btnReportsOnAction(ActionEvent event) {
        handleDashboardSidePanelBtnClicks(EmployeeDashboardViewType.REPORTS);
    }

    @FXML
    void btnSuppliersOnAction(ActionEvent event) {
        handleDashboardSidePanelBtnClicks(EmployeeDashboardViewType.SUPPLIERS);
    }

    @FXML
    void btnCategoriesOnAction(ActionEvent event) {
        handleDashboardSidePanelBtnClicks(EmployeeDashboardViewType.CATEGORY);
    }

    @FXML
    void btnAddCategoryOnAction(ActionEvent event) {
        String name = txtAddCategoryName.getText().trim();
        try{
            if (!name.isEmpty() && !categoryService.isCategoryNameAvailable(name)) {
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Confirm Category Addition");
                confirmationAlert.setHeaderText("Add New Category");
                confirmationAlert.setContentText("Are you sure you want to add the new category: " + name + "?");

                Optional<ButtonType> resultConfirmationAlert = confirmationAlert.showAndWait();

                if (resultConfirmationAlert.isPresent() && resultConfirmationAlert.get() == ButtonType.OK) {
                    if (!categorizedAddProducts.isEmpty()) {
                        for(Product product : categorizedAddProducts){
                            product.setCategory(new Category(null, name));
                            productService.addProduct(product);
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Confirm No Items");
                        alert.setHeaderText("No Items for Category");
                        alert.setContentText("Are you sure you don't want to add items for this category: " + name + "?");

                        Optional<ButtonType> resultAlert = alert.showAndWait();

                        if (resultAlert.isPresent() && resultAlert.get() == ButtonType.OK) {
                            categoryService.addCategory(new Category(null, name));
                        } else {
                            return;
                        }
                    }
                    loadCategoriesTable(categoryService.getAllCategories());
                    loadCatalogProductsTable(productService.getAllProducts());
                    setCategoriesPaneLabels();
                    resetCategoriesTables();
                }
                showAlert(Alert.AlertType.INFORMATION, "Supplier Added", "Success", "Supplier added successfully!", AlertType.SHOW);

            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", "Please Enter All the Fields with Correct Data",AlertType.SHOW);

                txtAddSupplierName.setText("");
                txtAddSupplierCompany.setText("");
                txtAddSupplierEmail.setText("");
            }
        } catch (RepositoryException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", e.getMessage(),AlertType.SHOW);
        }
    }

    @FXML
    void btnAddProductForTheCategoryOnAction(ActionEvent event) {
        try {
            setEmployeeDashboardStage(event);
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../view/AddProductsByCategory.fxml"));
            Parent root = loader.load();
            AddProductsByCategoryFormController controller = loader.getController();
            controller.setEmployeeDashboardFormController(this);
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
            stage.setResizable(false);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", e.getMessage(),AlertType.SHOW);
        }
        enableScreen();
    }

    @FXML
    void btnAddSupplierOnAction(ActionEvent event) {
        String name = txtAddSupplierName.getText().trim();
        String email = txtAddSupplierEmail.getText().trim();
        String company = txtAddSupplierCompany.getText().trim();
        try{
            if (!name.isEmpty() && supplierService.isValidEmail(email) && !company.isEmpty() && !supplierService.isSupplierNameAlreadyAvailable(name)) {
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Confirm Supplier Addition");
                confirmationAlert.setHeaderText("Add New Supplier");
                confirmationAlert.setContentText("Are you sure you want to add the new supplier: " + name + "?");

                Optional<ButtonType> resultConfirmationAlert = confirmationAlert.showAndWait();

                if (resultConfirmationAlert.isPresent() && resultConfirmationAlert.get() == ButtonType.OK) {
                    if (!suppliedProducts.isEmpty()) {
                        for(Product product : suppliedProducts){
                            product.setSupplier(new Supplier(null, name, company, email));
                            productService.addProduct(product);
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Confirm No Items");
                        alert.setHeaderText("No Items for Supplier");
                        alert.setContentText("Are you sure you don't want to add items for the supplier: " + name + "?");

                        Optional<ButtonType> resultAlert = alert.showAndWait();

                        if (resultAlert.isPresent() && resultAlert.get() == ButtonType.OK) {
                            supplierService.addSupplier(new Supplier(null, name, company, email));
                        } else {
                            return;
                        }
                    }
                    loadSuppliersTable(supplierService.getAll());
                    loadCatalogProductsTable(productService.getAllProducts());
                    setSuppliersPaneLabels();
                    resetSuppliersTables();
                }
                showAlert(Alert.AlertType.INFORMATION, "Supplier Added", "Success", "Supplier added successfully!", AlertType.SHOW);

            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", "Please Enter All the Fields with Correct Data",AlertType.SHOW);

                txtAddSupplierName.setText("");
                txtAddSupplierCompany.setText("");
                txtAddSupplierEmail.setText("");
            }
        } catch (RepositoryException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", e.getMessage(),AlertType.SHOW);
        }
    }

    @FXML
    void btnAddToCartOnAction(ActionEvent event) {
        try {
            showAlert(Alert.AlertType.INFORMATION,"Product Added","Success","Product added successfully!",AlertType.SHOW);
            Product selectedProduct = null;

            for (Product product : allProducts) {
                if (product.getProductId().equals(lblCatalogProductID.getText())) {
                    selectedProduct = product;
                    product.setQuantity(product.getQuantity() - spinnerCatalogQuantity.getValue());
                    break;
                }
            }

            for (OrderItemWithQuantity orderItemWithQuantity : orderItemWithQuantityList) {
                boolean isProductIdMatches = orderItemWithQuantity.getOrderItem().getProductId().equals(lblCatalogProductID.getText());
                boolean isProductSizeMatches = orderItemWithQuantity.getOrderItem().getSize().equals(cmbCatalogSize.getValue().trim());
                if (isProductSizeMatches && isProductIdMatches) {
                    orderItemWithQuantity.setQuantity(orderItemWithQuantity.getQuantity() + spinnerCatalogQuantity.getValue());
                    return;
                }
            }

            if (selectedProduct != null) {
                orderItemWithQuantityList.add(new OrderItemWithQuantity(
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
            setCatalogPaneLabels();
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
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", e.getMessage(),AlertType.SHOW);
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
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", e.getMessage(),AlertType.SHOW);
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
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", e.getMessage(),AlertType.SHOW);
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
    void btnClearAddCategoryOnAction(ActionEvent event) {
        categorizedAddProducts = FXCollections.observableArrayList();
        tblCategoriesAddProducts.getItems().clear();
        txtAddCategoryName.setText("");
    }

    @FXML
    void btnLoadCustomerReportOnAction(ActionEvent event) {
        openReports("src/main/resources/reports/Customer.jrxml");
    }

    @FXML
    void btnLoadEmployeeReportOnAction(ActionEvent event) {
        openReports("src/main/resources/reports/Employee.jrxml");
    }

    @FXML
    void btnLoadInventoryReportOnAction(ActionEvent event) {
        openReports("src/main/resources/reports/Inventory.jrxml");
    }

    @FXML
    public void searchProduct(KeyEvent keyEvent) {
        String search = searchInput.getText().trim();
        boolean found = false;

        if (!search.isEmpty()) {
            enableCloseSearch();

            for (Product product : tblCatalogProducts.getItems()) {
                if (product.getProductId().equalsIgnoreCase(search)) {
                    int index = tblCatalogProducts.getItems().indexOf(product);
                    Platform.runLater(() -> {
                        tblCatalogProducts.getSelectionModel().clearAndSelect(index);
                        tblCatalogProducts.scrollTo(index);
                        tblCatalogProducts.requestFocus();
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
        try {
            URI uri = new URI("https://www.instagram.com/_tharush_00/");
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(uri);
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", e.getMessage(),AlertType.SHOW);
        }
    }

    @FXML
    public void openLinkedinOnAction(MouseEvent event) {
        try {
            URI uri = new URI("https://www.linkedin.com/in/tharusha-gunarathne-5b0b88330/");
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(uri);
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", e.getMessage(),AlertType.SHOW);
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
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", e.getMessage(),AlertType.SHOW);
        }
    }

    @FXML
    void showPwdConfirmNewPasswordOnAction(MouseEvent event) {
        enablePwdConfirmNewPassword(true);
        enableTxtConfirmNewPassword(false);
        pwdConfirmNewPassword.requestFocus();
        pwdConfirmNewPassword.positionCaret(pwdConfirmNewPassword.getText().length());
    }

    @FXML
    void showPwdSetNewPasswordOnAction(MouseEvent event) {
        enablePwdSetNewPassword(true);
        enableTxtSetNewPassword(false);
        pwdSetNewPassword.requestFocus();
        pwdSetNewPassword.positionCaret(pwdSetNewPassword.getText().length());
    }

    @FXML
    void showTxtConfirmNewPasswordOnAction(MouseEvent event) {
        enablePwdConfirmNewPassword(false);
        enableTxtConfirmNewPassword(true);
        txtConfirmNewPassword.requestFocus();
        txtConfirmNewPassword.positionCaret(txtConfirmNewPassword.getText().length());
    }

    @FXML
    void showTxtSetNewPasswordOnAction(MouseEvent event) {
        enablePwdSetNewPassword(false);
        enableTxtSetNewPassword(true);
        txtSetNewPassword.requestFocus();
        txtSetNewPassword.positionCaret(txtSetNewPassword.getText().length());
    }

    @FXML
    void btnResetPasswordOnAction(ActionEvent event) {
        btnResetPassword.setDisable(true);
        enableResetPasswordPane(true);
        setPwdInitializationStates();
    }

    @FXML
    void btnConfirmResetPasswordOnAction(ActionEvent event) {
        try{
            if(pwdSetNewPassword.getText().equals(pwdConfirmNewPassword.getText())){
                if(!pwdSetNewPassword.getText().trim().isEmpty() && !pwdConfirmNewPassword.getText().trim().isEmpty()){
                    employeeService.updateEmployeePassword(loggedEmployee.getEmail(), pwdSetNewPassword.getText().trim());
                    showAlert(Alert.AlertType.INFORMATION,"Password Update","Success!","Your password has been successfully changed.",AlertType.SHOWANDWAIT);
                    btnResetPassword.setDisable(false);
                    enableResetPasswordPane(false);
                }else{
                    showAlert(Alert.AlertType.ERROR,"Error","Password Field is Empty","Please enter your password before proceeding.",AlertType.SHOWANDWAIT);
                }
            }else{
                showAlert(Alert.AlertType.ERROR,"Error","Passwords Do Not Match","The passwords entered do not match. Please try again.",AlertType.SHOWANDWAIT);
            }
        } catch (RepositoryException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", e.getMessage(),AlertType.SHOW);
        }
    }

    @FXML
    void btnCancelResetPasswordOnAction(ActionEvent event) {
        cancelResetPassword();
    }

    @FXML
    void btnReturnOrdersOnAction(ActionEvent event) {
        try {
            setEmployeeDashboardStage(event);
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../view/ReturnOrders.fxml"));
            Parent root = loader.load();
            ReturnOrderFormController controller = loader.getController();
            controller.setEmployeeDashboardFormController(this);
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
            stage.setResizable(false);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", e.getMessage(),AlertType.SHOW);
        }
        enableScreen();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        handleDashboardSidePanelBtnClicks(EmployeeDashboardViewType.CATALOG);

        loadDateAndTime();
        loadTables();

        setAllLabels();
        disableCloseSearch();
        setTablesSelectableStates();

        bindResetPasswordFields();
        enableResetPasswordPane(false);
    }

    //====================== MY IMPLEMENTATIONS =======================

    //LOAD TABLES

    private void loadTables() {
        try{
            loadCatalogProductsTable(productService.getAllProducts());
            loadSuppliersTable(supplierService.getAll());
            loadCategoriesTable(categoryService.getAllCategories());
            loadOrdersTable(orderService.getAllOrders());
            loadReturnOrdersTable(returnOrderService.getAllReturnedItems());
        } catch (RepositoryException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", e.getMessage(),AlertType.SHOW);
        }
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

    private void loadCategorizedProducts(ObservableList<Product> categorizedProducts) {
        if (categorizedProducts != null) {
            columnCategorizedProductsProductName.setCellValueFactory(new PropertyValueFactory<>("name"));
            columnCategorizedProductsCategoryId.setCellValueFactory(new PropertyValueFactory<>("productId"));

            columnCategorizedProductsProductName.setStyle("-fx-alignment:center;");
            columnCategorizedProductsCategoryId.setStyle("-fx-alignment:center;");

            tblCategoriesCategorizedProducts.setItems(categorizedProducts);
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

            columnSuppliersAddProductsProductName.setStyle("-fx-alignment:center;");
            columnSuppliersAddProductsCategoryName.setStyle("-fx-alignment:center;");

            tblSuppliersAddProducts.setItems(suppliedProducts);
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

    public void loadReturnOrdersTable(ObservableList<ReturnOrder> allReturnOrderItems){
        if(!allReturnOrderItems.isEmpty()){
            this.allReturnOrderItems = allReturnOrderItems;

            columnReturnsOrderID.setCellValueFactory(cellData -> {
                Order order = cellData.getValue().getOrder();
                return new SimpleStringProperty(order != null ? order.getOrderId() : "-");
            });

            columnReturnsReturnID.setCellValueFactory(new PropertyValueFactory<>("returnOrderId"));
            columnReturnsPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
            columnReturnsReturnDate.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
            columnReturnsProductID.setCellValueFactory(new PropertyValueFactory<>("productId"));
            columnReturnsSize.setCellValueFactory(new PropertyValueFactory<>("size"));

            columnReturnsOrderID.setStyle("-fx-alignment:center;");
            columnReturnsReturnID.setStyle("-fx-alignment:center;");
            columnReturnsPrice.setStyle("-fx-alignment:center;");
            columnReturnsReturnDate.setStyle("-fx-alignment:center;");
            columnReturnsProductID.setStyle("-fx-alignment:center;");
            columnReturnsSize.setStyle("-fx-alignment:center;");

            tblReturnOrders.setItems(allReturnOrderItems);
        }
    }

    public void loadCategoriesTable(ObservableList<Category> allCategories){
        if(!allCategories.isEmpty()){
            this.allCategories = allCategories;

            columnCategoriesId.setCellValueFactory(new PropertyValueFactory<>("categoryId"));
            columnCategoriesName.setCellValueFactory(new PropertyValueFactory<>("name"));

            columnCategoriesId.setStyle("-fx-alignment:center;");
            columnCategoriesName.setStyle("-fx-alignment:center;");

            setActionsToTables(ActionTableType.CATEGORY);
            tblCategories.setItems(allCategories);
        }
    }

    public void loadCategoriesAddProducts(){
        if (!categorizedAddProducts.isEmpty()) {
            columnCategoriesAddProductsProduct.setCellValueFactory(new PropertyValueFactory<>("name"));

            columnCategoriesAddProductsSupplier.setCellValueFactory(cellData -> {
                Supplier supplier = cellData.getValue().getSupplier();
                return new SimpleStringProperty(supplier != null ? supplier.getName() : "-");
            });

            columnCategoriesAddProductsProduct.setStyle("-fx-alignment:center;");
            columnCategoriesAddProductsSupplier.setStyle("-fx-alignment:center;");

            tblCategoriesAddProducts.setItems(categorizedAddProducts);
        }
    }


    //=============


    //OTHER IMPLEMENTATIONS

    private void handleDashboardSidePanelBtnClicks(EmployeeDashboardViewType type) {
        switch (type) {
            case CATALOG:
//                HANDLE BUTTON STYLES WHEN CLICKED
                btnCatalog.setStyle("-fx-background-color: rgba(44, 37, 14, 0.4);");
                btnOrders.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
                btnSuppliers.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
                btnCategories.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
                btnReports.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
//                ENABLE AND SHOW ANCHOR PANE CATALOG
                anchorPaneCatalog.setDisable(false);
                anchorPaneCatalog.setVisible(true);
//                DISABLE AND HIDE REMAINING ANCHOR PANES
                anchorPaneCategories.setDisable(true);
                anchorPaneOrders.setDisable(true);
                anchorPaneReports.setDisable(true);
                anchorPaneSuppliers.setDisable(true);
                anchorPaneCategories.setVisible(false);
                anchorPaneOrders.setVisible(false);
                anchorPaneReports.setVisible(false);
                anchorPaneSuppliers.setVisible(false);
                cancelResetPassword();
                break;
            case CATEGORY:
//                HANDLE BUTTON STYLES WHEN CLICKED
                btnCategories.setStyle("-fx-background-color: rgba(44, 37, 14, 0.4);");
                btnCatalog.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
                btnSuppliers.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
                btnOrders.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
                btnReports.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
//                ENABLE AND SHOW ANCHOR PANE ORDERS
                anchorPaneCategories.setDisable(false);
                anchorPaneCategories.setVisible(true);
//                DISABLE AND HIDE REMAINING ANCHOR PANES
                anchorPaneOrders.setDisable(true);
                anchorPaneCatalog.setDisable(true);
                anchorPaneReports.setDisable(true);
                anchorPaneSuppliers.setDisable(true);
                anchorPaneOrders.setVisible(false);
                anchorPaneCatalog.setVisible(false);
                anchorPaneReports.setVisible(false);
                anchorPaneSuppliers.setVisible(false);
                resetSearch();
                cancelResetPassword();
                break;
            case ORDERS:
//                HANDLE BUTTON STYLES WHEN CLICKED
                btnOrders.setStyle("-fx-background-color: rgba(44, 37, 14, 0.4);");
                btnCatalog.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
                btnSuppliers.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
                btnCategories.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
                btnReports.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
//                ENABLE AND SHOW ANCHOR PANE ORDERS
                anchorPaneOrders.setDisable(false);
                anchorPaneOrders.setVisible(true);
//                DISABLE AND HIDE REMAINING ANCHOR PANES
                anchorPaneCategories.setDisable(true);
                anchorPaneCatalog.setDisable(true);
                anchorPaneReports.setDisable(true);
                anchorPaneSuppliers.setDisable(true);
                anchorPaneCategories.setVisible(false);
                anchorPaneCatalog.setVisible(false);
                anchorPaneReports.setVisible(false);
                anchorPaneSuppliers.setVisible(false);
                resetSearch();
                cancelResetPassword();
                break;
            case SUPPLIERS:
//                HANDLE BUTTON STYLES WHEN CLICKED
                btnSuppliers.setStyle("-fx-background-color: rgba(44, 37, 14, 0.4);");
                btnOrders.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
                btnCatalog.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
                btnCategories.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
                btnReports.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
//                ENABLE AND SHOW ANCHOR PANE ORDERS
                anchorPaneSuppliers.setDisable(false);
                anchorPaneSuppliers.setVisible(true);
//                DISABLE AND HIDE REMAINING ANCHOR PANES
                anchorPaneCategories.setDisable(true);
                anchorPaneOrders.setDisable(true);
                anchorPaneReports.setDisable(true);
                anchorPaneCatalog.setDisable(true);
                anchorPaneCategories.setVisible(false);
                anchorPaneOrders.setVisible(false);
                anchorPaneReports.setVisible(false);
                anchorPaneCatalog.setVisible(false);
                resetSearch();
                cancelResetPassword();
                break;
            case REPORTS:
//                HANDLE BUTTON STYLES WHEN CLICKED
                btnReports.setStyle("-fx-background-color: rgba(44, 37, 14, 0.4);");
                btnOrders.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
                btnSuppliers.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
                btnCategories.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
                btnCatalog.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
//                ENABLE AND SHOW ANCHOR PANE ORDERS
                anchorPaneReports.setDisable(false);
                anchorPaneReports.setVisible(true);
//                DISABLE AND HIDE REMAINING ANCHOR PANES
                anchorPaneCategories.setDisable(true);
                anchorPaneOrders.setDisable(true);
                anchorPaneCatalog.setDisable(true);
                anchorPaneSuppliers.setDisable(true);
                anchorPaneCategories.setVisible(false);
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

                            deleteIcon.setOnMouseClicked(event -> handleDeleteActions(type, tblCatalogProducts.getItems().get(getIndex())));

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

        Callback<TableColumn<Category, Void>, TableCell<Category, Void>> categoryCellFactory = new Callback<>() {
            @Override
            public TableCell<Category, Void> call(final TableColumn<Category, Void> param) {
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

                            deleteIcon.setOnMouseClicked(event -> handleDeleteActions(type, tblCategories.getItems().get(getIndex())));

                            editIcon.setOnMouseClicked(event -> {
                                selectedCategoryToEdit = tblCategories.getItems().get(getIndex());
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
            case CATEGORY:
                columnCategoriesAction.setCellFactory(categoryCellFactory);
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
                case CATEGORY -> new FXMLLoader(getClass().getResource("../../view/EditCategory.fxml"));
            };
            Parent root = loader.load();
            switch (type) {
                case PRODUCTS -> {
                    EditProductFormController editProductFormController = loader.getController();
                    editProductFormController.setEmployeeDashboardFormController(this);
                }
                case SUPPLIERS -> {
                    EditSupplierFormController editSupplierFormController = loader.getController();
                    editSupplierFormController.setEmployeeDashboardFormController(this);
                }
                default -> {
                    EditCategoryFormController editCategoryFormController = loader.getController();
                    editCategoryFormController.setEmployeeDashboardFormController(this);
                }
            }

            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
            stage.setResizable(false);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", e.getMessage(),AlertType.SHOW);
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
            try{
                switch (type) {
                    case PRODUCTS -> {
                        Product product = (Product) object;
                        productService.deleteProduct(product.getProductId());
                        setCatalogPaneLabels();
                        loadCatalogProductsTable(productService.getAllProducts());
                    }
                    case SUPPLIERS -> {
                        Supplier supplier = (Supplier) object;
                        supplierService.deleteSupplier(supplier.getSupplierId());
                        setSuppliersPaneLabels();
                        loadSuppliersTable(supplierService.getAll());
                        loadCatalogProductsTable(productService.getAllProducts());
                    }
                    default -> {
                        Category category = (Category) object;
                        categoryService.deleteCategory(category.getCategoryId());
                        setCategoriesPaneLabels();
                        loadCategoriesTable(categoryService.getAllCategories());
                        loadCatalogProductsTable(productService.getAllProducts());
                    }
                }
            } catch (RepositoryException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", e.getMessage(),AlertType.SHOW);
            }
            showAlert(Alert.AlertType.INFORMATION,"Deletion Successful","Success","Selected property has been successfully deleted.",AlertType.SHOWANDWAIT);
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

        if(newVal.getQuantity() != 0) {
            showNotInStock(false);
            spinnerCatalogQuantity.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, newVal.getQuantity(), 1));
            spinnerCatalogQuantity.setVisible(true);
        }else{
            showNotInStock(true);
        }
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

    private void resetCategoriesTables(){
        tblCategoriesAddProducts.getItems().clear();
        tblCategories.getSelectionModel().clearSelection();
    }

    private void setAllLabels() {
        showNotInStock(false);
        showOrderHasReturnedItems(false);

        setCatalogPaneLabels();
        setSuppliersPaneLabels();
        setOrdersPaneLabels();
        setCategoriesPaneLabels();
    }

    public void setCategoriesPaneLabels(){
        if (allCategories != null) {
            lblTotalCategories.setText(String.valueOf(allCategories.size()));
        } else {
            lblTotalProductsCategories.setText(lblTotalProductsCatalog.getText());
        }
        lblTotalProductsCategories.setText(lblTotalProductsCatalog.getText());
    }

    public void setCatalogPaneLabels() {
        lblTotalOrdersCatalog.setText(allOrders != null ? String.valueOf(allOrders.size()) : "");
        if (allProducts != null) {
            lblTotalProductsCatalog.setText(String.valueOf(allProducts.size()));

            Integer totalStock = 0;
            ObservableList<Product> items = tblCatalogProducts.getItems();
            for (Product product : items) {
                totalStock += product.getQuantity();
            }
            lblTotalStock.setText(String.valueOf(totalStock));
        } else {
            lblTotalProductsCatalog.setText("");
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
        try{
            ObservableList<Customer> customers = customerService.getAllCustomers();
            lblTotalCustomerCount.setText(customers != null ? String.valueOf(customers.size()) : "");
            lblTotalOrdersOrders.setText(allOrders != null ? String.valueOf(allOrders.size()) : "");
            lblTotalReturnOrderCount.setText(allReturnOrderItems != null ? String.valueOf(allReturnOrderItems.size()) : "");
        } catch (RepositoryException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", e.getMessage(),AlertType.SHOW);
        }
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

    private void setTablesSelectableStates(){
        setCatalogProductsSelectableState();
        setSuppliersSelectableState();
        setOrdersSelectableState();
        setCategoriesSelectableState();
    }

    private void setCategoriesSelectableState() {
        tblCategories.getSelectionModel().selectedItemProperty().addListener((observableValue, oldVal, newVal) -> {
            if (newVal != null) {
                try {
                    loadCategorizedProducts(productService.findByCategoryId(newVal.getCategoryId()));
                } catch (RepositoryException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", e.getMessage(),AlertType.SHOW);
                }
            }
        });
    }

    private void setOrdersSelectableState() {
        tblOrders.getSelectionModel().selectedItemProperty().addListener((observableValue, oldVal, newVal) -> {
            if (newVal != null) {
                try {
                    ObservableList<OrderItem> temp = orderItemsService.findOrderItemsByOrderID(newVal.getOrderId());
                    showOrderHasReturnedItems(temp.size()!= newVal.getOrderItemCount());
                    loadOrderItemsTable(temp);
                } catch (RepositoryException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", e.getMessage(),AlertType.SHOW);
                }
            }
        });
    }

    private void setSuppliersSelectableState() {
        tblSuppliers.getSelectionModel().selectedItemProperty().addListener((observableValue, oldVal, newVal) -> {
            if (newVal != null) {
                try {
                    loadSuppliedProductsTable(productService.findBySupplierID(newVal.getSupplierId()));
                } catch (RepositoryException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", e.getMessage(),AlertType.SHOW);
                }
            }
        });
    }

    private void setCatalogProductsSelectableState() {
        tblCatalogProducts.getSelectionModel().selectedItemProperty().addListener((observableValue, oldVal, newVal) -> {
            if (newVal != null) {
                resetSearch();
                addProductValuesToDisplay(newVal);
            }
        });
    }

    private void showOrderHasReturnedItems(boolean state) {
        lblReturnedItemsAvailableForTheOrder.setVisible(state);
    }

    public void loadEmployeeDetails(Employee employee) {
        loggedEmployee = employee;
        lblWelcomeEmployee.setText(String.format("Welcome Back, %s",getEmployeeName(employee)));
        lblEmployeeName.setText(getEmployeeName(employee));
        lblEmployeeID.setText(employee.getEmployeeId() != null ? employee.getEmployeeId():"");
        lblEmployeeNIC.setText(employee.getNic() != null ? employee.getNic():"");
        lblEmployeeEmail.setText(employee.getEmail() != null ? employee.getEmail():"");
        lblEmployeeContact.setText(employee.getPhoneNumber() != null ? employee.getPhoneNumber():"");
        lblEmployeeAddress.setText(employee.getAddress() != null ? employee.getAddress():"");
        lblEmployeeUserName.setText(employee.getUserName() != null ? employee.getUserName():"");
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
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred while generating the report.", e.getMessage(),AlertType.SHOW);
            log.error(e.getMessage());
        }
    }

//    PASSWORD FIELD VALIDATIONS

    private void enableTxtSetNewPassword(boolean state){
        txtSetNewPasswordPane.setDisable(!state);
        txtSetNewPasswordPane.setVisible(state);
    }

    private void enableTxtConfirmNewPassword(boolean state){
        txtConfirmNewPasswordPane.setDisable(!state);
        txtConfirmNewPasswordPane.setVisible(state);
    }

    private void enablePwdSetNewPassword(boolean state){
        pwdSetNewPasswordPane.setDisable(!state);
        pwdSetNewPasswordPane.setVisible(state);
    }

    private void enablePwdConfirmNewPassword(boolean state){
        pwdConfirmNewPasswordPane.setDisable(!state);
        pwdConfirmNewPasswordPane.setVisible(state);
    }

    private void enableResetPasswordPane(boolean state){
        resetPasswordPane.setDisable(!state);
        resetPasswordPane.setVisible(state);
    }

    private void setPwdInitializationStates(){
        enableTxtConfirmNewPassword(false);
        enableTxtSetNewPassword(false);
        enablePwdConfirmNewPassword(true);
        enablePwdSetNewPassword(true);
        pwdConfirmNewPassword.setText("");
        pwdSetNewPassword.setText("");
    }

    private void bindResetPasswordFields(){
        txtSetNewPassword.textProperty().bindBidirectional(pwdSetNewPassword.textProperty());
        txtConfirmNewPassword.textProperty().bindBidirectional(pwdConfirmNewPassword.textProperty());
    }

    private void cancelResetPassword() {
        btnResetPassword.setDisable(false);
        enableResetPasswordPane(false);
        setPwdInitializationStates();
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

    private void showNotInStock(boolean state){
        notInStock.setDisable(!state);
        notInStock.setVisible(state);

        inStock.setDisable(state);
        inStock.setVisible(!state);
    }
}
