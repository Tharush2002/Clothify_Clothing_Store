package service.custom;

import javafx.collections.ObservableList;
import model.ReturnOrder;
import service.SuperService;

import java.util.List;

public interface ReturnOrderService extends SuperService {
    boolean save(ReturnOrder returnOrder);

    ObservableList<ReturnOrder> getAllReturnedItems();
}
