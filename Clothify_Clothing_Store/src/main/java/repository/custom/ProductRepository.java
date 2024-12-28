package repository.custom;

import entity.ProductEntity;
import org.hibernate.Session;
import repository.SuperRepository;

import java.util.List;

public interface ProductRepository extends SuperRepository {
    boolean save(ProductEntity entity);
    void update(ProductEntity entity);
    List<ProductEntity> findAll();

    ProductEntity findByID(String productId);
    ProductEntity findByID(ProductEntity productEntity, Session session);
    ProductEntity findByID(String productId, Session session);

    void save(ProductEntity entity, Session session);

    void deleteByID(String productId);

    List<ProductEntity> findBySupplierID(String supplierId);
}
