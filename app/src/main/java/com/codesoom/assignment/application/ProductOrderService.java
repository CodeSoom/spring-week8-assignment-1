package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Order;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.OrderData;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductOrderService implements OrderService {
    private final UserQueryService userQueryService;
    private final ProductQueryService productQueryService;

    public ProductOrderService(UserQueryService userQueryService, ProductQueryService productQueryService) {
        this.userQueryService = userQueryService;
        this.productQueryService = productQueryService;
    }

    @Override
    public Order create(OrderData data, Authentication auth) {
        User buyer = userQueryService.findById((Long) auth.getPrincipal());
        Product product = productQueryService.findById(data.getProductId());

        Order order = new Order(buyer, product, data.getQuantity());
        product.reduceQuantity(data.getQuantity());
        return order;
    }
}
