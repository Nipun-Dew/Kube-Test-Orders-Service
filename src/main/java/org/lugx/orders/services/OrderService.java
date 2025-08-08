package org.lugx.orders.services;

import org.lugx.orders.dtos.OrderData;
import org.lugx.orders.entities.OrderEB;
import org.lugx.orders.repos.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepo orderRepository;

    @Autowired
    public OrderService(OrderRepo orderRepository) {
        this.orderRepository = orderRepository;
    }

    public ResponseEntity<List<OrderData>> getOrders(String cartId) {
        List<OrderEB> results = orderRepository.findAll(OrderSpecification.filterBy(cartId));

        List<OrderData> games = results.stream().map(result -> {
            OrderData gameData = new OrderData();
            gameData.mapToOrderData(result);
            return gameData;
        }).toList();

        return ResponseEntity.ok(games);
    }

    public ResponseEntity<OrderEB> saveOrUpdateOrder(OrderData gameData) {
        OrderEB results = orderRepository.save(gameData.mapToOrderEB());
        return ResponseEntity.ok(results);
    }
}
