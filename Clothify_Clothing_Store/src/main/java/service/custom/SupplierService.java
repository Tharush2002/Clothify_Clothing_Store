package service.custom;

import exceptions.RepositoryException;
import javafx.collections.ObservableList;
import model.Product;
import model.Supplier;
import service.SuperService;

public interface SupplierService extends SuperService {
    ObservableList<Supplier> getAllSuppliers() throws RepositoryException;

    Supplier findSupplierByName(String value) throws RepositoryException;

    void deleteSupplier(String supplierId) throws RepositoryException;

    void updateSupplier(Supplier supplier) throws RepositoryException;

    void addSupplier(Supplier supplier) throws RepositoryException;

    boolean isValidEmail(String email);
}
