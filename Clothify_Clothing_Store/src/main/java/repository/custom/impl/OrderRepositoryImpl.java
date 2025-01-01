package repository.custom.impl;

import entity.CustomerEntity;
import entity.OrderEntity;
import exceptions.RepositoryException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import repository.custom.OrderRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class OrderRepositoryImpl implements OrderRepository {
    @Override
    public List<OrderEntity> findAll() throws RepositoryException {
        Transaction transaction = null;
        List<OrderEntity> orderEntityList = null;
        Session session = sessionFactory.openSession();
        try{
            transaction = session.beginTransaction();
            orderEntityList = session.createQuery("from OrderEntity", OrderEntity.class).list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RepositoryException("Failed to fetch the order entity records.");
        }finally{
            session.close();
        }
        return orderEntityList;
    }

    @Override
    public OrderEntity findByCustomerDateTime(Session session, CustomerEntity customerEntity, LocalDate date, LocalTime time) {
        return session
                .createQuery("FROM OrderEntity WHERE customerEntity = :customer AND date = :date AND time = :time", OrderEntity.class)
                .setParameter("customer", customerEntity)
                .setParameter("date", date)
                .setParameter("time", time)
                .uniqueResult();
    }

    @Override
    public OrderEntity findByOrderId(Session session, String orderId) {
        return session
                .createQuery("FROM OrderEntity WHERE orderId = :orderId", OrderEntity.class)
                .setParameter("orderId", orderId)
                .uniqueResult();
    }
}
