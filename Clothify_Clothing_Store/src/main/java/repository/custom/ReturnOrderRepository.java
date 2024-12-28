package repository.custom;

import entity.ReturnOrderEntity;
import model.ReturnOrder;
import org.hibernate.Session;
import repository.SuperRepository;

import java.util.List;

public interface ReturnOrderRepository extends SuperRepository {
    boolean save(ReturnOrderEntity returnOrderEntity);

    void save(ReturnOrderEntity returnOrderEntity, Session session);

    List<ReturnOrderEntity> findAll();
}
