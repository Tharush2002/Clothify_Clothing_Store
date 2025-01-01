package repository.custom;

import entity.ReturnOrderEntity;
import exceptions.RepositoryException;
import org.hibernate.Session;
import repository.SuperRepository;

import java.util.List;

public interface ReturnOrderRepository extends SuperRepository {
    void save(ReturnOrderEntity returnOrderEntity) throws RepositoryException;
    void save(ReturnOrderEntity returnOrderEntity, Session session);

    List<ReturnOrderEntity> findAll() throws RepositoryException;
}
