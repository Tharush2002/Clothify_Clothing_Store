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

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "orderID")
    private OrderEntity orderEntity;

    @OneToMany
    @JoinColumn(name = "product_id", referencedColumnName = "productID")
    private ProductEntity productEntity;

    private Integer quantity;
    private Double cost;
    private String size;
}
