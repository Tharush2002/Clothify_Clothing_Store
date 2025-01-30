package repository.custom.impl;

import entity.AdminEntity;
import entity.CategoryEntity;
import exceptions.RepositoryException;
import jakarta.persistence.OptimisticLockException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import repository.custom.AdminRepository;

import java.util.List;

@Slf4j
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
        AdminEntity adminEntity;

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
            AdminEntity existingAdmin = session
                    .createQuery("FROM AdminEntity WHERE adminId = :adminId", AdminEntity.class)
                    .setParameter("adminId", adminEntity.getAdminId())
                    .uniqueResult();

            existingAdmin.setFirstName(adminEntity.getFirstName());
            existingAdmin.setLastName(adminEntity.getLastName());
            existingAdmin.setEmail(adminEntity.getEmail());
            existingAdmin.setPhoneNumber(adminEntity.getPhoneNumber());
            existingAdmin.setUserName(adminEntity.getUserName());

            session.update(existingAdmin);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RepositoryException("Failed to update the specific admin entity record.");
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
