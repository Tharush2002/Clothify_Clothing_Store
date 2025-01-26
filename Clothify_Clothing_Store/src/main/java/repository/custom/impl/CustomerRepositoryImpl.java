package repository.custom.impl;

import entity.AdminEntity;
import entity.CustomerEntity;
import entity.ProductEntity;
import exceptions.RepositoryException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import repository.custom.CustomerRepository;

import java.util.List;

public class CustomerRepositoryImpl implements CustomerRepository {
    @Override
    public List<CustomerEntity> findAll() throws RepositoryException {
        Transaction transaction = null;
        List<CustomerEntity> customerEntityList = null;
        Session session = sessionFactory.openSession();
        try{
            transaction = session.beginTransaction();
            customerEntityList = session.createQuery("from CustomerEntity", CustomerEntity.class).list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RepositoryException("Failed to fetch customer entity records.");
        }finally{
            session.close();
        }
        return customerEntityList;
    }

    @Override
    public CustomerEntity findByCustomerId(Session session, String customerId) {
        return session
                .createQuery("FROM CustomerEntity WHERE customerId = :customerId", CustomerEntity.class)
                .setParameter("customerId", customerId)
                .uniqueResult();
    }

    @Override
    public CustomerEntity findByCustomerId(String customerId) throws RepositoryException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        CustomerEntity customerEntity;

        try {
            transaction = session.beginTransaction();
            customerEntity = session
                    .createQuery("FROM CustomerEntity WHERE customerId = :customerId", CustomerEntity.class)
                    .setParameter("customerId", customerId)
                    .uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RepositoryException("Failed to find the specific customer entity record.");
        } finally {
            session.close();
        }
        return customerEntity;
    }
}
