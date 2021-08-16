package com.example.mywebshop.entity;

import com.example.mywebshop.service.impl.OrderStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringExclude;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "order_entity")
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime timestamp = LocalDateTime.now();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<OrderProduct> products;

    private BigDecimal totalPrice;

    private OrderStatus status = OrderStatus.ACTIVE;

    public Order(User user, LocalDateTime timestamp, BigDecimal totalPrice) {
        this.user = user;
        this.timestamp = timestamp;
        this.totalPrice = totalPrice;
    }
}
