package org.lugx.orders.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "t_orders")
public class OrderEB {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orders_seq_gen")
    @SequenceGenerator(name = "orders_seq_gen", sequenceName = "orders_seq", allocationSize = 1)
    private Long id;

    @Column(name = "products", length = 255)
    private String products;

    @Column(name = "cart_id", length = 255)
    private String cartId;

    @Column(name = "total_price", length = 255)
    private String totalPrice;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
