package model;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Order {
    private String orderId;
    private LocalDate date;
    private LocalTime time;
    private Double total;
    private String paymentType;
//    private Product[] productsPurchased;
    private Customer customer;
}
