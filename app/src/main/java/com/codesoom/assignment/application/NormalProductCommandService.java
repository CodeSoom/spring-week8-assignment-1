package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.domain.ProductRepository;
import com.codesoom.assignment.domain.Role;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.dto.ProductInquiryInfo;
import com.codesoom.assignment.errors.ProductNotFoundException;
import com.codesoom.assignment.errors.UserNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NormalProductCommandService implements ProductCommandService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public NormalProductCommandService(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ProductInquiryInfo register(ProductData data, Authentication authentication) {
        final Long userId = (Long) authentication.getPrincipal();
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Product product = data.toProduct();
        product.setOwner(user);

        return ProductInquiryInfo.builder()
                .product(productRepository.save(product))
                .build();
    }

    @Override
    public Product update(Long productId, ProductData productData, Authentication authentication) {
        final Long userId = (Long) authentication.getPrincipal();
        checkIfUserExists(userId);

        final Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        checkAuthority(authentication, userId, product.getOwner());

        return product.update(productData);
    }

    private void checkAuthority(Authentication authentication, Long userId, User owner) {
        if (!owner.isSameUser(userId) && Role.isUser((Role) authentication.getCredentials())) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }
    }

    private void checkIfUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
    }
}
