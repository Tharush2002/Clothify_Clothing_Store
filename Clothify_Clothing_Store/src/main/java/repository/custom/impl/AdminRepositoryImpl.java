package repository.custom.impl;

import entity.AdminEntity;
import exceptions.RepositoryException;
import jakarta.persistence.OptimisticLockException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import repository.custom.AdminRepository;

import java.util.List;

public class AdminRepositoryImpl implements AdminRepository {
    @Override
    public AdminEntity findByEmail(String email) throws RepositoryException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        AdminEntity adminEntity;

        try {
            transaction = session.beginTransaction();
            adminEntity = session
                    .createQuery("FROM AdminEntity WHERE email = :email", AdminEntity.class)
                    .setParameter("email", email)
                    .uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RepositoryException("Failed to find the specific admin entity record.");
        } finally {
            session.close();
        }
        return adminEntity;
    }

    @Override
    public AdminEntity findByUserName(String userName) throws RepositoryException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        AdminEntity adminEntity = null;

        try {
            transaction = session.beginTransaction();
            adminEntity = session
                    .createQuery("FROM AdminEntity WHERE userName = :userName", AdminEntity.class)
                    .setParameter("userName", userName)
                    .uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RepositoryException("Failed to find the specific admin entity record.");
        } finally {
            session.close();
        }
        return adminEntity;
    }

    @Override
    public void update(AdminEntity adminEntity) throws RepositoryException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(adminEntity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RepositoryException("Failed to update teh specific admin entity record.");
        } finally {
            session.close();
        }
    }

    @Override
    public boolean saveOrUpdate(AdminEntity adminEntity) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            if (adminEntity.getId() != null) {
                AdminEntity existingEntity = session.get(AdminEntity.class, adminEntity.getId());
                if (existingEntity != null && adminEntity.getAdminId() == null) {
                    adminEntity.setAdminId(existingEntity.getAdminId());
                }
                session.merge(adminEntity);
            } else {
                session.persist(adminEntity);
            }
            transaction.commit();
            return true;
        } catch (OptimisticLockException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public List<AdminEntity> findAll() throws RepositoryException {
        Transaction transaction = null;
        List<AdminEntity> adminEntityList = null;
        Session session = sessionFactory.openSession();
        try{
            transaction = session.beginTransaction();
            adminEntityList = session.createQuery("from AdminEntity", AdminEntity.class).list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RepositoryException("Failed to fetch admin entity records.");
        }finally{
            session.close();
        }
        return adminEntityList;
    }

    @Override
    public void deleteById(String adminId) throws RepositoryException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session
                    .createQuery("DELETE FROM AdminEntity WHERE adminId = :adminId")
                    .setParameter("adminId", adminId)
                    .executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RepositoryException("Failed to delete the specific admin entity record.");
        } finally {
            session.close();
        }
    }

    @Override
    public void save(AdminEntity adminEntity) throws RepositoryException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(adminEntity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RepositoryException("Failed to save admin entity records.");
        } finally {
            session.close();
        }
    }

    @Override
    public AdminEntity findByEmail(Session session, String email) {
        return session
                .createQuery("FROM AdminEntity WHERE email = :email", AdminEntity.class)
                .setParameter("email", email)
                .uniqueResult();
    }

    @Override
    public void update(Session session, AdminEntity adminEntity) {
        session.update(adminEntity);
    }
}
