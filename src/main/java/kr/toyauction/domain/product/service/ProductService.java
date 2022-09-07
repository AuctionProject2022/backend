package kr.toyauction.domain.product.service;


import kr.toyauction.domain.image.entity.ImageEntity;
import kr.toyauction.domain.image.repository.ImageRepository;
import kr.toyauction.domain.product.dto.ProductGetRequest;
import kr.toyauction.domain.product.dto.ProductGetResponse;
import kr.toyauction.domain.product.dto.ProductPostRequest;
import kr.toyauction.domain.product.dto.ProductViewResponse;
import kr.toyauction.domain.product.entity.Product;
import kr.toyauction.domain.product.entity.ProductStatus;
import kr.toyauction.domain.product.repository.ProductQueryRepository;
import kr.toyauction.domain.product.repository.ProductRepository;
import kr.toyauction.global.entity.AlertCode;
import kr.toyauction.global.event.AlertPublishEvent;
import kr.toyauction.global.event.ImageProductEvent;
import kr.toyauction.global.exception.DomainNotFoundException;
import kr.toyauction.global.exception.NoAuthorityException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;
	private final ImageRepository imageRepository;
	private final ProductQueryRepository productQueryRepository;
	private final ApplicationEventPublisher applicationEventPublisher;

	@Transactional
	public Product save(@NonNull final ProductPostRequest productPostRequest, Long memberId) {

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
				.registerMemberId(memberId) //임시로 registerId 임의값
				.productStatus(ProductStatus.ON_SALE)
				.build();

		product.validation();

		Product saved = productRepository.save(product);

		// PRODUCT_THUMBNAIL
//		applicationEventPublisher.publishEvent(ImageProductEvent.builder()
//				.thumbnailImageId(productPostRequest.getThumbnailImageId())
//				.imageIds(productPostRequest.getImageIds())
//				.targetId(saved.getId())
//				.build());

		Object[] messageList = {saved.getProductName(),saved.getMinBidPrice()};
		applicationEventPublisher.publishEvent(
				new AlertPublishEvent(saved.getRegisterMemberId()
						,AlertCode.AC0006
						,AlertCode.AC0006.getMessage()
						,AlertCode.AC0006.getUrl()+saved.getId()
						,saved.getEndSaleDateTime()
						,messageList));
		return saved;
	}

	@Transactional(readOnly = true)
	public ProductViewResponse getProduct(Long productId) {

		Product product = this.productRepository.findById(productId).orElseThrow(() -> new DomainNotFoundException(productId));
		ProductViewResponse productViewResponse = new ProductViewResponse(product);
		productViewResponse.setBids(product.getBids());
		List<ImageEntity> image = this.imageRepository.findAllByTargetId(productId);
		productViewResponse.setImages(image);

		return productViewResponse;

	}

	@Transactional(readOnly = true)
	public Page<ProductGetResponse> pageProduct(@NonNull final Pageable pageable, final ProductGetRequest productGetRequest) {
		return this.productQueryRepository.page(pageable, productGetRequest);
	}

	public List<Product> getAutoCompleteProduct(String productName) {
		return this.productRepository.findTop10ByProductNameContainsOrderByIdDesc(productName);
	}

	@Transactional
	public void deleteProduct(Long productId,Long memberId){
		Product product = this.productRepository.findById(productId).orElseThrow(() -> new DomainNotFoundException(productId));
		if (!Objects.equals(memberId, product.getRegisterMemberId())) throw new NoAuthorityException();

		Object[] messageList = {product.getProductName()};
		applicationEventPublisher.publishEvent(
				new AlertPublishEvent(product.getRegisterMemberId()
						,AlertCode.AC0008
						,AlertCode.AC0008.getMessage()
						,AlertCode.AC0008.getUrl()+product.getId()
						,null
						,messageList));

		this.productRepository.delete(product);
	}
}
