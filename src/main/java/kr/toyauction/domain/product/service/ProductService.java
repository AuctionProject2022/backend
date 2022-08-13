package kr.toyauction.domain.product.service;

import kr.toyauction.domain.image.entity.ImageType;
import kr.toyauction.domain.product.dto.ProductPostRequest;
import kr.toyauction.domain.product.entity.Product;
import kr.toyauction.domain.product.entity.ProductStatus;
import kr.toyauction.domain.product.repository.ProductRepository;
import kr.toyauction.global.event.ImageRegistryEvent;
import kr.toyauction.global.exception.DomainNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;
	private final ApplicationEventPublisher applicationEventPublisher;

	@Transactional
	public Product registerProduct(@NonNull final ProductPostRequest productPostRequest) {

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
		applicationEventPublisher.publishEvent(
				ImageRegistryEvent.builder()
						.imageIds(Set.of(productPostRequest.getThumbnailImageId()))
						.imageType(ImageType.PRODUCT_THUMBNAIL)
						.targetId(saved.getId()));

		// PRODUCT IMAGE
		if(!productPostRequest.getImageIds().isEmpty()) {
			applicationEventPublisher.publishEvent(
					ImageRegistryEvent.builder()
							.imageIds(productPostRequest.getImageIds())
							.imageType(ImageType.PRODUCT)
							.targetId(saved.getId()));
		}

		return saved;
	}

	@Transactional(readOnly = true)
	public Product getProduct(Long productId) {
		return this.productRepository.findById(productId)
				.orElseThrow(() -> new DomainNotFoundException(productId));
	}

}
