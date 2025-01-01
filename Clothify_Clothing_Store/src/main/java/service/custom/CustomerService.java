package service.custom;

import exceptions.RepositoryException;
import javafx.collections.ObservableList;
import model.Customer;
import service.SuperService;

public interface CustomerService extends SuperService {
    ObservableList<Customer> getAllCustomers() throws RepositoryException;

    boolean isValidPhoneNumber(String phoneNumber);

    boolean isValidEmail(String email);
}
