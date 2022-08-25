package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.domain.ProductRepository;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.dto.ProductInquiryInfo;
import com.codesoom.assignment.errors.ProductNotFoundException;
import com.codesoom.assignment.errors.UserNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class NormalProductCommandService implements ProductCommandService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public NormalProductCommandService(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ProductInquiryInfo register(ProductData data, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Product product = data.toProduct();
        product.setOwner(user);

        return ProductInquiryInfo.from(productRepository.save(product));
    }

    @Override
    public Product update(Long productId, ProductData productData, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        if (!userId.equals(product.getOwner().getId())) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }

        return product.update(productData);
    }
}
