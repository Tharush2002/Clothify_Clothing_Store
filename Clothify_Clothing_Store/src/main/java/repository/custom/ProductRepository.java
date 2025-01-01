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
    void update(Session session, ProductEntity productEntity);

    List<ProductEntity> findAll() throws RepositoryException;

    ProductEntity findByID(String productId) throws RepositoryException;
    ProductEntity findByID(String productId, Session session);

    void deleteByID(String productId) throws RepositoryException;

    List<ProductEntity> findBySupplierID(String supplierId) throws RepositoryException;
}
