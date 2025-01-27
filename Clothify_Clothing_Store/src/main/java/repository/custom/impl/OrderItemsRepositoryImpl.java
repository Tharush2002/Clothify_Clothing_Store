package repository.custom.impl;

import entity.CustomerEntity;
import entity.OrderEntity;
import entity.OrderItemEntity;
import exceptions.RepositoryException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import repository.RepositoryFactory;
import repository.custom.CustomerRepository;
import repository.custom.OrderItemsRepository;
import repository.custom.OrderRepository;
import util.Type;

import java.util.List;

public class OrderItemsRepositoryImpl implements OrderItemsRepository {
    private final CustomerRepository customerRepository = RepositoryFactory.getInstance().getRepositoryType(Type.CUSTOMER);
    private final OrderRepository orderRepository = RepositoryFactory.getInstance().getRepositoryType(Type.ORDER);

    @Override
    public List<OrderItemEntity> findAll() throws RepositoryException {
        Transaction transaction = null;
        List<OrderItemEntity> orderItemEntityList = null;
        Session session = sessionFactory.openSession();
        try{
            transaction = session.beginTransaction();
            orderItemEntityList = session.createQuery("from OrderItemEntity", OrderItemEntity.class).list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RepositoryException("Failed to fetch the order item entity records.");
        }finally{
            session.close();
        }
        return orderItemEntityList;
    }

    @Override
    public List<OrderItemEntity> findByOrderID(String orderId) throws RepositoryException {
        List<OrderItemEntity> orderItems = null;
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            orderItems = session.createQuery("SELECT oi FROM OrderItemEntity oi " +
                    "JOIN oi.orderEntity o " +
                    "WHERE o.orderId = :orderId", OrderItemEntity.class)
                    .setParameter("orderId", orderId)
                    .getResultList();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RepositoryException("Failed to find the specific order item entity record.");
        }
        return orderItems;
    }

    @Override
    public void deleteBySizeId(String orderId, String productId, String size, Session session) {
        OrderItemEntity orderItemToDelete = session.createQuery(
                "FROM OrderItemEntity WHERE orderEntity.orderId = :orderId AND productId = :productId AND size = :size",
                OrderItemEntity.class)
                .setParameter("orderId", orderId)
                .setParameter("productId", productId)
                .setParameter("size", size)
                .setMaxResults(1)
                .uniqueResult();

        if (orderItemToDelete != null) {
            orderItemToDelete.setOrderEntity(null);
            session.delete(orderItemToDelete);
        } else {
            throw new RuntimeException("Order item not found for deletion with the provided details.");
        }
    }

    @Override
    public void save(OrderItemEntity orderItemEntity, Session session) {

        OrderEntity orderEntity = orderRepository.findByDateTime(
                session,
                orderItemEntity.getOrderEntity().getDate(),
                orderItemEntity.getOrderEntity().getTime()
        );

        if (orderEntity == null) {
            orderEntity = new OrderEntity();

            CustomerEntity customerEntity = new CustomerEntity();

            if(orderItemEntity.getOrderEntity().getCustomerEntity().getCustomerId()==null) orderItemEntity.getOrderEntity().getCustomerEntity().setCustomerId("");

            if(!orderItemEntity.getOrderEntity().getCustomerEntity().getCustomerId().isEmpty()){
                customerEntity = customerRepository.findByCustomerId(
                        session,
                        orderItemEntity.getOrderEntity().getCustomerEntity().getCustomerId()
                );
            }else{
                customerEntity.setName(orderItemEntity.getOrderEntity().getCustomerEntity().getName());
                customerEntity.setEmail(orderItemEntity.getOrderEntity().getCustomerEntity().getEmail());
                customerEntity.setPhoneNumber(orderItemEntity.getOrderEntity().getCustomerEntity().getPhoneNumber());
                session.save(customerEntity);
                session.flush();
                session.refresh(customerEntity);
            }

            orderEntity.setCustomerEntity(customerEntity);

            orderEntity.setDate(orderItemEntity.getOrderEntity().getDate());
            orderEntity.setTime(orderItemEntity.getOrderEntity().getTime());
            orderEntity.setTotal(orderItemEntity.getOrderEntity().getTotal());
            orderEntity.setPaymentType(orderItemEntity.getOrderEntity().getPaymentType());
            orderEntity.setOrderItemCount(orderItemEntity.getOrderEntity().getOrderItemCount());
            session.save(orderEntity);
            session.flush();
            session.refresh(orderEntity);
        }

        orderItemEntity.setOrderEntity(orderEntity);
        session.save(orderItemEntity);
        session.flush();
    }
}
