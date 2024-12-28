package repository.custom.impl;

import entity.OrderEntity;
import entity.ProductEntity;
import entity.ReturnOrderEntity;
import model.ReturnOrder;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import repository.custom.ReturnOrderRepository;

import java.util.List;

public class ReturnOrderRepositoryImpl implements ReturnOrderRepository {

    @Override
    public boolean save(ReturnOrderEntity returnOrderEntity) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            Query<OrderEntity> orderEntityQuery = session.createQuery("FROM OrderEntity WHERE orderId = :orderId", OrderEntity.class);
            orderEntityQuery.setParameter("orderId", returnOrderEntity.getOrderEntity().getOrderId());
            OrderEntity orderEntity = orderEntityQuery.uniqueResult();

            returnOrderEntity.setOrderEntity(orderEntity);

            session.save(returnOrderEntity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
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
    public List<ReturnOrderEntity> findAll(){
        Transaction transaction = null;
        List<ReturnOrderEntity> returnOrderEntityList = null;
        Session session = sessionFactory.openSession();
        try{
            transaction = session.beginTransaction();
            returnOrderEntityList = session.createQuery("from ReturnOrderEntity", ReturnOrderEntity.class).list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }finally{
            session.close();
        }
        return returnOrderEntityList;
    }

}
