package repository.custom;

import entity.CustomerEntity;
import exceptions.RepositoryException;
import org.hibernate.Session;
import repository.SuperRepository;

import java.util.List;

public interface CustomerRepository extends SuperRepository {
    List<CustomerEntity> findAll() throws RepositoryException;

    CustomerEntity findByCustomerId(Session session, String customerId);

    CustomerEntity findByCustomerId(String customerId) throws RepositoryException;
}
