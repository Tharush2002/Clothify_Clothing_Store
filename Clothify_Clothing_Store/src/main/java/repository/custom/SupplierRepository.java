package repository.custom;

import entity.SupplierEntity;
import exceptions.RepositoryException;
import model.Supplier;
import org.hibernate.Session;
import repository.SuperRepository;

import java.util.List;

public interface SupplierRepository extends SuperRepository {
    List<SupplierEntity> findAll() throws RepositoryException;

    SupplierEntity findByName(String name) throws RepositoryException;

    void update(SupplierEntity supplierEntity) throws RepositoryException;
    void update(Session session, SupplierEntity supplierEntity);

    void save(SupplierEntity supplierEntity) throws RepositoryException;
    void save(Session session, SupplierEntity newSupplier);

    SupplierEntity findByName(Session session, String name);

    SupplierEntity findBySupplierID(Session session, String supplierId);

    void deleteByID(Session session, String supplierId);

    SupplierEntity findBySupplierID(String supplierId) throws RepositoryException;
}
