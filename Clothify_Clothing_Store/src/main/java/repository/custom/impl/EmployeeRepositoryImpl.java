package repository.custom.impl;

import entity.EmployeeEntity;
import exceptions.RepositoryException;
import jakarta.persistence.OptimisticLockException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import repository.custom.EmployeeRepository;

import java.util.List;

public class EmployeeRepositoryImpl implements EmployeeRepository {
    @Override
    public EmployeeEntity findByEmail(String email) throws RepositoryException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        EmployeeEntity employeeEntity = null;

        try {
            transaction = session.beginTransaction();
            employeeEntity = session
                    .createQuery("FROM EmployeeEntity WHERE email = :email", EmployeeEntity.class)
                    .setParameter("email", email)
                    .uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RepositoryException("Failed to find the specific employee entity record.");
        } finally {
            session.close();
        }
        return employeeEntity;
    }

    @Override
    public EmployeeEntity findByUserName(String userName) throws RepositoryException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        EmployeeEntity employeeEntity = null;

        try {
            transaction = session.beginTransaction();
            employeeEntity = session
                    .createQuery("FROM EmployeeEntity WHERE userName = :userName", EmployeeEntity.class)
                    .setParameter("userName", userName)
                    .uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RepositoryException("Failed to find the specific employee entity record.");
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
    public void update(EmployeeEntity employeeEntity) throws RepositoryException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(employeeEntity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RepositoryException("Failed to update the specific employee entity record.");
        } finally {
            session.close();
        }
    }

    @Override
    public List<EmployeeEntity> findAll() throws RepositoryException {
        Transaction transaction = null;
        List<EmployeeEntity> employeeEntityList = null;
        Session session = sessionFactory.openSession();
        try{
            transaction = session.beginTransaction();
            employeeEntityList = session.createQuery("from EmployeeEntity", EmployeeEntity.class).list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RepositoryException("Failed to fetch employee entity records.");
        }finally{
            session.close();
        }
        return employeeEntityList;
    }

    @Override
    public void deleteById(String employeeId) throws RepositoryException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session
                    .createQuery("DELETE FROM EmployeeEntity WHERE employeeId = :employeeId")
                    .setParameter("employeeId", employeeId)
                    .executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RepositoryException("Failed to delete the specific employee entity record.");
        } finally {
            session.close();
        }
    }

    @Override
    public void save(EmployeeEntity employeeEntity) throws RepositoryException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(employeeEntity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RepositoryException("Failed to save the specific employee entity record.");
        } finally {
            session.close();
        }
    }

    @Override
    public EmployeeEntity findByEmail(String email, Session session) {
        return session
                .createQuery("FROM EmployeeEntity WHERE email = :email", EmployeeEntity.class)
                .setParameter("email", email)
                .uniqueResult();
    }

    @Override
    public void update(Session session, EmployeeEntity employeeEntity) {
        session.update(employeeEntity);
    }
}
