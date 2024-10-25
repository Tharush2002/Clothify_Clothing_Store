package model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderItems {
    private Order order;
    private Product product;
    private String name;
    private Double unitPrice;
    private String size;
}
