package repository.custom.impl;

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
    public CustomerEntity findByNameEmailPhoneNumber(Session session, String name, String email, String phoneNumber) {
        return session
                .createQuery("FROM CustomerEntity WHERE name = :name AND email = :email AND phoneNumber = :phoneNumber", CustomerEntity.class)
                .setParameter("name", name)
                .setParameter("email", email)
                .setParameter("phoneNumber", phoneNumber)
                .uniqueResult();
    }
}
