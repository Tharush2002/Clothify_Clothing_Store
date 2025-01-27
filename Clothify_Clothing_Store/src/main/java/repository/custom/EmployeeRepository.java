package repository.custom;

import entity.EmployeeEntity;
import exceptions.RepositoryException;
import org.hibernate.Session;
import repository.SuperRepository;

import java.util.List;

public interface EmployeeRepository extends SuperRepository {
    EmployeeEntity findByEmail(String email) throws RepositoryException;

    EmployeeEntity findByUserName(String userName) throws RepositoryException;

    void update(EmployeeEntity employeeEntity) throws RepositoryException;

    List<EmployeeEntity> findAll() throws RepositoryException;

    void deleteById(String employeeId) throws RepositoryException;

    void save(EmployeeEntity employeeEntity) throws RepositoryException;

    EmployeeEntity findByEmail(String email, Session session);

    void update(Session session, EmployeeEntity employeeEntity);
}
