package repository.custom;

import entity.OrderItemEntity;
import exceptions.RepositoryException;
import org.hibernate.Session;
import repository.SuperRepository;

import java.util.List;

public interface OrderItemsRepository extends SuperRepository {
    List<OrderItemEntity> findAll() throws RepositoryException;

    List<OrderItemEntity> findByOrderID(String orderId) throws RepositoryException;

//    void save(OrderItemEntity orderItemEntity) throws RepositoryException;

    void deleteBySizeId(String orderId, String productId, String size, Session session);

    void save(OrderItemEntity orderItemEntity, Session session);
}
