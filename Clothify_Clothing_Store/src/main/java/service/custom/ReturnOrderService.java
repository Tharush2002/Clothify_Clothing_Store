package service.custom;

import exceptions.RepositoryException;
import javafx.collections.ObservableList;
import model.ReturnOrder;
import service.SuperService;

public interface ReturnOrderService extends SuperService {
    void save(ReturnOrder returnOrder) throws RepositoryException;

    ObservableList<ReturnOrder> getAllReturnedItems() throws RepositoryException;
}
