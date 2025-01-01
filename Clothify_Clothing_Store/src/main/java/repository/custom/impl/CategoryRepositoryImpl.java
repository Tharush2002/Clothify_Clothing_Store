package repository.custom.impl;

import entity.CategoryEntity;
import exceptions.RepositoryException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import repository.custom.CategoryRepository;

import java.util.List;

public class CategoryRepositoryImpl implements CategoryRepository {
    @Override
    public List<CategoryEntity> findAll() throws RepositoryException {
        Transaction transaction = null;
        List<CategoryEntity> categoryEntityList = null;
        Session session = sessionFactory.openSession();
        try{
            transaction = session.beginTransaction();
            categoryEntityList = session.createQuery("from CategoryEntity", CategoryEntity.class).list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RepositoryException("Failed to fetch category entity records.");
        }finally{
            session.close();
        }
        return categoryEntityList;
    }

    @Override
    public CategoryEntity findByName(String categoryName) throws RepositoryException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        CategoryEntity categoryEntity = null;

        try {
            transaction = session.beginTransaction();
            categoryEntity = session
                    .createQuery("FROM CategoryEntity WHERE name = :categoryName", CategoryEntity.class)
                    .setParameter("categoryName", categoryName)
                    .uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RepositoryException("Failed to find the specific category entity record.");
        } finally {
            session.close();
        }
        return categoryEntity;
    }

    @Override
    public CategoryEntity findByName(Session session, String name) {
        return session.createQuery("FROM CategoryEntity WHERE name = :name", CategoryEntity.class)
                .setParameter("name", name)
                .uniqueResult();
    }

    @Override
    public CategoryEntity findByCategoryID(Session session, String categoryId) {
        return session
                .createQuery("FROM CategoryEntity WHERE categoryId = :categoryId", CategoryEntity.class)
                .setParameter("categoryId", categoryId)
                .uniqueResult();
    }
}
