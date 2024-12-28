package model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderItemWithQuantity {
    private OrderItem orderItem;
    private Integer quantity;
}
