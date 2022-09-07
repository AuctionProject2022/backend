package kr.toyauction.domain.product.service;

import kr.toyauction.domain.image.repository.ImageRepository;
import kr.toyauction.domain.member.service.MemberService;
import kr.toyauction.domain.product.entity.*;
import kr.toyauction.domain.product.repository.ProductQueryRepository;
import kr.toyauction.domain.product.repository.ProductRepository;
import kr.toyauction.global.exception.DomainNotFoundException;
import kr.toyauction.global.exception.NoAuthorityException;
import kr.toyauction.global.exception.OverlapException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@EnableJpaAuditing
@ExtendWith({MockitoExtension.class})
public class ProductServiceTest {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ImageRepository imageRepository;
    ProductQueryRepository productQueryRepository;
    @Mock
    ApplicationEventPublisher applicationEventPublisher;
    ProductService productService;

    @BeforeEach
    void setUp() {
        this.productService = new ProductService(productRepository,imageRepository,productQueryRepository,applicationEventPublisher);
    }

    @Test
    @DisplayName("success : 상품삭제")
    void deleteProduct(){
        // given
        Long memberId = 1L;

        Product product = Product.builder()
                .productName("NIKE 에어포스")
                .minBidPrice(1000)
                .rightPrice(100000)
                .startSaleDateTime(LocalDateTime.now())
                .endSaleDateTime(LocalDateTime.now().plusDays(7))
                .unitPrice(1000)
                .purchaseTime(PurchaseTime.SIX_MONTHS)
                .deliveryOption(DeliveryOption.DELIVERY)
                .exchangeType(ExchangeType.IMPOSSIBLE)
                .productCondition(ProductCondition.CLEAN)
                .detail("구매한지 한달밖에 안된 최고의 상품입니다.")
                .registerMemberId(memberId)
                .build();

        product = productRepository.save(product);
        Long productId = product.getId();

        // when
        productService.deleteProduct(productId,memberId);
    }

    @Test
    @DisplayName("fail : 상품삭제 - 없는 상품")
    void deleteProductNoneProduct(){
        // given
        Long memberId = 1L;

        Product product = Product.builder()
                .productName("NIKE 에어포스")
                .minBidPrice(1000)
                .rightPrice(100000)
                .startSaleDateTime(LocalDateTime.now())
                .endSaleDateTime(LocalDateTime.now().plusDays(7))
                .unitPrice(1000)
                .purchaseTime(PurchaseTime.SIX_MONTHS)
                .deliveryOption(DeliveryOption.DELIVERY)
                .exchangeType(ExchangeType.IMPOSSIBLE)
                .productCondition(ProductCondition.CLEAN)
                .detail("구매한지 한달밖에 안된 최고의 상품입니다.")
                .registerMemberId(memberId)
                .build();

        product = productRepository.save(product);
        Long productId = product.getId();

        // when
        assertThrows(DomainNotFoundException.class, () -> productService.deleteProduct(Long.MAX_VALUE,memberId));
    }

    @Test
    @DisplayName("fail : 상품삭제 - 등록한 회원이 아닐 때")
    void deleteProductOtherMemberId(){
        // given
        Long memberId = 1L;

        Product product = Product.builder()
                .productName("NIKE 에어포스")
                .minBidPrice(1000)
                .rightPrice(100000)
                .startSaleDateTime(LocalDateTime.now())
                .endSaleDateTime(LocalDateTime.now().plusDays(7))
                .unitPrice(1000)
                .purchaseTime(PurchaseTime.SIX_MONTHS)
                .deliveryOption(DeliveryOption.DELIVERY)
                .exchangeType(ExchangeType.IMPOSSIBLE)
                .productCondition(ProductCondition.CLEAN)
                .detail("구매한지 한달밖에 안된 최고의 상품입니다.")
                .registerMemberId(memberId)
                .build();

        product = productRepository.save(product);
        Long productId = product.getId();

        // when
        assertThrows(NoAuthorityException.class, () -> productService.deleteProduct(productId,2L));
    }
}
