package org.lugx.orders.controllers;

import org.lugx.orders.dtos.OrderData;
import org.lugx.orders.entities.OrderEB;
import org.lugx.orders.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "${api.prefix}")
public class OrdersController {
    private final OrderService orderService;

    @Autowired
    public OrdersController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderData>> getGames(@RequestParam(required = false) String cartId) {
        return orderService.getOrders(cartId);
    }

    @PostMapping
    public ResponseEntity<OrderEB> saveGame(@RequestBody OrderData orderData) {
        return orderService.saveOrUpdateOrder(orderData);
    }
}
