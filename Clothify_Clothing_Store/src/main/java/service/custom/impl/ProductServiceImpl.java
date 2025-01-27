package service.custom.impl;

import entity.CategoryEntity;
import entity.ProductEntity;
import entity.SupplierEntity;
import exceptions.NoCategoryMatchFoundException;
import exceptions.NoProductMatchFoundException;
import exceptions.NoSupplierMatchFoundException;
import exceptions.RepositoryException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Category;
import model.Product;
import model.Supplier;
import org.hibernate.Session;
import org.hibernate.Transaction;
import repository.RepositoryFactory;
import repository.custom.CategoryRepository;
import repository.custom.ProductRepository;
import repository.custom.SupplierRepository;
import service.custom.ProductService;
import util.Type;

import java.util.List;

import static repository.SuperRepository.sessionFactory;

public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository = RepositoryFactory.getInstance().getRepositoryType(Type.PRODUCT);
    private final CategoryRepository categoryRepository = RepositoryFactory.getInstance().getRepositoryType(Type.CATEGORY);
    private final SupplierRepository supplierRepository = RepositoryFactory.getInstance().getRepositoryType(Type.SUPPLIER);

    @Override
    public void addProduct(Product product) throws RepositoryException {

        CategoryEntity categoryEntity = new CategoryEntity();

        categoryEntity.setName(product.getCategory().getName());
        categoryEntity.setCategoryId(product.getCategory().getCategoryId());

        SupplierEntity supplierEntity = new SupplierEntity();

        supplierEntity.setName(product.getSupplier().getName());
        supplierEntity.setEmail(product.getSupplier().getEmail());
        supplierEntity.setCompany(product.getSupplier().getCompany());
        supplierEntity.setSupplierId(product.getSupplier().getSupplierId());

        ProductEntity entity = new ProductEntity(null,null,product.getName(),categoryEntity, product.getQuantity(), product.getUnitPrice(), supplierEntity);
        productRepository.save(entity);
    }

    @Override
    public ObservableList<Product> getAllProducts() throws RepositoryException {
        List<ProductEntity> productEntityList = productRepository.findAll();
        ObservableList<Product> productObservableList= FXCollections.observableArrayList();
        if (productEntityList!=null){
            productEntityList.forEach(entity->{
                Product product = new Product(entity.getProductId(), entity.getName(), new Category(),entity.getQuantity(), entity.getUnitPrice(), new Supplier());
                if (entity.getCategoryEntity()!=null) product.setCategory(new Category(entity.getCategoryEntity().getCategoryId(),entity.getCategoryEntity().getName()));
                if (entity.getSupplierEntity()!=null) product.setSupplier(new Supplier(entity.getSupplierEntity().getSupplierId(),entity.getSupplierEntity().getName(),entity.getSupplierEntity().getCompany(),entity.getSupplierEntity().getEmail()));
                productObservableList.add(product);
            });
        }
        return productObservableList;
    }

    @Override
    public void update(Product product) throws RepositoryException {
        Transaction transaction = null;
        Session session = sessionFactory.openSession();
        try {
            transaction = session.beginTransaction();

            ProductEntity productEntity = productRepository.findByProductID(product.getProductId(), session);
            if (productEntity == null) {
                throw new NoProductMatchFoundException("Product with ID " + product.getProductId() + " not found.");
            }

            productEntity.setName(product.getName());
            productEntity.setQuantity(product.getQuantity());
            productEntity.setUnitPrice(product.getUnitPrice());

            if(product.getCategory().getCategoryId()!=null){
                CategoryEntity categoryEntity = categoryRepository.findByCategoryID(session, product.getCategory().getCategoryId());
                if (categoryEntity == null) {
                    throw new NoCategoryMatchFoundException("Category with ID " + product.getCategory().getCategoryId() + " not found.");
                }
                productEntity.setCategoryEntity(categoryEntity);
                session.flush();
            }

            if (product.getSupplier().getSupplierId() != null) {
                SupplierEntity supplierEntity = supplierRepository.findBySupplierID(session, product.getSupplier().getSupplierId());
                if (supplierEntity == null) {
                    throw new NoSupplierMatchFoundException("Supplier with ID " + product.getSupplier().getSupplierId() + " not found.");
                }
                productEntity.setSupplierEntity(supplierEntity);
                session.flush();
            }

            session.update(productEntity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RepositoryException("Failed to update the specific product entity record.");
        } finally {
            session.close();
        }
    }

    @Override
    public void deleteProduct(String productId) throws RepositoryException {
        productRepository.deleteByID(productId);
    }

    @Override
    public ObservableList<Product> findBySupplierID(String supplierId) throws RepositoryException {
        List<ProductEntity> suppliedProducts = productRepository.findBySupplierID(supplierId);
        ObservableList<Product> productObservableList= FXCollections.observableArrayList();
        if (suppliedProducts!=null){
            suppliedProducts.forEach(entity->{
                Product product = new Product(entity.getProductId(), entity.getName(), new Category(),entity.getQuantity(), entity.getUnitPrice(), new Supplier());
                if (entity.getCategoryEntity()!=null) product.setCategory(new Category(entity.getCategoryEntity().getCategoryId(),entity.getCategoryEntity().getName()));
                if (entity.getSupplierEntity()!=null) product.setSupplier(new Supplier(entity.getSupplierEntity().getSupplierId(),entity.getSupplierEntity().getName(),entity.getSupplierEntity().getCompany(),entity.getSupplierEntity().getEmail()));
                productObservableList.add(product);
            });
        }
        return productObservableList;
    }

    @Override
    public ObservableList<Product> findByCategoryId(String categoryId) throws RepositoryException {
        List<ProductEntity> categorizedProducts = productRepository.findByCategoryId(categoryId);
        ObservableList<Product> productObservableList= FXCollections.observableArrayList();
        if (categorizedProducts!=null){
            categorizedProducts.forEach(entity->{
                Product product = new Product(entity.getProductId(), entity.getName(), new Category(),entity.getQuantity(), entity.getUnitPrice(), new Supplier());
                if (entity.getCategoryEntity()!=null) product.setCategory(new Category(entity.getCategoryEntity().getCategoryId(),entity.getCategoryEntity().getName()));
                if (entity.getSupplierEntity()!=null) product.setSupplier(new Supplier(entity.getSupplierEntity().getSupplierId(),entity.getSupplierEntity().getName(),entity.getSupplierEntity().getCompany(),entity.getSupplierEntity().getEmail()));
                productObservableList.add(product);
            });
        }
        return productObservableList;
    }

}
