package repository.custom.impl;

import entity.CategoryEntity;
import entity.ProductEntity;
import entity.SupplierEntity;
import exceptions.RepositoryException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import repository.custom.SupplierRepository;
import util.HibernateUtil;

import java.util.List;

public class SupplierRepositoryImpl implements SupplierRepository {
    @Override
    public List<SupplierEntity> findAll() throws RepositoryException {
        Transaction transaction = null;
        List<SupplierEntity> supplierEntityList = null;
        Session session = sessionFactory.openSession();
        try{
            transaction = session.beginTransaction();
            supplierEntityList = session.createQuery("from SupplierEntity", SupplierEntity.class).list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RepositoryException("Failed to fetch the supplier entity records.");
        }finally{
            session.close();
        }
        return supplierEntityList;
    }

    @Override
    public SupplierEntity findByName(String name) throws RepositoryException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        SupplierEntity supplierEntity = null;
        try {
            transaction = session.beginTransaction();
            supplierEntity = session
                    .createQuery("FROM SupplierEntity WHERE name = :name", SupplierEntity.class)
                    .setParameter("name", name)
                    .uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RepositoryException("Failed to find the specific supplier entity record.");
        } finally {
            session.close();
        }
        return supplierEntity;
    }

    @Override
    public void update(SupplierEntity supplierEntity) throws RepositoryException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            session
                    .createQuery("UPDATE SupplierEntity s SET s.name = :name, s.company = :company, s.email = :email WHERE s.supplierId = :supplierId")
                    .setParameter("name", supplierEntity.getName())
                    .setParameter("company", supplierEntity.getCompany())
                    .setParameter("email", supplierEntity.getEmail())
                    .setParameter("supplierId", supplierEntity.getSupplierId())
                    .executeUpdate();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RepositoryException("Failed to update the specific supplier entity record.");
        } finally {
            session.close();
        }
    }

    @Override
    public void save(SupplierEntity supplierEntity) throws RepositoryException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(supplierEntity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RepositoryException("Failed to save the specific supplier entity record.");
        } finally {
            session.close();
        }
    }

    @Override
    public SupplierEntity findByName(Session session, String name) {
        return session.createQuery("FROM SupplierEntity WHERE name = :name", SupplierEntity.class)
                .setParameter("name", name)
                .uniqueResult();
    }

    @Override
    public SupplierEntity findBySupplierID(Session session, String supplierId) {
        return session.createQuery("FROM SupplierEntity WHERE supplierId = :supplierId", SupplierEntity.class)
                .setParameter("supplierId", supplierId)
                .uniqueResult();
    }

    @Override
    public void deleteByID(Session session, String supplierId) {
        session
                .createQuery("DELETE FROM SupplierEntity WHERE supplierId = :supplierId")
                .setParameter("supplierId", supplierId)
                .executeUpdate();
    }

    @Override
    public void update(Session session, SupplierEntity supplierEntity) {
        session
                .createQuery("UPDATE SupplierEntity s SET s.name = :name, s.company = :company, s.email = :email WHERE s.supplierId = :supplierId")
                .setParameter("name", supplierEntity.getName())
                .setParameter("company", supplierEntity.getCompany())
                .setParameter("email", supplierEntity.getEmail())
                .setParameter("supplierId", supplierEntity.getSupplierId())
                .executeUpdate();
    }

    @Override
    public void save(Session session, SupplierEntity newSupplier) {
        session.save(newSupplier);
    }
}
