package service.custom;

import javafx.collections.ObservableList;
import model.Product;
import service.SuperService;

import java.util.List;

public interface ProductService extends SuperService {
    boolean addProduct(Product product);
    ObservableList<Product> getAllProducts();
    boolean updateProduct(Product product);
    void deleteProduct(String id);
    Product searchProduct(String id);
}
