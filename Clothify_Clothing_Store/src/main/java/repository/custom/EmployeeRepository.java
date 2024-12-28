package repository.custom;

import entity.EmployeeEntity;
import repository.SuperRepository;

public interface EmployeeRepository extends SuperRepository {
    EmployeeEntity findByEmail(String email);

    EmployeeEntity findByUserName(String userName);

    boolean saveOrUpdate(EmployeeEntity employeeEntity);

    boolean update(EmployeeEntity employeeEntity);
}
