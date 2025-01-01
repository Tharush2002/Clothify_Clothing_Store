package repository.custom;

import entity.CustomerEntity;
import exceptions.RepositoryException;
import org.hibernate.Session;
import repository.SuperRepository;

import java.util.List;

public interface CustomerRepository extends SuperRepository {
    List<CustomerEntity> findAll() throws RepositoryException;

    CustomerEntity findByNameEmailPhoneNumber(Session session, String name, String email, String phoneNumber);
}
