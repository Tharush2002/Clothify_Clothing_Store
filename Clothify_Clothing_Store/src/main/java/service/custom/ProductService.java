package service.custom;

import exceptions.RepositoryException;
import javafx.collections.ObservableList;
import model.Product;
import service.SuperService;

public interface ProductService extends SuperService {
    void addProduct(Product product) throws RepositoryException;
    ObservableList<Product> getAllProducts() throws RepositoryException;
    void update(Product product) throws RepositoryException;
    void deleteProduct(String id) throws RepositoryException;

    ObservableList<Product> findBySupplierID(String supplierId) throws RepositoryException;

    ObservableList<Product> findByCategoryId(String categoryId) throws RepositoryException;
}
