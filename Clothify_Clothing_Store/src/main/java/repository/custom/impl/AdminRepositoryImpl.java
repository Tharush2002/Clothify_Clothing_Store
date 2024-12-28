package repository.custom.impl;

import entity.AdminEntity;
import jakarta.persistence.OptimisticLockException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import repository.custom.AdminRepository;

public class AdminRepositoryImpl implements AdminRepository {
    @Override
    public AdminEntity findByEmail(String email) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        AdminEntity adminEntity = null;

        try {
            transaction = session.beginTransaction();
            String hql = "FROM AdminEntity WHERE email = :email";
            Query<AdminEntity> query = session.createQuery(hql, AdminEntity.class);
            query.setParameter("email", email);
            adminEntity = query.uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return adminEntity;
    }

    @Override
    public AdminEntity findByUserName(String userName) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        AdminEntity adminEntity = null;

        try {
            transaction = session.beginTransaction();
            String hql = "FROM AdminEntity WHERE userName = :userName";
            Query<AdminEntity> query = session.createQuery(hql, AdminEntity.class);
            query.setParameter("userName", userName);
            adminEntity = query.uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return adminEntity;
    }

    @Override
    public boolean update(AdminEntity adminEntity) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(adminEntity);
            transaction.commit();
            return true;
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

}
