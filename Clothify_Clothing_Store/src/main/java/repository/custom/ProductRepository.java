package repository.custom;

import entity.ProductEntity;
import exceptions.RepositoryException;
import org.hibernate.Session;
import repository.SuperRepository;

import java.util.List;

public interface ProductRepository extends SuperRepository {
    void save(ProductEntity entity) throws RepositoryException;
    void save(ProductEntity entity, Session session);

    void update(ProductEntity entity) throws RepositoryException;

    List<ProductEntity> findAll() throws RepositoryException;

    ProductEntity findByProductID(String productId, Session session);

    void deleteByID(String productId) throws RepositoryException;

    List<ProductEntity> findBySupplierID(String supplierId) throws RepositoryException;

    List<ProductEntity> findByCategoryId(String categoryId) throws RepositoryException;
}
