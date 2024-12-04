package service.custom;

import javafx.collections.ObservableList;
import model.Customer;
import service.SuperService;

public interface CustomerService extends SuperService {
    ObservableList<Customer> getAllCustomers();

    boolean isValidPhoneNumber(String phoneNumber);

    boolean isValidEmail(String email);
}
