package controller.returnOrders;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
import javafx.scene.layout.VBox;
import lombok.Setter;
import model.Order;
import model.OrderItem;
import model.ReturnOrder;
import service.ServiceFactory;
import service.custom.OrderItemsService;
import service.custom.OrderService;
import service.custom.ProductService;
import service.custom.ReturnOrderService;
import util.AlertType;
import util.Type;

import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;


@Setter
public class ReturnOrderFormController implements Initializable {
    private final ProductService productService = ServiceFactory.getInstance().getServiceType(Type.PRODUCT);
    private final OrderService orderService = ServiceFactory.getInstance().getServiceType(Type.ORDER);
    private final OrderItemsService orderItemsService = ServiceFactory.getInstance().getServiceType(Type.ORDERITEMS);
    private final ReturnOrderService returnOrderService = ServiceFactory.getInstance().getServiceType(Type.RETURNORDER);

    private ObservableList<Order> allOrderList = FXCollections.observableArrayList();
    private ObservableList<OrderItem> allOrderItemList = FXCollections.observableArrayList();
    private ObservableList<ReturnOrder> allReturnOrderItemsList = FXCollections.observableArrayList();

    private EmployeeDashboardFormController employeeDashboardFormController;

    private Double totalReturnCost;

    @FXML
    private Button btnReturnItem;

    @FXML
    private ComboBox<String> cmbSetOrderToReturn;

    @FXML
    private TableColumn<OrderItem, String> columnOrdersProductName;

    @FXML
    private TableColumn<OrderItem, String> columnOrdersProductSize;

    @FXML
    private TableColumn<OrderItem, Double> columnOrdersUnitPrice;

    @FXML
    private TableColumn<ReturnOrder, String> columnReturnsProductName;

    @FXML
    private TableColumn<ReturnOrder, String> columnReturnsProductSize;

    @FXML
    private TableColumn<ReturnOrder, Double> columnReturnsUnitPrice;

    @FXML
    private Label lblCustomerEmail;

    @FXML
    private Label lblCustomerName;

    @FXML
    private Label lblNoOrdersAvailable;

    @FXML
    private Label lblTotalReturnCost;

    @FXML
    private TableView<OrderItem> tblOrderItems;

    @FXML
    private TableView<ReturnOrder> tblReturnedItems;

    @FXML
    void btnClearReturnOrderOnAction(ActionEvent event) {
        ObservableList<OrderItem> temp = FXCollections.observableArrayList(allOrderItemList);
        loadOrderItemsTable(temp);
        tblReturnedItems.getItems().clear();
        resetTotalReturnCost();
    }

    @FXML
    void btnCloseReturnOrdersOnAction(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
        Scene scene = EmployeeDashboardFormController.getInstance().getEmployeeDashboardStage().getScene();
        AnchorPane root = (AnchorPane) scene.getRoot();
        VBox vbox = (VBox) root.getChildren().get(8);
        vbox.setVisible(false);
        vbox.setDisable(true);
    }

    @FXML
    void btnConfirmReturnOrderOnAction(ActionEvent event) {
        try{
            if(!allReturnOrderItemsList.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Order Confirmation");
                alert.setHeaderText("Confirm Order");
                alert.setContentText("Do you want to confirm the order?");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    for(ReturnOrder returnOrder : allReturnOrderItemsList){
                        returnOrderService.save(returnOrder);
                    }
                    showAlert(Alert.AlertType.INFORMATION,"Success","Return Order Success","Your items for return has been processed.",AlertType.SHOWANDWAIT);

                    employeeDashboardFormController.loadCatalogProductsTable(productService.getAllProducts());
                    employeeDashboardFormController.loadReturnOrdersTable(returnOrderService.getAllReturnedItems());
                    employeeDashboardFormController.setCatalogPaneLabels();
                    employeeDashboardFormController.setOrdersPaneLabels();

                    btnCloseReturnOrdersOnAction(event);
                }
            }else{
                showAlert(Alert.AlertType.ERROR,"Error","No Return Orders Available","Please input the items to return for the order.",AlertType.SHOW);
            }
        } catch (RepositoryException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", e.getMessage(),AlertType.SHOW);
        }
    }

    @FXML
    void btnReturnItemOnAction(ActionEvent event) {
        OrderItem selectedItem = tblOrderItems.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            allReturnOrderItemsList.add(
                    new ReturnOrder(
                            null,
                            selectedItem.getOrder(),
                            selectedItem.getProductName(),
                            selectedItem.getProductId(),
                            selectedItem.getCategoryId(),
                            selectedItem.getCategoryName(),
                            selectedItem.getSupplierId(),
                            selectedItem.getSupplierName(),
                            selectedItem.getUnitPrice(),
                            selectedItem.getSize(),
                            LocalDate.now()
                            ));
            loadReturnedItemsTable(allReturnOrderItemsList);
            tblOrderItems.getItems().remove(selectedItem);
            calculateTotalReturnCost(selectedItem.getUnitPrice());
            clearFocusFromTables();
        }
    }

    @FXML
    void setOrderDetailsOnAction(ActionEvent event) {
        try{
            tblOrderItems.getItems().clear();
            for (Order order : allOrderList) {
                if (order.getOrderId().equals(cmbSetOrderToReturn.getValue())) {
                    lblCustomerName.setText(order.getCustomer().getName());
                    lblCustomerEmail.setText(order.getCustomer().getEmail());
                }
            }
            allOrderItemList = orderItemsService.findOrderItemsByOrderID(cmbSetOrderToReturn.getValue());
            ObservableList<OrderItem> temp = FXCollections.observableArrayList(allOrderItemList);
            loadOrderItemsTable(temp);
            tblReturnedItems.getItems().clear();
            resetTotalReturnCost();
        } catch (RepositoryException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", e.getMessage(),AlertType.SHOW);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeComboBox();
        setReturnItemButtonStatus();
        lblNoOrdersAvailable.setVisible(false);
    }

    private void loadOrderItemsTable(ObservableList<OrderItem> orderItemsList) {
        if(!orderItemsList.isEmpty()){
            lblNoOrdersAvailable.setVisible(false);

            columnOrdersProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
            columnOrdersProductSize.setCellValueFactory(new PropertyValueFactory<>("size"));
            columnOrdersUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));

            columnOrdersProductName.setStyle("-fx-alignment:center;");
            columnOrdersProductSize.setStyle("-fx-alignment:center;");
            columnOrdersUnitPrice.setStyle("-fx-alignment:center;");

            tblOrderItems.setItems(orderItemsList);
        }else{
            lblNoOrdersAvailable.setVisible(true);
        }
    }

    private void loadReturnedItemsTable(ObservableList<ReturnOrder> returnOrderItemsList) {
        if(returnOrderItemsList != null){
            columnReturnsProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
            columnReturnsProductSize.setCellValueFactory(new PropertyValueFactory<>("size"));
            columnReturnsUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));

            columnReturnsProductName.setStyle("-fx-alignment:center;");
            columnReturnsProductSize.setStyle("-fx-alignment:center;");
            columnReturnsUnitPrice.setStyle("-fx-alignment:center;");

            tblReturnedItems.setItems(returnOrderItemsList);
        }
    }

    private void clearFocusFromTables(){
        tblOrderItems.getSelectionModel().clearSelection();
        tblReturnedItems.getSelectionModel().clearSelection();

        cmbSetOrderToReturn.requestFocus();
    }

    private void initializeComboBox(){
        try{
            ObservableList<String> orderIdList = FXCollections.observableArrayList();
            allOrderList = orderService.getAllOrders();
            allOrderList.forEach(order -> orderIdList.add(order.getOrderId()));
            cmbSetOrderToReturn.setItems(orderIdList);
        } catch (RepositoryException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", e.getMessage(),AlertType.SHOW);
        }
    }

    private void setReturnItemButtonStatus(){
        btnReturnItem.setDisable(true);
        tblOrderItems.getSelectionModel().selectedItemProperty().addListener((observableValue, oldVal, newVal) -> {
            btnReturnItem.setDisable(newVal == null);
        });
    }

    private void calculateTotalReturnCost(Double unitPrice) {
        totalReturnCost+=unitPrice;
        lblTotalReturnCost.setText(String.format("Rs. %.2f",totalReturnCost));
    }

    private void resetTotalReturnCost(){
        totalReturnCost=0.0;
        lblTotalReturnCost.setText("");
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
