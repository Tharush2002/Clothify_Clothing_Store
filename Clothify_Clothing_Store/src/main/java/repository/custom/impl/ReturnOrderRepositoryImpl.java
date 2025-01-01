package repository.custom.impl;

import entity.OrderEntity;
import entity.ReturnOrderEntity;
import exceptions.RepositoryException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import repository.RepositoryFactory;
import repository.custom.OrderRepository;
import repository.custom.ReturnOrderRepository;
import util.Type;

import java.util.List;

public class ReturnOrderRepositoryImpl implements ReturnOrderRepository {
    private final OrderRepository orderRepository = RepositoryFactory.getInstance().getRepositoryType(Type.ORDER);

    @Override
    public void save(ReturnOrderEntity returnOrderEntity) throws RepositoryException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            OrderEntity orderEntity = orderRepository.findByOrderId(session, returnOrderEntity.getOrderEntity().getOrderId());

            returnOrderEntity.setOrderEntity(orderEntity);

            session.save(returnOrderEntity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RepositoryException("Failed to save the specific return order entity record.");
        } finally {
            session.close();
        }
    }

    @Override
    public void save(ReturnOrderEntity returnOrderEntity, Session session) {
        OrderEntity orderEntity = session.createQuery(
                "FROM OrderEntity WHERE orderId = :orderId",
                OrderEntity.class
        ).setParameter("orderId", returnOrderEntity.getOrderEntity().getOrderId()).uniqueResult();

        if (orderEntity == null) {
            throw new RuntimeException("OrderEntity not found for orderId: " + returnOrderEntity.getOrderEntity().getOrderId());
        }

        returnOrderEntity.setOrderEntity(orderEntity);
        session.save(returnOrderEntity);
    }

    @Override
    public List<ReturnOrderEntity> findAll() throws RepositoryException {
        Transaction transaction = null;
        List<ReturnOrderEntity> returnOrderEntityList = null;
        Session session = sessionFactory.openSession();
        try{
            transaction = session.beginTransaction();
            returnOrderEntityList = session.createQuery("from ReturnOrderEntity", ReturnOrderEntity.class).list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RepositoryException("Failed to fetch the return order entity records.");
        }finally{
            session.close();
        }
        return returnOrderEntityList;
    }

}
