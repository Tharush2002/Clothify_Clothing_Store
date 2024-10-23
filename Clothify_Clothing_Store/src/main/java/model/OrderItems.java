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
    private Integer quantity;
    private Double cost;
    private String size;
}
