package service.custom.impl;

import entity.CustomerEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import repository.RepositoryFactory;
import repository.custom.CustomerRepository;
import service.custom.CustomerService;
import util.Type;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository= RepositoryFactory.getInstance().getRepositoryType(Type.CUSTOMER);

    @Override
    public ObservableList<Customer> getAllCustomers() {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        List<CustomerEntity> customerEntityList = customerRepository.findAll();

        customerEntityList.forEach(customerEntity -> customers.add(new Customer(
                customerEntity.getCustomerId(),
                customerEntity.getName(),
                customerEntity.getEmail(),
                customerEntity.getPhoneNumber())
        ));
        return customers;
    }

    @Override
    public boolean isValidPhoneNumber(String phoneNumber) {
        String phoneNumberPattern = "^(\\+\\d{1,3}[- ]?)?\\(?\\d{3}\\)?[- ]?\\d{3}[- ]?\\d{4}$";
        return phoneNumber.matches(phoneNumberPattern);
    }

    @Override
    public boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailPattern);
    }
}
