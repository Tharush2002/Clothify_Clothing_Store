package service.custom;

import exceptions.RepositoryException;
import javafx.collections.ObservableList;
import model.OrderItem;
import service.SuperService;

public interface OrderItemsService extends SuperService {
    ObservableList<OrderItem> findOrderItemsByOrderID(String orderId) throws RepositoryException;

    void saveOrder(OrderItem orderItem, Integer quantity) throws RepositoryException;
}
