package model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReturnOrder {
    private String returnOrderId;
    private Order order;
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
