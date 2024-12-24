package service.custom;

import exceptions.NoEmployeeFoundException;
import exceptions.NoPasswordMatchFoundException;
import model.Employee;
import service.SuperService;

public interface EmployeeService extends SuperService {
    boolean isValidEmail(String email);

    Employee findByEmail(String email) throws NoEmployeeFoundException;

    Employee findByUserName(String trim) throws NoEmployeeFoundException;

    boolean saveOrUpdate(Employee employee, int i);
}
