package entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "`returnOrder`")
public class ReturnOrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String returnOrderId;

    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "order_id", referencedColumnName = "orderID")
    private OrderEntity orderEntity;

    private String productName;
    private String productId;
    private String categoryId;
    private String categoryName;
    private String supplierId;
    private String supplierName;

    private Double unitPrice;
    private String size;

    private LocalDate returnDate;

}
