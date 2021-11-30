package com.es.core.service.order;

import com.es.core.dao.order.OrderDao;
import com.es.core.exception.OutOfStockException;
import com.es.core.exception.StockNotFoundException;
import com.es.core.model.cart.Cart;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.model.order.OrderStatus;
import com.es.core.model.stock.Stock;
import com.es.core.service.stock.StockService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrderServiceImpl implements OrderService {

    private Integer deliveryPrice;
    private StockService stockService;
    private OrderDao orderDao;

    public OrderServiceImpl(Integer deliveryPrice, StockService stockService, OrderDao orderDao) {
        this.deliveryPrice = deliveryPrice;
        this.stockService = stockService;
        this.orderDao = orderDao;
    }

    @Override
    public Order createOrder(Cart cart) {
        Order order = new Order();

        order.setSubtotal(cart.getTotalCost());
        order.setDeliveryPrice(BigDecimal.valueOf(deliveryPrice));
        order.setTotalPrice(cart.getTotalCost().add(BigDecimal.valueOf(deliveryPrice)));

        cart.getItems().forEach(cartItem -> {
            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(order);
            orderItem.setPhone(cartItem.getPhone());
            orderItem.setQuantity(cartItem.getQuantity());

            order.getOrderItems().add(orderItem);
        });
        return order;
    }

    @Override
    public void placeOrder(Order order) throws OutOfStockException {
        checkOrderItemsStock(order);

        order.getOrderItems().forEach(orderItem -> {
            Optional<Stock> stockOptional = stockService.getStock(orderItem.getPhone().getId());
            Stock stock = stockOptional.orElseThrow(StockNotFoundException::new);
            stock.setStock((int) (stock.getStock() - orderItem.getQuantity()));
            stock.setReserved((int) (stock.getReserved() + orderItem.getQuantity()));
            stockService.save(stock);
        });

        order.setStatus(OrderStatus.NEW);
        order.setSecureId(UUID.randomUUID().toString());
        orderDao.save(order);
    }

    @Override
    public Optional<Order> getOrderBySecureId(String secureId) {
        return orderDao.getBySecureId(secureId);
    }

    private void checkOrderItemsStock(Order order) {
        List<OrderItem> orderItems = order.getOrderItems();
        List<OrderItem> outOfStockItems = new ArrayList<>();

        orderItems.forEach(orderItem -> {
            Optional<Stock> stockOptional = stockService.getStock(orderItem.getPhone().getId());
            Integer stockQuantity = stockOptional.map(Stock::getStock).orElse(0);
            if (stockQuantity < orderItem.getQuantity()) {
                outOfStockItems.add(orderItem);
            }
        });

        if (!outOfStockItems.isEmpty()) {
            String outOfStockPhoneIds = outOfStockItems.stream()
                    .map(orderItem -> orderItem.getPhone().getModel())
                    .collect(Collectors.joining(", "));
            throw new OutOfStockException(outOfStockPhoneIds + "out of stock and was deleted");
        }
    }
}
