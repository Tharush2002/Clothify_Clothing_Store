package repository.custom.impl;

import entity.CategoryEntity;
import entity.ProductEntity;
import entity.SupplierEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import repository.custom.SupplierRepository;

import java.util.List;

public class SupplierRepositoryImpl implements SupplierRepository {
    @Override
    public List<SupplierEntity> findAll() {
        Transaction transaction = null;
        List<SupplierEntity> supplierEntityList = null;
        Session session = sessionFactory.openSession();
        try{
            transaction = session.beginTransaction();
            supplierEntityList = session.createQuery("from SupplierEntity", SupplierEntity.class).list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }finally{
            session.close();
        }
        return supplierEntityList;
    }

    @Override
    public SupplierEntity findBySupplierID(String supplierId) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        SupplierEntity supplierEntity = null;

        try {
            transaction = session.beginTransaction();
            String hql = "FROM SupplierEntity WHERE supplierId = :supplierId";
            Query<SupplierEntity> query = session.createQuery(hql, SupplierEntity.class);
            query.setParameter("supplierId", supplierId);
            supplierEntity = query.uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return supplierEntity;
    }

    @Override
    public SupplierEntity findByName(String name) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        SupplierEntity supplierEntity = null;
        try {
            transaction = session.beginTransaction();
            String hql = "FROM SupplierEntity WHERE name = :name";
            Query<SupplierEntity> query = session.createQuery(hql, SupplierEntity.class);
            query.setParameter("name", name);
            supplierEntity = query.uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return supplierEntity;
    }
}
