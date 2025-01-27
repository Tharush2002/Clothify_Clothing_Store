package service.custom;

import exceptions.NoEmployeeFoundException;
import exceptions.RepositoryException;
import javafx.collections.ObservableList;
import model.Employee;
import service.SuperService;

public interface EmployeeService extends SuperService {
    boolean isValidEmail(String email);

    boolean isValidContactNumber(String contactNumber);

    boolean isValidNIC(String nic);

    Employee findByEmail(String email) throws NoEmployeeFoundException, RepositoryException;

    Employee findByUserName(String trim) throws NoEmployeeFoundException, RepositoryException;

    void updateEmployeePassword(String email, String password) throws RepositoryException;

    ObservableList<Employee> getAll() throws RepositoryException;

    void deleteById(String employeeId) throws RepositoryException;

    void save(Employee employee) throws RepositoryException;

    void update(Employee employee) throws RepositoryException;
}
