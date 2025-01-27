package service.custom.impl;

import entity.EmployeeEntity;
import exceptions.NoEmployeeFoundException;
import exceptions.RepositoryException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Employee;
import org.hibernate.Session;
import org.hibernate.Transaction;
import repository.RepositoryFactory;
import repository.custom.EmployeeRepository;
import service.custom.EmployeeService;
import util.Type;

import java.util.List;

import static repository.SuperRepository.sessionFactory;

public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository= RepositoryFactory.getInstance().getRepositoryType(Type.EMPLOYEE);

    @Override
    public boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailPattern);
    }

    @Override
    public boolean isValidContactNumber(String contactNumber){
        String contactNumberPattern = "^(\\+\\d{1,3})?\\d{10}$";
        return contactNumber.matches(contactNumberPattern);
    }

    @Override
    public boolean isValidNIC(String nic){
        String nicPattern = "^\\d{13}$|^\\d{9}[vV]$\n";
        return nic.matches(nicPattern);
    }


    @Override
    public Employee findByEmail(String email) throws NoEmployeeFoundException, RepositoryException {
        EmployeeEntity employeeEntity = employeeRepository.findByEmail(email);
        if (employeeEntity != null) {
            return new Employee(employeeEntity.getEmployeeId(),
                    employeeEntity.getFirstName(),
                    employeeEntity.getLastName(),
                    employeeEntity.getEmail(),
                    employeeEntity.getPhoneNumber(),
                    employeeEntity.getUserName(),
                    employeeEntity.getNic(),
                    employeeEntity.getAddress(),
                    employeeEntity.getPassword());
        } else {
            throw new NoEmployeeFoundException("No Employee found");
        }
    }

    @Override
    public Employee findByUserName(String userName) throws NoEmployeeFoundException, RepositoryException {
        EmployeeEntity employeeEntity = employeeRepository.findByUserName(userName);
        if (employeeEntity != null) {
            return new Employee(employeeEntity.getEmployeeId(),
                    employeeEntity.getFirstName(),
                    employeeEntity.getLastName(),
                    employeeEntity.getEmail(),
                    employeeEntity.getPhoneNumber(),
                    employeeEntity.getUserName(),
                    employeeEntity.getNic(),
                    employeeEntity.getAddress(),
                    employeeEntity.getPassword());
        } else {
            throw new NoEmployeeFoundException("No Employee found");
        }
    }

    @Override
    public void updateEmployeePassword(String email, String password) throws RepositoryException {
        Transaction transaction = null;
        Session session = sessionFactory.openSession();
        try {
            transaction = session.beginTransaction();

            EmployeeEntity employeeEntity = employeeRepository.findByEmail(email, session);
            if (employeeEntity == null) {
                throw new NoEmployeeFoundException("Employee with email " + email + " not found.");
            }

            employeeEntity.setPassword(password);

            employeeRepository.update(session, employeeEntity);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RepositoryException("Failed to update EmployeeEntity password.");
        } finally {
            session.close();
        }
    }

    @Override
    public ObservableList<Employee> getAll() throws RepositoryException {
        List<EmployeeEntity> employeeEntityList = employeeRepository.findAll();
        ObservableList<Employee> employeeObservableList= FXCollections.observableArrayList();
        if (employeeEntityList!=null){
            employeeEntityList.forEach(entity->employeeObservableList.add(new Employee(
                    entity.getEmployeeId(),
                    entity.getFirstName(),
                    entity.getLastName(),
                    entity.getEmail(),
                    entity.getPhoneNumber(),
                    entity.getUserName(),
                    entity.getNic(),
                    entity.getAddress(),
                    null
            )));
        }
        return employeeObservableList;
    }

    @Override
    public void deleteById(String employeeId) throws RepositoryException {
        employeeRepository.deleteById(employeeId);
    }

    @Override
    public void save(Employee employee) throws RepositoryException {
        employeeRepository.save(
            new EmployeeEntity(
                    null,
                    null,
                    employee.getFirstName(),
                    employee.getLastName(),
                    employee.getEmail(),
                    employee.getPhoneNumber(),
                    employee.getUserName(),
                    employee.getNic(),
                    employee.getAddress(),
                    employee.getPassword()
            )
        );
    }

    @Override
    public void update(Employee employee) throws RepositoryException {
        employeeRepository.update(
            new EmployeeEntity(
                    null,
                    employee.getEmployeeId(),
                    employee.getFirstName(),
                    employee.getLastName(),
                    employee.getEmail(),
                    employee.getPhoneNumber(),
                    employee.getUserName(),
                    employee.getNic(),
                    employee.getAddress(),
                    null
            )
        );
    }
}
