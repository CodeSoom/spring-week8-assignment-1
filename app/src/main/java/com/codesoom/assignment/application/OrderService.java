package com.codesoom.assignment.application;
import com.codesoom.assignment.domain.Order;
import com.codesoom.assignment.dto.OrderData;
import org.springframework.security.core.Authentication;

/**
 * 주문 로직을 가지고 있습니다.
 */
public interface OrderService {
    /**
     * 주문 정보와 인가를 받아 주문을 생성하고 리턴합니다.
     *
     * @param data 주문 정보
     * @param auth 인가
     * @return 주문
     */
    Order create(OrderData data, Authentication auth);
}
