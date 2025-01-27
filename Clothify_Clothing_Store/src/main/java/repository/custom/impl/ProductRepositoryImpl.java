package repository.custom.impl;

import entity.CategoryEntity;
import entity.ProductEntity;
import entity.SupplierEntity;
import exceptions.RepositoryException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import repository.RepositoryFactory;
import repository.custom.CategoryRepository;
import repository.custom.ProductRepository;
import repository.custom.SupplierRepository;
import util.Type;

import java.util.List;

public class ProductRepositoryImpl implements ProductRepository {
    private final CategoryRepository categoryRepository = RepositoryFactory.getInstance().getRepositoryType(Type.CATEGORY);
    private final SupplierRepository supplierRepository = RepositoryFactory.getInstance().getRepositoryType(Type.SUPPLIER);

    @Override
    public void save(ProductEntity entity) throws RepositoryException {
        Transaction transaction = null;
        Session session = sessionFactory.openSession();
        try {
            transaction = session.beginTransaction();

            if (entity.getCategoryEntity().getCategoryId() != null) {
                CategoryEntity existingCategory = categoryRepository.findByCategoryID(session, entity.getCategoryEntity().getCategoryId());

                if (existingCategory != null) {
                    entity.setCategoryEntity(existingCategory);
                } else {
                    throw new RepositoryException("No matching category entity found.");
                }
            }else{
                session.save(entity.getCategoryEntity());
                session.refresh(entity.getCategoryEntity());
            }
            if (entity.getSupplierEntity().getSupplierId() != null) {
                SupplierEntity existingSupplier = supplierRepository.findBySupplierID(session, entity.getSupplierEntity().getSupplierId());

                if (existingSupplier != null) {
                    entity.setSupplierEntity(existingSupplier);
                } else {
                    throw new RepositoryException("No matching category entity found.");
                }
            }else{
                session.save(entity.getSupplierEntity());
                session.refresh(entity.getSupplierEntity());
            }

            session.save(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RepositoryException("Failed to save the specific product entity record.");
        } finally {
            session.close();
        }
    }

    @Override
    public void update(ProductEntity entity) throws RepositoryException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            ProductEntity existingProduct = session
                    .createQuery("FROM ProductEntity WHERE productId = :productId", ProductEntity.class)
                    .setParameter("productId", entity.getProductId())
                    .uniqueResult();

            if (existingProduct != null) {
                existingProduct.setName(entity.getName());
                existingProduct.setQuantity(entity.getQuantity());
                existingProduct.setUnitPrice(entity.getUnitPrice());

                if (entity.getCategoryEntity()!=null){
                    CategoryEntity existingCategory = categoryRepository.findByName(session, entity.getCategoryEntity().getName());

                    if (existingCategory != null) {
                        existingProduct.setCategoryEntity(existingCategory);
                    }
                }

                if(entity.getSupplierEntity()!=null){
                    SupplierEntity existingSupplier = supplierRepository.findBySupplierID(session, entity.getSupplierEntity().getSupplierId());

                    if (existingSupplier != null) {
                        existingProduct.setSupplierEntity(existingSupplier);
                    }
                }else{
                    existingProduct.setSupplierEntity(null);
                }

                session.update(existingProduct);
                transaction.commit();
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RepositoryException("Failed to update the specific product entity record.");
        } finally {
            session.close();
        }
    }

    @Override
    public List<ProductEntity> findAll() throws RepositoryException {
        Transaction transaction = null;
        List<ProductEntity> productEntityList = null;
        Session session = sessionFactory.openSession();
        try{
            transaction = session.beginTransaction();
            productEntityList = session.createQuery("from ProductEntity", ProductEntity.class).list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RepositoryException("Failed to fetch the product entity records.");
        }finally{
            session.close();
        }
        return productEntityList;
    }

    @Override
    public ProductEntity findByProductID(String productId, Session session) {
        ProductEntity entity = session.createQuery(
                "FROM ProductEntity WHERE productId = :productId",
                ProductEntity.class
        ).setParameter("productId", productId).uniqueResult();

        if (entity == null) {
            throw new RuntimeException("Inventory not found for productId: " + productId);
        }

        return entity;
    }

    @Override
    public void save(ProductEntity entity, Session session){
        if (entity.getCategoryEntity() != null) {
            CategoryEntity existingCategory = categoryRepository.findByName(session, entity.getCategoryEntity().getName());

            if (existingCategory != null) {
                entity.setCategoryEntity(existingCategory);
            } else {
                session.save(entity.getCategoryEntity());
                session.flush();
            }
        }
        if (entity.getSupplierEntity() != null) {
            SupplierEntity existingSupplier = supplierRepository.findByName(session, entity.getSupplierEntity().getName());

            if (existingSupplier != null) {
                entity.setSupplierEntity(existingSupplier);
            } else {
                session.save(entity.getSupplierEntity());
                session.flush();
            }
        }

        session.save(entity);
    }

    @Override
    public void deleteByID(String productId) throws RepositoryException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.createQuery("DELETE FROM ProductEntity WHERE productId = :productId")
                    .setParameter("productId", productId)
                    .executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RepositoryException("Failed to delete the specific product entity record.");
        } finally {
            session.close();
        }
    }

    @Override
    public List<ProductEntity> findBySupplierID(String supplierId) throws RepositoryException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        List<ProductEntity> productEntityList = null;
        try {
            transaction = session.beginTransaction();
            productEntityList = session
                    .createQuery("FROM ProductEntity p WHERE p.supplierEntity.supplierId = :supplierId", ProductEntity.class)
                    .setParameter("supplierId", supplierId)
                    .getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RepositoryException("Failed to find the specific product entity record.");
        } finally {
            session.close();
        }
        return productEntityList;
    }

    @Override
    public List<ProductEntity> findByCategoryId(String categoryId) throws RepositoryException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        List<ProductEntity> productEntityList = null;
        try {
            transaction = session.beginTransaction();
            productEntityList = session
                    .createQuery("FROM ProductEntity p WHERE p.categoryEntity.categoryId = :categoryId", ProductEntity.class)
                    .setParameter("categoryId", categoryId)
                    .getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RepositoryException("Failed to find the specific product entity record.");
        } finally {
            session.close();
        }
        return productEntityList;
    }
}
