package kr.toyauction.domain.product.controller;


import kr.toyauction.domain.product.dto.*;

import kr.toyauction.domain.product.entity.Bid;
import kr.toyauction.domain.product.entity.Product;
import kr.toyauction.domain.product.service.BidService;
import kr.toyauction.domain.product.service.ProductService;
import kr.toyauction.global.dto.SuccessResponse;
import kr.toyauction.global.dto.SuccessResponseHelper;
import kr.toyauction.global.dto.VerifyMember;
import kr.toyauction.global.property.Url;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;
	private final BidService bidService;

	@GetMapping(value = Url.PRODUCT + "/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public SuccessResponse<ProductViewResponse> getProduct(@PathVariable final Long productId){
		ProductViewResponse productViewResponse = productService.getProduct(productId);
		return SuccessResponseHelper.success(productViewResponse);
	}

	@DeleteMapping(value = Url.PRODUCT  + "/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('USER')")
	public SuccessResponse<Void> deleteProduct(@PathVariable final Long productId, VerifyMember verifyMember) {
		productService.deleteProduct(productId,verifyMember.getId());
		return SuccessResponseHelper.success(null);
	}

	@PostMapping(value = Url.PRODUCT , produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('USER')")
	public SuccessResponse<ProductPostResponse> postProduct(@Validated @RequestBody final ProductPostRequest request, VerifyMember verifyMember) {
		Product product = productService.save(request, verifyMember.getId());
		return SuccessResponseHelper.success(new ProductPostResponse(product));
	}

	@PostMapping(value = Url.PRODUCT  + "/{productId}" + "/bids", produces = MediaType.APPLICATION_JSON_VALUE)
	public SuccessResponse<BidPostResponse> postBid(@PathVariable(required = true) final Long productId, @Validated @RequestBody final BidPostRequest request) {
		Bid bid = bidService.registerBid(productId, request);
		return SuccessResponseHelper.success(new BidPostResponse(bid));
	}

	@GetMapping(value = Url.PRODUCT  + "/autocomplete", produces = MediaType.APPLICATION_JSON_VALUE)
	public SuccessResponse<List<ProductAutoCompleteResponse>> getAutoComplete(@RequestParam String productName) {
		List<Product> productList = productService.getAutoCompleteProduct(productName);
		return SuccessResponseHelper.success(productList.stream().map(ProductAutoCompleteResponse::new).collect(Collectors.toList()));
	}

	@GetMapping(value = Url.PRODUCT, produces = MediaType.APPLICATION_JSON_VALUE)
	public SuccessResponse<Page<ProductGetResponse>> getProducts(final Pageable pageable, final ProductGetRequest productGetRequest) {
		Page<ProductGetResponse> pageProduct = productService.pageProduct(pageable, productGetRequest);
		return SuccessResponseHelper.success(pageProduct);
	}

}