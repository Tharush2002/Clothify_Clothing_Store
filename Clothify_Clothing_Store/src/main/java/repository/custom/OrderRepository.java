package repository.custom;

import entity.OrderEntity;
import exceptions.RepositoryException;
import org.hibernate.Session;
import repository.SuperRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface OrderRepository extends SuperRepository {
    List<OrderEntity> findAll() throws RepositoryException;

    OrderEntity findByDateTime(Session session, LocalDate date, LocalTime time);

    OrderEntity findByOrderId(Session session, String orderId);
}
