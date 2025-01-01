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

    ObservableList<Product> findProductsBySupplierID(String supplierId) throws RepositoryException;

    Product findProductByProductID(String productId) throws RepositoryException;
}
