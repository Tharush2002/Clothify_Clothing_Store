package service.custom.impl;

import entity.*;
import exceptions.RepositoryException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import model.Order;
import model.ReturnOrder;
import org.hibernate.Session;
import org.hibernate.Transaction;
import repository.RepositoryFactory;
import repository.custom.OrderItemsRepository;
import repository.custom.ProductRepository;
import repository.custom.ReturnOrderRepository;
import service.custom.ReturnOrderService;
import util.Type;

import java.util.List;

import static repository.SuperRepository.sessionFactory;

public class ReturnOrderServiceImpl implements ReturnOrderService {
    private final ReturnOrderRepository returnOrderRepository = RepositoryFactory.getInstance().getRepositoryType(Type.RETURNORDER);
    private final ProductRepository productRepository = RepositoryFactory.getInstance().getRepositoryType(Type.PRODUCT);
    private final OrderItemsRepository orderItemsRepository = RepositoryFactory.getInstance().getRepositoryType(Type.ORDERITEMS);

    @Override
    public void save(ReturnOrder returnOrder) throws RepositoryException {

        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            ReturnOrderEntity returnOrderEntity = new ReturnOrderEntity(
                    null,
                    null,
                    new OrderEntity(
                            null,
                            returnOrder.getOrder().getOrderId(),
                            returnOrder.getOrder().getDate(),
                            returnOrder.getOrder().getTime(),
                            returnOrder.getOrder().getTotal(),
                            returnOrder.getOrder().getPaymentType(),
                            returnOrder.getOrder().getOrderItemCount(),
                            new CustomerEntity(
                                    null,
                                    null,
                                    returnOrder.getOrder().getCustomer().getName(),
                                    returnOrder.getOrder().getCustomer().getEmail(),
                                    returnOrder.getOrder().getCustomer().getPhoneNumber()
                            )
                    ),
                    returnOrder.getProductName(),
                    returnOrder.getProductId(),
                    returnOrder.getCategoryId(),
                    returnOrder.getCategoryName(),
                    returnOrder.getSupplierId(),
                    returnOrder.getSupplierName(),
                    returnOrder.getUnitPrice(),
                    returnOrder.getSize(),
                    returnOrder.getReturnDate()
            );

            returnOrderRepository.save(returnOrderEntity, session);
            ProductEntity productEntity = productRepository.findByProductID(returnOrder.getProductId(), session);

            productEntity.setQuantity(productEntity.getQuantity() + 1);
            productRepository.update(session, productEntity);

            orderItemsRepository.deleteBySizeId(returnOrder.getOrder().getOrderId(),returnOrder.getProductId(),returnOrder.getSize(),session);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RepositoryException("Failed to save the specific return order entity record.");
        } finally {
            session.close();
        }
    }

    @Override
    public ObservableList<ReturnOrder> getAllReturnedItems() throws RepositoryException {
        List<ReturnOrderEntity> returnOrderEntityList = returnOrderRepository.findAll();
        ObservableList<ReturnOrder> returnOrderObservableList = FXCollections.observableArrayList();
        if (returnOrderEntityList!=null){
            returnOrderEntityList.forEach(entity-> returnOrderObservableList.add(
                    new ReturnOrder(
                            entity.getReturnOrderId(),
                            new Order(
                                    entity.getOrderEntity().getOrderId(),
                                    entity.getOrderEntity().getDate(),
                                    entity.getOrderEntity().getTime(),
                                    entity.getOrderEntity().getTotal(),
                                    entity.getOrderEntity().getPaymentType(),
                                    new Customer(
                                            entity.getOrderEntity().getCustomerEntity().getCustomerId(),
                                            entity.getOrderEntity().getCustomerEntity().getName(),
                                            entity.getOrderEntity().getCustomerEntity().getEmail(),
                                            entity.getOrderEntity().getCustomerEntity().getPhoneNumber()
                                    ),
                                    entity.getOrderEntity().getOrderItemCount()
                            ),
                            entity.getProductName(),
                            entity.getProductId(),
                            entity.getCategoryId(),
                            entity.getCategoryName(),
                            entity.getSupplierId(),
                            entity.getSupplierName(),
                            entity.getUnitPrice(),
                            entity.getSize(),
                            entity.getReturnDate()
                    )
            ));
        }
        return returnOrderObservableList;
    }
}
