package repository.custom.impl;

import entity.CustomerEntity;
import entity.OrderEntity;
import entity.OrderItemEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import repository.custom.OrderItemsRepository;

import java.util.List;

public class OrderItemsRepositoryImpl implements OrderItemsRepository {
    @Override
    public List<OrderItemEntity> findAll() {
        Transaction transaction = null;
        List<OrderItemEntity> orderItemEntityList = null;
        Session session = sessionFactory.openSession();
        try{
            transaction = session.beginTransaction();
            orderItemEntityList = session.createQuery("from OrderItemEntity", OrderItemEntity.class).list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }finally{
            session.close();
        }
        return orderItemEntityList;
    }

    @Override
    public List<OrderItemEntity> findByOrderID(String orderId) {
        List<OrderItemEntity> orderItems = null;
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            String hql = "SELECT oi FROM OrderItemEntity oi " +
                    "JOIN oi.orderEntity o " +
                    "WHERE o.orderId = :orderId";

            Query<OrderItemEntity> query = session.createQuery(hql, OrderItemEntity.class);
            query.setParameter("orderId", orderId);
            orderItems = query.getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return orderItems;
    }

    @Override
    public void save(OrderItemEntity orderItemEntity) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            Query<CustomerEntity> customerEntityQuery = session.createQuery("FROM CustomerEntity WHERE name = :name AND email = :email AND phoneNumber = :phoneNumber", CustomerEntity.class);
            customerEntityQuery.setParameter("name", orderItemEntity.getOrderEntity().getCustomerEntity().getName());
            customerEntityQuery.setParameter("email", orderItemEntity.getOrderEntity().getCustomerEntity().getEmail());
            customerEntityQuery.setParameter("phoneNumber", orderItemEntity.getOrderEntity().getCustomerEntity().getPhoneNumber());
            CustomerEntity customerEntity = customerEntityQuery.uniqueResult();

            if (customerEntity == null) {
                customerEntity = new CustomerEntity();
                customerEntity.setName(orderItemEntity.getOrderEntity().getCustomerEntity().getName());
                customerEntity.setEmail(orderItemEntity.getOrderEntity().getCustomerEntity().getEmail());
                customerEntity.setPhoneNumber(orderItemEntity.getOrderEntity().getCustomerEntity().getPhoneNumber());
                session.save(customerEntity);
                session.flush();
            }

            Query<OrderEntity> orderEntityQuery = session.createQuery("FROM OrderEntity WHERE customerEntity = :customer AND date = :date AND time = :time", OrderEntity.class);
            orderEntityQuery.setParameter("customer", customerEntity);
            orderEntityQuery.setParameter("date", orderItemEntity.getOrderEntity().getDate());
            orderEntityQuery.setParameter("time", orderItemEntity.getOrderEntity().getTime());
            OrderEntity orderEntity = orderEntityQuery.uniqueResult();

            if (orderEntity == null) {
                orderEntity = new OrderEntity();
                orderEntity.setCustomerEntity(customerEntity);
                orderEntity.setDate(orderItemEntity.getOrderEntity().getDate());
                orderEntity.setTime(orderItemEntity.getOrderEntity().getTime());
                orderEntity.setTotal(orderItemEntity.getOrderEntity().getTotal());
                orderEntity.setPaymentType(orderItemEntity.getOrderEntity().getPaymentType());
                orderEntity.setOrderItemCount(orderItemEntity.getOrderEntity().getOrderItemCount());
                session.save(orderEntity);
                session.flush();
            }

            orderItemEntity.setOrderEntity(orderEntity);

            session.save(orderItemEntity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @Override
    public void deleteBySizeAndId(String orderId, String productId, String size, Session session) {
        Query<OrderItemEntity> query = session.createQuery(
                "FROM OrderItemEntity WHERE orderEntity.orderId = :orderId AND productId = :productId AND size = :size",
                OrderItemEntity.class
        );
        query.setParameter("orderId", orderId);
        query.setParameter("productId", productId);
        query.setParameter("size", size);

        query.setMaxResults(1);
        OrderItemEntity orderItemToDelete = query.uniqueResult();

        if (orderItemToDelete != null) {
            orderItemToDelete.setOrderEntity(null);
            session.delete(orderItemToDelete);
        } else {
            throw new RuntimeException("Order item not found for deletion with the provided details.");
        }
    }
}
