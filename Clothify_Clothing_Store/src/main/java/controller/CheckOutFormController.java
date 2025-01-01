package controller;

import com.jfoenix.controls.JFXButton;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import lombok.Setter;
import model.Order;
import model.OrderItem;
import model.Product;
import model.OrderItemWithQuantity;
import service.ServiceFactory;
import service.custom.CustomerService;
import service.custom.OrderItemsService;
import service.custom.OrderService;
import service.custom.ProductService;
import util.AlertType;
import util.Type;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@Setter
public class CheckOutFormController implements Initializable {
    private final ProductService productService= ServiceFactory.getInstance().getServiceType(Type.PRODUCT);
    private final OrderItemsService orderItemsService= ServiceFactory.getInstance().getServiceType(Type.ORDERITEMS);
    private final OrderService orderService= ServiceFactory.getInstance().getServiceType(Type.ORDER);
    private final CustomerService customerService = ServiceFactory.getInstance().getServiceType(Type.CUSTOMER);
    private Button checkOutButton;
    private EmployeeDashboardFormController employeeDashboardFormController;

    @FXML
    private JFXButton btnConfirmOrder;

    @FXML
    private JFXComboBox<String> cmbSetPaymentType;

    @FXML
    private TableColumn<OrderItem, Void> columnOrdersAction;

    @FXML
    private TableColumn<OrderItem, String> columnOrdersProductCategory;

    @FXML
    private TableColumn<OrderItem, String> columnOrdersProductID;

    @FXML
    private TableColumn<OrderItem, String> columnOrdersProductName;

    @FXML
    private TableColumn<OrderItem, String> columnOrdersProductSize;

    @FXML
    private TableColumn<OrderItem, String> columnOrdersProductSupplier;

    @FXML
    private TableColumn<OrderItem, Double> columnOrdersUnitPrice;

    @FXML
    private TableView<OrderItem> tblOrderItems;

    @FXML
    private Label lblTotalCost;

    @FXML
    private JFXTextField txtSetCustomerEmail;

    @FXML
    private JFXTextField txtSetCustomerName;

    @FXML
    private JFXTextField txtSetCustomerPhoneNumber;

    @FXML
    void btnClearOrderOnAction(ActionEvent event) {
        try{
            EmployeeDashboardFormController.getInstance().getOrderItemWithQuantityList().clear();
            checkOutButton.setDisable(true);
            employeeDashboardFormController.loadCatalogProductsTable(productService.getAllProducts());
            btnCloseCheckOutOnAction(event);
        } catch (RepositoryException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", e.getMessage(),AlertType.SHOW);
        }
    }

    @FXML
    void btnConfirmOrderOnAction(ActionEvent event) {
        try{
            String name = txtSetCustomerName.getText().trim();
            String email = txtSetCustomerEmail.getText().trim();
            String phoneNumber = txtSetCustomerPhoneNumber.getText().trim();

            List<OrderItemWithQuantity> orderItemWithQuantityList = EmployeeDashboardFormController.getInstance().getOrderItemWithQuantityList();

            if(!name.isEmpty() && customerService.isValidEmail(email) && customerService.isValidPhoneNumber(phoneNumber) && !orderItemWithQuantityList.isEmpty() && !cmbSetPaymentType.getValue().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Order Confirmation");
                alert.setHeaderText("Confirm Order");
                alert.setContentText("Do you want to confirm the order?");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    LocalDate date = LocalDate.now();
                    LocalTime time = LocalTime.now().withNano(0);

                    for(OrderItemWithQuantity orderItemWithQuantity : orderItemWithQuantityList){
                        Order tempOrder = orderItemWithQuantity.getOrderItem().getOrder();
                        tempOrder.setDate(date);
                        tempOrder.setTime(time);
                        tempOrder.setPaymentType(cmbSetPaymentType.getValue());
                        tempOrder.getCustomer().setName(name);
                        tempOrder.getCustomer().setEmail(email);
                        tempOrder.getCustomer().setPhoneNumber(phoneNumber);
                        tempOrder.setOrderItemCount(tblOrderItems.getItems().size());
                        orderItemsService.saveOrder(orderItemWithQuantity.getOrderItem() , orderItemWithQuantity.getQuantity());
                    }

                    employeeDashboardFormController.loadCatalogProductsTable(productService.getAllProducts());
                    employeeDashboardFormController.loadOrdersTable(orderService.getAllOrders());
                    employeeDashboardFormController.setCatalogPaneLabels();
                    employeeDashboardFormController.setOrdersPaneLabels();

                    showAlert(Alert.AlertType.INFORMATION,"Confirmation Successful","Success","Order Has Been Processed",AlertType.SHOWANDWAIT);

                    btnClearOrderOnAction(event);
                }
                return;
            }
            showAlert(Alert.AlertType.ERROR,"Error","Check All The Fields Correctly","Please Enter All the Fields with Correct Data",AlertType.SHOW);
        } catch (RepositoryException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", e.getMessage(),AlertType.SHOW);
        }
    }

    @FXML
    void btnCloseCheckOutOnAction(ActionEvent event) {
        if(EmployeeDashboardFormController.getInstance().getOrderItemWithQuantityList().isEmpty()){
            checkOutButton.setDisable(true);
        }
        ((Node) (event.getSource())).getScene().getWindow().hide();
        Scene scene = EmployeeDashboardFormController.getInstance().getEmployeeDashboardStage().getScene();
        AnchorPane root = (AnchorPane) scene.getRoot();
        VBox vbox = (VBox) root.getChildren().get(7);
        vbox.setVisible(false);
        vbox.setDisable(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnConfirmOrder.setDisable(false);
        setPaymentTypes();
        loadOrderItemsTable(EmployeeDashboardFormController.getInstance().getOrderItemWithQuantityList());
        calculateTotal();
    }

    private void setPaymentTypes() {
        ObservableList<String> paymentTypeList = FXCollections.observableArrayList();
        paymentTypeList.add("Cash");
        paymentTypeList.add("Card");
        paymentTypeList.add("CryptoCurrency");
        cmbSetPaymentType.setValue("");
        cmbSetPaymentType.setItems(paymentTypeList);
    }

    private void calculateTotal() {
        double total=0.0;
        List<OrderItemWithQuantity> orderItemWithQuantityList = EmployeeDashboardFormController.getInstance().getOrderItemWithQuantityList();
        for (OrderItemWithQuantity orderItemWithQuantity : orderItemWithQuantityList) {
            total += (orderItemWithQuantity.getOrderItem().getUnitPrice() * orderItemWithQuantity.getQuantity());
        }
        lblTotalCost.setText("Rs. "+total);
    }

    private void loadOrderItemsTable(List<OrderItemWithQuantity> orderItemWithQuantityList) {
        if (!orderItemWithQuantityList.isEmpty()){
            ObservableList<OrderItem> orderItems = FXCollections.observableArrayList();
            orderItemWithQuantityList.forEach(orderItemWithQuantity -> {
                for(int i=0 ; i<orderItemWithQuantity.getQuantity() ; i++){
                    orderItems.add(orderItemWithQuantity.getOrderItem());
                }
            });

            columnOrdersProductID.setCellValueFactory(new PropertyValueFactory<>("productId"));
            columnOrdersProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
            columnOrdersProductCategory.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
            columnOrdersProductSupplier.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
            columnOrdersUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
            columnOrdersProductSize.setCellValueFactory(new PropertyValueFactory<>("size"));

            columnOrdersProductID.setStyle("-fx-alignment:center;");
            columnOrdersProductName.setStyle("-fx-alignment:center;");
            columnOrdersProductCategory.setStyle("-fx-alignment:center;");
            columnOrdersProductSupplier.setStyle("-fx-alignment:center;");
            columnOrdersUnitPrice.setStyle("-fx-alignment:center;");
            columnOrdersProductSize.setStyle("-fx-alignment:center;");

            setActionsToTable();
            tblOrderItems.setItems(orderItems);
        }
    }

    private void setActionsToTable(){
        Callback<TableColumn<OrderItem, Void>, TableCell<OrderItem, Void>> orderItemsCellFactory = new Callback<>() {
            @Override
            public TableCell<OrderItem, Void> call(final TableColumn<OrderItem, Void> param) {
                return new TableCell<>() {
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            Button removeIcon = new Button("Remove");
                            removeIcon.setStyle("-fx-cursor: hand ;");

                            removeIcon.setOnMouseClicked(event -> {
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Confirmation Dialog");
                                alert.setHeaderText("Are you sure you want to delete the selected Item?");
                                alert.setContentText("Please confirm your action.");
                                Optional<ButtonType> result = alert.showAndWait();

                                if (result.isPresent() && result.get() == ButtonType.OK) {
                                    showAlert(Alert.AlertType.INFORMATION,"Deletion Successful","Success","Selected property has been successfully deleted.",AlertType.SHOWANDWAIT);

                                    OrderItem orderItem = tblOrderItems.getItems().get(getIndex());

                                    for (int index = 0; EmployeeDashboardFormController.getInstance().getOrderItemWithQuantityList().size()>index; index++){

                                        boolean isProductIdMatches = orderItem.getProductId().equals(EmployeeDashboardFormController.getInstance().getOrderItemWithQuantityList().get(index).getOrderItem().getProductId());
                                        boolean isSizeMatches = orderItem.getSize().equals(EmployeeDashboardFormController.getInstance().getOrderItemWithQuantityList().get(index).getOrderItem().getSize());

                                        if (isProductIdMatches && isSizeMatches){

                                            if(EmployeeDashboardFormController.getInstance().getOrderItemWithQuantityList().get(index).getQuantity()>1){
                                                EmployeeDashboardFormController.getInstance().getOrderItemWithQuantityList().get(index).setQuantity(EmployeeDashboardFormController.getInstance().getOrderItemWithQuantityList().get(index).getQuantity()-1);
                                            }else{
                                                EmployeeDashboardFormController.getInstance().getOrderItemWithQuantityList().remove(index);
                                            }

                                            if (EmployeeDashboardFormController.getInstance().getOrderItemWithQuantityList().isEmpty()){
                                                tblOrderItems.getItems().clear();
                                                btnConfirmOrder.setDisable(true);
                                            }

                                            for(int i=0;EmployeeDashboardFormController.getInstance().getAllProducts().size()>i;i++){
                                                if(EmployeeDashboardFormController.getInstance().getAllProducts().get(i).getProductId().equals(orderItem.getProductId())){
                                                    EmployeeDashboardFormController.getInstance().getAllProducts().get(i).setQuantity(EmployeeDashboardFormController.getInstance().getAllProducts().get(i).getQuantity()+1);
                                                }
                                            }

                                            loadOrderItemsTable(EmployeeDashboardFormController.getInstance().getOrderItemWithQuantityList());
                                            employeeDashboardFormController.loadCatalogProductsTable(EmployeeDashboardFormController.getInstance().getAllProducts());
                                            calculateTotal();
                                            return;
                                        }
                                    }
                                }
                            });

                            HBox managebtn = new HBox(removeIcon);
                            managebtn.setStyle("-fx-alignment:center");
                            managebtn.setSpacing(8);
                            setGraphic(managebtn);
                        }
                    }
                };
            }
        };
        columnOrdersAction.setCellFactory(orderItemsCellFactory);
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
