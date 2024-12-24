package service.custom.impl;

import entity.AdminEntity;
import entity.EmployeeEntity;
import exceptions.NoAdminFoundException;
import exceptions.NoEmployeeFoundException;
import exceptions.NoPasswordMatchFoundException;
import model.Admin;
import model.Employee;
import repository.RepositoryFactory;
import repository.custom.EmployeeRepository;
import service.custom.EmployeeService;
import util.Type;

public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository= RepositoryFactory.getInstance().getRepositoryType(Type.EMPLOYEE);

    @Override
    public boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailPattern);
    }

    @Override
    public Employee findByEmail(String email) throws NoEmployeeFoundException {
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
    public Employee findByUserName(String userName) throws  NoEmployeeFoundException {
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
    public boolean saveOrUpdate(Employee employee, int id) {
        employeeRepository.saveOrUpdate(
                new EmployeeEntity(
                        id,
                        employee.getEmployeeId(),
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
        return true;
    }
}
