package service.custom;

import exceptions.RepositoryException;
import javafx.collections.ObservableList;
import model.Order;
import service.SuperService;

public interface OrderService extends SuperService {
    ObservableList<Order> getAllOrders() throws RepositoryException;
}
