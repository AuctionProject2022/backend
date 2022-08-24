package kr.toyauction.domain.product.service;


import kr.toyauction.domain.product.dto.ProductGetRequest;
import kr.toyauction.domain.product.dto.ProductGetResponse;
import kr.toyauction.domain.product.dto.ProductPostRequest;
import kr.toyauction.domain.product.entity.Product;
import kr.toyauction.domain.product.entity.ProductStatus;
import kr.toyauction.domain.product.repository.ProductQueryRepository;
import kr.toyauction.domain.product.repository.ProductRepository;
import kr.toyauction.global.entity.AlertCode;
import kr.toyauction.global.event.AlertPublishEvent;
import kr.toyauction.global.event.ImageProductEvent;
import kr.toyauction.global.exception.DomainNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;
	private final ProductQueryRepository productQueryRepository;
	private final ApplicationEventPublisher applicationEventPublisher;

	@Transactional
	public Product save(@NonNull final ProductPostRequest productPostRequest) {

		//상품 설명 텍스트 입력시 자바스크립트 삽입 공격 방지
		productPostRequest.getDetail()
				.replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;")
				.replaceAll("&", "&amp;")
				.replaceAll("\"", "&quot;");

		Product product = Product.builder()
				.productName(productPostRequest.getProductName())
				.minBidPrice(productPostRequest.getMinBidPrice())
				.rightPrice(productPostRequest.getMinBidPrice())
				.startSaleDateTime(productPostRequest.getStartSaleDateTime())
				.endSaleDateTime(productPostRequest.getEndSaleDateTime())
				.unitPrice(productPostRequest.getUnitPrice())
				.purchaseTime(productPostRequest.getPurchaseTime())
				.deliveryOption(productPostRequest.getDeliveryOption())
				.exchangeType(productPostRequest.getExchangeType())
				.productCondition(productPostRequest.getProductCondition())
				.detail(productPostRequest.getDetail())
				.registerMemberId(1) //임시로 registerId 임의값
				.productStatus(ProductStatus.ON_SALE)
				.build();

		product.validation();

		Product saved = productRepository.save(product);

		// PRODUCT_THUMBNAIL
		applicationEventPublisher.publishEvent(ImageProductEvent.builder()
				.thumbnailImageId(productPostRequest.getThumbnailImageId())
				.imageIds(productPostRequest.getImageIds())
				.targetId(saved.getId())
				.build());

		Duration remainingTime = Duration.between(productPostRequest.getStartSaleDateTime() , productPostRequest.getEndSaleDateTime());
		Object[] messageList = {saved.getProductName(),saved.getMinBidPrice()};
		applicationEventPublisher.publishEvent(
				new AlertPublishEvent(saved.getRegisterMemberId()
						,AlertCode.AC0006
						,AlertCode.AC0006.getMessage()
						,AlertCode.AC0006.getUrl()+"/"+saved.getId()
						,Long.toString(remainingTime.getSeconds())
						,messageList));
		return saved;
	}


	@Transactional(readOnly = true)
	public Product getProduct(Long productId) {
		return this.productRepository.findById(productId)
				.orElseThrow(() -> new DomainNotFoundException(productId));
	}

	@Transactional(readOnly = true)
	public Page<ProductGetResponse> pageProduct(@NonNull final Pageable pageable, final ProductGetRequest productGetRequest) {
		return this.productQueryRepository.page(pageable, productGetRequest);
	}
}
