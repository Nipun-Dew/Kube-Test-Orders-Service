package org.lugx.orders.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lugx.orders.dtos.OrderData;
import org.lugx.orders.entities.OrderEB;
import org.lugx.orders.services.OrderService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = "api.prefix=orders")
class OrdersControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrdersController ordersController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(ordersController)
                .addPlaceholderValue("api.prefix", "orders")
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetOrders_WithoutCartId_ShouldReturnAllOrders() throws Exception {
        // Arrange
        OrderData order1 = createOrderData(1L, "Product1,Product2", "cart123", "29.99");
        OrderData order2 = createOrderData(2L, "Product3", "cart456", "15.50");
        List<OrderData> orders = Arrays.asList(order1, order2);

        ResponseEntity<List<OrderData>> responseEntity = ResponseEntity.ok(orders);
        when(orderService.getOrders(null)).thenReturn(responseEntity);

        // Act & Assert
        mockMvc.perform(get("/orders")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].products").value("Product1,Product2"))
                .andExpect(jsonPath("$[0].cartId").value("cart123"))
                .andExpect(jsonPath("$[0].totalPrice").value("29.99"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].products").value("Product3"))
                .andExpect(jsonPath("$[1].cartId").value("cart456"))
                .andExpect(jsonPath("$[1].totalPrice").value("15.50"));
    }

    @Test
    void testGetOrders_WithCartId_ShouldReturnFilteredOrders() throws Exception {
        // Arrange
        String cartId = "cart123";
        OrderData order = createOrderData(1L, "Product1,Product2", cartId, "29.99");
        List<OrderData> orders = List.of(order);

        ResponseEntity<List<OrderData>> responseEntity = ResponseEntity.ok(orders);
        when(orderService.getOrders(eq(cartId))).thenReturn(responseEntity);

        // Act & Assert
        mockMvc.perform(get("/orders")
                .param("cartId", cartId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].products").value("Product1,Product2"))
                .andExpect(jsonPath("$[0].cartId").value(cartId))
                .andExpect(jsonPath("$[0].totalPrice").value("29.99"));
    }

    @Test
    void testGetOrders_EmptyResult_ShouldReturnEmptyArray() throws Exception {
        // Arrange
        ResponseEntity<List<OrderData>> responseEntity = ResponseEntity.ok(List.of());
        when(orderService.getOrders(null)).thenReturn(responseEntity);

        // Act & Assert
        mockMvc.perform(get("/orders")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testSaveOrder_ValidOrderData_ShouldReturnSavedOrder() throws Exception {
        // Arrange
        OrderData orderData = createOrderData(null, "Product1,Product2", "cart123", "29.99");
        OrderEB savedOrder = createOrderEB(1L, "Product1,Product2", "cart123", "29.99");

        ResponseEntity<OrderEB> responseEntity = ResponseEntity.ok(savedOrder);
        when(orderService.saveOrUpdateOrder(any(OrderData.class))).thenReturn(responseEntity);

        // Act & Assert
        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderData)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.products").value("Product1,Product2"))
                .andExpect(jsonPath("$.cartId").value("cart123"))
                .andExpect(jsonPath("$.totalPrice").value("29.99"));
    }

    @Test
    void testSaveOrder_UpdateExistingOrder_ShouldReturnUpdatedOrder() throws Exception {
        // Arrange
        OrderData orderData = createOrderData(1L, "Product1,Product2,Product3", "cart123", "45.99");
        OrderEB updatedOrder = createOrderEB(1L, "Product1,Product2,Product3", "cart123", "45.99");

        ResponseEntity<OrderEB> responseEntity = ResponseEntity.ok(updatedOrder);
        when(orderService.saveOrUpdateOrder(any(OrderData.class))).thenReturn(responseEntity);

        // Act & Assert
        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderData)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.products").value("Product1,Product2,Product3"))
                .andExpect(jsonPath("$.cartId").value("cart123"))
                .andExpect(jsonPath("$.totalPrice").value("45.99"));
    }

    @Test
    void testSaveOrder_MinimalOrderData_ShouldReturnSavedOrder() throws Exception {
        // Arrange
        OrderData orderData = createOrderData(null, "Product1", "cart456", "10.00");
        OrderEB savedOrder = createOrderEB(2L, "Product1", "cart456", "10.00");

        ResponseEntity<OrderEB> responseEntity = ResponseEntity.ok(savedOrder);
        when(orderService.saveOrUpdateOrder(any(OrderData.class))).thenReturn(responseEntity);

        // Act & Assert
        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderData)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.products").value("Product1"))
                .andExpect(jsonPath("$.cartId").value("cart456"))
                .andExpect(jsonPath("$.totalPrice").value("10.00"));
    }

    @Test
    void testSaveOrder_ServiceReturnsCreated_ShouldReturnCreatedStatus() throws Exception {
        // Arrange
        OrderData orderData = createOrderData(null, "Product1", "cart789", "25.50");
        OrderEB savedOrder = createOrderEB(3L, "Product1", "cart789", "25.50");

        ResponseEntity<OrderEB> responseEntity = new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
        when(orderService.saveOrUpdateOrder(any(OrderData.class))).thenReturn(responseEntity);

        // Act & Assert
        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderData)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.products").value("Product1"))
                .andExpect(jsonPath("$.cartId").value("cart789"))
                .andExpect(jsonPath("$.totalPrice").value("25.50"));
    }

    // Helper methods for creating test data
    private OrderData createOrderData(Long id, String products, String cartId, String totalPrice) {
        OrderData orderData = new OrderData();
        orderData.setId(id);
        orderData.setProducts(products);
        orderData.setCartId(cartId);
        orderData.setTotalPrice(totalPrice);
        return orderData;
    }

    private OrderEB createOrderEB(Long id, String products, String cartId, String totalPrice) {
        OrderEB orderEB = new OrderEB();
        orderEB.setId(id);
        orderEB.setProducts(products);
        orderEB.setCartId(cartId);
        orderEB.setTotalPrice(totalPrice);
        return orderEB;
    }
}
