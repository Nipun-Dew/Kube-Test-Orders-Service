package org.lugx.orders.dtos;

import org.lugx.orders.entities.OrderEB;

public class OrderData {
    private Long id;
    private String products;
    private String cartId;
    private String totalPrice;

    public void mapToOrderData(OrderEB orderEB) {
        id = orderEB.getId();
        products = orderEB.getProducts();
        cartId = orderEB.getCartId();
        totalPrice = orderEB.getTotalPrice();
    }

    public OrderEB mapToOrderEB() {
        OrderEB orderEB = new OrderEB();
        // If update request, ID will be present
        if (id != null) {
            orderEB.setId(id);
        }
        orderEB.setProducts(products);
        orderEB.setCartId(cartId);
        orderEB.setTotalPrice(totalPrice);

        return orderEB;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
