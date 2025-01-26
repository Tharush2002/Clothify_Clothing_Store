package repository.custom.impl;

import entity.CategoryEntity;
import entity.ProductEntity;
import exceptions.RepositoryException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import repository.RepositoryFactory;
import repository.custom.CategoryRepository;
import repository.custom.ProductRepository;
import util.Type;

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

    @Override
    public CategoryEntity findByCategoryID(String categoryId) throws RepositoryException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        CategoryEntity categoryEntity = null;

        try {
            transaction = session.beginTransaction();
            categoryEntity = session
                    .createQuery("FROM CategoryEntity WHERE categoryId = :categoryId", CategoryEntity.class)
                    .setParameter("categoryId", categoryId)
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
    public void update(CategoryEntity categoryEntity) throws RepositoryException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            CategoryEntity existingEntity = findByCategoryID(session,categoryEntity.getCategoryId());
            existingEntity.setName(categoryEntity.getName());
            session.update(existingEntity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RepositoryException("Failed to find the specific category entity record.");
        } finally {
            session.close();
        }
    }

    @Override
    public void deleteByCategoryId(String categoryId) throws RepositoryException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            List<ProductEntity> productEntityList = session.createQuery("FROM ProductEntity WHERE categoryEntity.categoryId = :categoryId", ProductEntity.class).setParameter("categoryId", categoryId).list();
            productEntityList.forEach(productEntity -> {
                productEntity.setCategoryEntity(null);
                session.update(productEntity);
                session.flush();
            });
            session
                    .createQuery("DELETE FROM CategoryEntity WHERE categoryId = :categoryId")
                    .setParameter("categoryId", categoryId)
                    .executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RepositoryException("Failed to find the specific supplier entity record.");
        } finally {
            session.close();
        }
    }

    @Override
    public CategoryEntity findByName(String name) throws RepositoryException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        CategoryEntity categoryEntity = null;

        try {
            transaction = session.beginTransaction();
            categoryEntity = session
                    .createQuery("FROM CategoryEntity WHERE name = :name", CategoryEntity.class)
                    .setParameter("name", name)
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
    public void add(CategoryEntity categoryEntity) throws RepositoryException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(categoryEntity);
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
}
