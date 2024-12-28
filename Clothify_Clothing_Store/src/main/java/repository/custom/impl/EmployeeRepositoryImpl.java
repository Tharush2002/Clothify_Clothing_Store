package repository.custom.impl;

import entity.AdminEntity;
import entity.EmployeeEntity;
import jakarta.persistence.OptimisticLockException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import repository.custom.EmployeeRepository;

public class EmployeeRepositoryImpl implements EmployeeRepository {
    @Override
    public EmployeeEntity findByEmail(String email) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        EmployeeEntity employeeEntity = null;

        try {
            transaction = session.beginTransaction();
            String hql = "FROM EmployeeEntity WHERE email = :email";
            Query<EmployeeEntity> query = session.createQuery(hql, EmployeeEntity.class);
            query.setParameter("email", email);
            employeeEntity = query.uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return employeeEntity;
    }

    @Override
    public EmployeeEntity findByUserName(String userName) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        EmployeeEntity employeeEntity = null;

        try {
            transaction = session.beginTransaction();
            String hql = "FROM EmployeeEntity WHERE userName = :userName";
            Query<EmployeeEntity> query = session.createQuery(hql, EmployeeEntity.class);
            query.setParameter("userName", userName);
            employeeEntity = query.uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return employeeEntity;
    }

    @Override
    public boolean saveOrUpdate(EmployeeEntity employeeEntity) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            if (employeeEntity.getId() != null) {
                EmployeeEntity existingEntity = session.get(EmployeeEntity.class, employeeEntity.getId());
                if (existingEntity != null && employeeEntity.getEmployeeId() == null) {
                    employeeEntity.setEmployeeId(existingEntity.getEmployeeId());
                }
                session.merge(employeeEntity);
            } else {
                session.persist(employeeEntity);
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
    public boolean update(EmployeeEntity employeeEntity) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(employeeEntity);
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
}
