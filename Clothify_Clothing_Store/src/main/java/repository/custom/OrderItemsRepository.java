package repository.custom;

import entity.OrderItemEntity;
import org.hibernate.Session;
import repository.SuperRepository;

import java.util.List;

public interface OrderItemsRepository extends SuperRepository {
    List<OrderItemEntity> findAll();

    List<OrderItemEntity> findByOrderID(String orderId);

    void save(OrderItemEntity orderItemEntity);

    void deleteBySizeAndId(String orderId, String productId, String size, Session session);
}
