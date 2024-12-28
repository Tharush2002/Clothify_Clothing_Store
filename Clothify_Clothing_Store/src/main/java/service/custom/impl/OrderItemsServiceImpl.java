package service.custom.impl;

import entity.CustomerEntity;
import entity.OrderEntity;
import entity.OrderItemEntity;
import entity.ProductEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import model.Order;
import model.OrderItem;
import model.Product;
import org.hibernate.Session;
import org.hibernate.Transaction;
import repository.RepositoryFactory;
import repository.custom.OrderItemsRepository;
import repository.custom.ProductRepository;
import service.custom.OrderItemsService;
import util.Type;

import java.util.List;

import static repository.SuperRepository.sessionFactory;

public class OrderItemsServiceImpl implements OrderItemsService {
    private final OrderItemsRepository orderItemsRepository= RepositoryFactory.getInstance().getRepositoryType(Type.ORDERITEMS);
    private final ProductRepository productRepository= RepositoryFactory.getInstance().getRepositoryType(Type.PRODUCT);

    @Override
    public ObservableList<OrderItem> findOrderItemsByOrderID(String orderId) {
        ObservableList<OrderItem> orderItemObservableList = FXCollections.observableArrayList();
        List<OrderItemEntity> orderItemEntityList = orderItemsRepository.findByOrderID(orderId);

        orderItemEntityList.forEach(orderItemEntity -> {
            Order order = getOrder(orderItemEntity);

            OrderItem orderItem = new OrderItem(
                    order,
                    orderItemEntity.getProductName(),
                    orderItemEntity.getProductId(),
                    orderItemEntity.getCategoryId(),
                    orderItemEntity.getCategoryName(),
                    orderItemEntity.getSupplierId(),
                    orderItemEntity.getSupplierName(),
                    orderItemEntity.getUnitPrice(),
                    orderItemEntity.getSize()
            );
            orderItemObservableList.add(orderItem);
        });
        return orderItemObservableList;
    }

    private Order getOrder(OrderItemEntity orderItemEntity) {
        OrderEntity orderEntity = orderItemEntity.getOrderEntity();
        CustomerEntity customerEntity = orderEntity.getCustomerEntity();

        Customer customer = new Customer(customerEntity.getCustomerId(), customerEntity.getName(), customerEntity.getEmail(), customerEntity.getPhoneNumber());
        return new Order(
                orderEntity.getOrderId(),
                orderEntity.getDate(),
                orderEntity.getTime(),
                orderEntity.getTotal(),
                orderEntity.getPaymentType(),
                customer,
                orderEntity.getOrderItemCount());
    }

    @Override
    public boolean saveOrder(OrderItem orderItem, Integer quantity) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            // Step 1: Save Orders
            OrderItemEntity orderItemEntity = new OrderItemEntity();

            orderItemEntity.setOrderEntity(new OrderEntity(null,null, orderItem.getOrder().getDate(), orderItem.getOrder().getTime(), orderItem.getOrder().getTotal(), orderItem.getOrder().getPaymentType(), orderItem.getOrder().getOrderItemCount(), new CustomerEntity(null,null,orderItem.getOrder().getCustomer().getName(),orderItem.getOrder().getCustomer().getEmail(),orderItem.getOrder().getCustomer().getPhoneNumber())));
            orderItemEntity.setProductName(orderItem.getProductName());
            orderItemEntity.setProductId(orderItem.getProductId());
            orderItemEntity.setCategoryId(orderItem.getCategoryId());
            orderItemEntity.setCategoryName(orderItem.getCategoryName());
            orderItemEntity.setSupplierId(orderItem.getSupplierId());
            orderItemEntity.setSupplierName(orderItem.getSupplierName());
            orderItemEntity.setSize(orderItem.getSize());
            orderItemEntity.setUnitPrice(orderItem.getUnitPrice());

            for(int i=0 ; i<quantity ; i++){
                orderItemsRepository.save(orderItemEntity);
            }

            // Step 2: Update Products
            ProductEntity productEntity = productRepository.findByID(orderItemEntity.getProductId(), session);

            // Update the product quantity
            productEntity.setQuantity(productEntity.getQuantity() - quantity);
            session.saveOrUpdate(productEntity);

            // Commit the transaction
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback(); // Rollback on failure
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }
}
