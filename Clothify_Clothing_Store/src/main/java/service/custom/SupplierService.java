package service.custom;

import exceptions.RepositoryException;
import javafx.collections.ObservableList;
import model.Supplier;
import service.SuperService;

public interface SupplierService extends SuperService {
    ObservableList<Supplier> getAll() throws RepositoryException;

    Supplier findSupplierByName(String value) throws RepositoryException;

    void deleteSupplier(String supplierId) throws RepositoryException;

    void updateSupplier(Supplier supplier) throws RepositoryException;

    void addSupplier(Supplier supplier) throws RepositoryException;

    boolean isValidEmail(String email);

    Supplier findBySupplierId(String supplierId) throws RepositoryException;

    boolean isSupplierNameAlreadyAvailable(String name) throws RepositoryException;
}
