package service.custom;

import javafx.collections.ObservableList;
import model.OrderItem;
import service.SuperService;

public interface OrderItemsService extends SuperService {
    ObservableList<OrderItem> findOrderItemsByOrderID(String orderId);

    boolean saveOrder(OrderItem orderItem, Integer quantity);
}
