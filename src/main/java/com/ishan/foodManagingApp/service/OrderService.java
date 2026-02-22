package com.ishan.foodManagingApp.service;

import com.ishan.foodManagingApp.DTO.*;
import com.ishan.foodManagingApp.exception.*;
import com.ishan.foodManagingApp.model.*;
import com.ishan.foodManagingApp.repository.FoodRepo;
import com.ishan.foodManagingApp.repository.OrderRepo;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepo orderRepo;
    private final FoodRepo foodrepo;

    public OrderService(OrderRepo orderRepo, FoodRepo foodRepo) {
        this.orderRepo = orderRepo;
        this.foodrepo = foodRepo;
    }

    @Transactional
    public OrderDetailResponse createOrder(OrderCreateRequest request) {

        Order order = new Order();

        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PENDING);

        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal totalTax = BigDecimal.ZERO;
        BigDecimal totalCgst = BigDecimal.ZERO;
        BigDecimal totalSgst = BigDecimal.ZERO;

        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemRequest itemRequest : request.getItems()) {
            Food food = foodrepo.findById(itemRequest.getFoodId())
                    .orElseThrow(() -> new FoodItemNotFoundException(itemRequest.getFoodId()));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setFood(food);
            orderItem.setQuantity(itemRequest.getQuantity());

            BigDecimal price = food.getPrice();
            orderItem.setPriceAtTimeOfOrder(price);

            BigDecimal subtotal = price.multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            orderItem.setSubtotal(subtotal);

            totalAmount = totalAmount.add(subtotal);

            TaxGroup taxGroup = food.getTaxGroup();
            BigDecimal cgst = BigDecimal.ZERO;
            BigDecimal sgst = BigDecimal.ZERO;

            if (taxGroup != null && taxGroup.getTaxes() != null) {
                for (Tax tax : taxGroup.getTaxes()) {
                    BigDecimal taxAmount = subtotal.multiply(
                            tax.getTaxRate().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
                    );
                    if (tax.getTaxType() == TaxType.CGST) cgst = cgst.add(taxAmount);
                    else if (tax.getTaxType() == TaxType.SGST) sgst = sgst.add(taxAmount);
                }
            }

            BigDecimal totalItemTax = cgst.add(sgst);
            BigDecimal totalAmountAfterTax = subtotal.add(totalItemTax);

            orderItem.setCgstAmount(cgst);
            orderItem.setSgstAmount(sgst);
            orderItem.setTotalTaxAmount(totalItemTax);
            orderItem.setTotalAmountAfterTax(totalAmountAfterTax);

            totalCgst = totalCgst.add(cgst);
            totalSgst = totalSgst.add(sgst);
            totalTax = totalTax.add(totalItemTax);

            orderItems.add(orderItem);
        }

        order.setTotalAmount(totalAmount);
        order.setTotalTax(totalTax);
        order.setFinalAmount(totalAmount.add(totalTax));
        order.setOrderItems(orderItems);

        Order savedOrder = orderRepo.save(order);

        OrderDetailResponse response = new OrderDetailResponse();
        response.setOrderId(savedOrder.getOrderId());
        response.setOrderDate(savedOrder.getOrderDate());
        response.setStatus(savedOrder.getOrderStatus().name());
        response.setTotalAmount(savedOrder.getTotalAmount());
        response.setTotalTax(savedOrder.getTotalTax());
        response.setFinalAmount(savedOrder.getFinalAmount());


        List<OrderItemResponse> itemResponses = new ArrayList<>();

        for (OrderItem item : order.getOrderItems()) {
            OrderItemResponse itemResponse = new OrderItemResponse();
            itemResponse.setFoodName(item.getFood().getFoodName());
            itemResponse.setQuantity(item.getQuantity());
            itemResponse.setPriceAtTimeOfOrder(item.getPriceAtTimeOfOrder());
            itemResponse.setCgstAmount(item.getCgstAmount());
            itemResponse.setSgstAmount(item.getSgstAmount());
            itemResponse.setTotalAmount(item.getTotalTaxAmount());
            itemResponse.setTotalTaxAmount(item.getTotalTaxAmount());
            itemResponse.setTotalAmountAfterTax(item.getTotalAmountAfterTax());
            itemResponse.setSubtotal(item.getSubtotal());

            itemResponses.add(itemResponse);
        }
        response.setItems(itemResponses);
        response.setTotalCgst(totalCgst);
        response.setTotalSgst(totalSgst);
        return response;
    }

    public OrderDetailResponse getOrderById(Integer orderId) {
        Order order = orderRepo.findOrderWithItems(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        OrderDetailResponse response = new OrderDetailResponse();
        response.setOrderId(order.getOrderId());
        response.setOrderDate(order.getOrderDate());
        response.setStatus(order.getOrderStatus().name());
        response.setTotalAmount(order.getTotalAmount());

        List<OrderItemResponse> items = new ArrayList<>();
        BigDecimal totalTax = BigDecimal.ZERO;
        BigDecimal totalCgst = BigDecimal.ZERO;
        BigDecimal totalSgst = BigDecimal.ZERO;

        for (OrderItem item : order.getOrderItems()) {
            OrderItemResponse itemResponse = new OrderItemResponse();
            itemResponse.setFoodName(item.getFood().getFoodName());
            itemResponse.setQuantity(item.getQuantity());
            itemResponse.setPriceAtTimeOfOrder(item.getPriceAtTimeOfOrder());
            itemResponse.setSubtotal(item.getSubtotal());
            itemResponse.setCgstAmount(item.getCgstAmount());
            itemResponse.setSgstAmount(item.getSgstAmount());
            itemResponse.setTotalAmount(item.getTotalTaxAmount());
            itemResponse.setTotalTaxAmount(item.getTotalTaxAmount());
            itemResponse.setTotalAmountAfterTax(item.getTotalAmountAfterTax());

            if (item.getTotalTaxAmount() != null) {
                totalTax = totalTax.add(item.getTotalTaxAmount());
            } else {
                totalTax = totalTax.add(BigDecimal.ZERO);
            }

            if (item.getCgstAmount() != null) {
                totalCgst = totalCgst.add(item.getCgstAmount());
            }
            if (item.getSgstAmount() != null) {
                totalSgst = totalSgst.add(item.getSgstAmount());
            }

            items.add(itemResponse);
        }

        response.setItems(items);
        response.setTotalCgst(totalCgst);
        response.setTotalSgst(totalSgst);
        response.setTotalTax(totalTax);
        response.setFinalAmount(order.getTotalAmount().add(totalTax));

        return response;
    }

    public Page<OrderListResponse> getOrdersList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> ordersPage = orderRepo.findAllOrders(pageable);

        return ordersPage.map(order -> {
            OrderListResponse response = new OrderListResponse();
            response.setOrderId(order.getOrderId());
            response.setOrderDate(order.getOrderDate());
            response.setStatus(order.getOrderStatus().name());
            response.setTotalAmount(order.getTotalAmount());
            response.setItemCount(order.getOrderItems().size());
            return response;
        });
    }

    @Transactional
    public OrderDetailResponse confirmOrder(Integer orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        switch (order.getOrderStatus()) {
            case CONFIRMED -> throw new OrderConfirmationException("Order " + orderId + " is already confirmed.");

            case CANCELED -> throw new OrderConfirmationException("Order " + orderId + " was canceled and cannot be confirmed.");

            case DELIVERED -> throw new OrderConfirmationException("Order " + orderId + " has already been delivered and cannot be confirmed.");

            default -> order.setOrderStatus(OrderStatus.CONFIRMED);
        }

        orderRepo.save(order);
        return getOrderById(orderId);
    }

    @Transactional
    public OrderDetailResponse deliverOrder(Integer orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        switch (order.getOrderStatus()) {
            case DELIVERED -> throw new OrderDeliveryException("Order " + orderId + " is already delivered.");

            case CANCELED -> throw new OrderDeliveryException("Order " + orderId + " was canceled and cannot be delivered.");

            case PENDING -> throw new OrderDeliveryException("Order " + orderId + " must be confirmed before it can be delivered.");

            default -> order.setOrderStatus(OrderStatus.DELIVERED);
        }

        orderRepo.save(order);
        return getOrderById(orderId);
    }

    @Transactional
    public OrderDetailResponse cancelOrder(Integer orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        switch (order.getOrderStatus()) {
            case CANCELED -> throw new OrderCancellationException(
                    "Order " + orderId + " is already canceled."
            );
            case DELIVERED -> throw new OrderCancellationException(
                    "Order " + orderId + " has already been delivered and cannot be canceled."
            );
            default -> order.setOrderStatus(OrderStatus.CANCELED);
        }

        orderRepo.save(order);
        return getOrderById(orderId);
    }

}
