package service.custom;

import javafx.collections.ObservableList;
import model.Order;
import service.SuperService;

public interface OrderService extends SuperService {
    ObservableList<Order> getAllOrders();
}
