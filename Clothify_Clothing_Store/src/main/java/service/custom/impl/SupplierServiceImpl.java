package service.custom.impl;

import entity.ProductEntity;
import entity.SupplierEntity;
import exceptions.NoSupplierMatchFoundException;
import exceptions.RepositoryException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Supplier;
import org.hibernate.Session;
import org.hibernate.Transaction;
import repository.RepositoryFactory;
import repository.custom.ProductRepository;
import repository.custom.SupplierRepository;
import service.custom.SupplierService;
import util.Type;

import java.util.List;

import static repository.SuperRepository.sessionFactory;

public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository supplierRepository = RepositoryFactory.getInstance().getRepositoryType(Type.SUPPLIER);
    private final ProductRepository productRepository = RepositoryFactory.getInstance().getRepositoryType(Type.PRODUCT);

    @Override
    public ObservableList<Supplier> getAll() throws RepositoryException {
        List<SupplierEntity> supplierEntityList = supplierRepository.findAll();
        ObservableList<Supplier> supplierObservableList= FXCollections.observableArrayList();
        if (supplierEntityList!=null){
            supplierEntityList.forEach(entity->{
                supplierObservableList.add(new Supplier(entity.getSupplierId(),entity.getName(), entity.getCompany(), entity.getEmail()));
            });
        }
        return supplierObservableList;
    }

    @Override
    public Supplier findSupplierByName(String name) throws RepositoryException {
        SupplierEntity supplierEntity = supplierRepository.findByName(name);
        if(supplierEntity !=null){
            return new Supplier(supplierEntity.getSupplierId(), supplierEntity.getName(), supplierEntity.getCompany(), supplierEntity.getEmail());
        }
        return new Supplier();
    }

    @Override
    public Supplier findBySupplierId(String supplierId) throws RepositoryException {
        SupplierEntity supplierEntity = supplierRepository.findBySupplierID(supplierId);
        return new Supplier(
                supplierEntity.getSupplierId(),
                supplierEntity.getName(),
                supplierEntity.getCompany(),
                supplierEntity.getEmail()
        );
    }

    @Override
    public boolean isSupplierNameAlreadyAvailable(String name) throws RepositoryException {
        return supplierRepository.findByName(name) != null;
    }

    @Override
    public void deleteSupplier(String supplierId) throws RepositoryException {
        Transaction transaction = null;
        Session session = sessionFactory.openSession();
        try {
            transaction = session.beginTransaction();

            List<ProductEntity> productsListBySupplierID = productRepository.findBySupplierID(supplierId);

            productsListBySupplierID.forEach(productEntity -> {
                productEntity.setSupplierEntity(null);
                session.update(productEntity);
                session.flush();
            });

            supplierRepository.deleteByID(session, supplierId);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback(); // Rollback on failure
            throw new RepositoryException("Failed to delete the supplier entity record.");
        } finally {
            session.close();
        }
    }

    @Override
    public void updateSupplier(Supplier supplier) throws RepositoryException {
        Transaction transaction = null;
        Session session = sessionFactory.openSession();
        try {
            transaction = session.beginTransaction();

            SupplierEntity supplierEntity = supplierRepository.findBySupplierID(session, supplier.getSupplierId());
            if (supplierEntity == null) {
                throw new NoSupplierMatchFoundException("Supplier with ID " + supplier.getSupplierId() + " not found.");
            }

            supplierEntity.setName(supplier.getName());
            supplierEntity.setCompany(supplier.getCompany());
            supplierEntity.setEmail(supplier.getEmail());

            supplierRepository.update(session, supplierEntity);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RepositoryException("Failed to update the specific supplier entity records");
        } finally {
            session.close();
        }
    }

    @Override
    public void addSupplier(Supplier supplier) throws RepositoryException {
        Transaction transaction = null;
        Session session = sessionFactory.openSession();
        try {
            transaction = session.beginTransaction();

            SupplierEntity existingSupplier = supplierRepository.findByName(session, supplier.getName());
            if (existingSupplier == null) {

                SupplierEntity newSupplier = new SupplierEntity(
                        null,
                        null,
                        supplier.getName(),
                        supplier.getCompany(),
                        supplier.getEmail()
                );

                supplierRepository.save(session, newSupplier);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback(); // Rollback on failure
            throw new RepositoryException("Failed to add the specific supplier entity record.");
        } finally {
            session.close();
        }
    }

    @Override
    public boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailPattern);
    }

}
