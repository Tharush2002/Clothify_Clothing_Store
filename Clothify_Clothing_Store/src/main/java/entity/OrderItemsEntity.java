package entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "`orderItems`")
public class OrderItemsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "order_id", referencedColumnName = "orderID")
    private OrderEntity orderEntity;

    @OneToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "product_id", referencedColumnName = "productID")
    private ProductEntity productEntity;

    private Double unitPrice;
    private String size;
}
