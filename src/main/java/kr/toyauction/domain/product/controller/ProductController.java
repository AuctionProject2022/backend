package kr.toyauction.domain.product.controller;


import kr.toyauction.domain.product.dto.*;

import kr.toyauction.domain.product.entity.Bid;
import kr.toyauction.domain.product.entity.Product;
import kr.toyauction.domain.product.service.BidService;
import kr.toyauction.domain.product.service.ProductService;
import kr.toyauction.global.dto.SuccessResponse;
import kr.toyauction.global.dto.SuccessResponseHelper;
import kr.toyauction.global.property.Url;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
	public String deleteProduct(@PathVariable final Long productId) {
		String result = "{\n" +
				"  \"success\": \"true\"\n" +
				"}";

		return result;
	}

	@PostMapping(value = Url.PRODUCT , produces = MediaType.APPLICATION_JSON_VALUE)
	public SuccessResponse<ProductPostResponse> postProduct(@Validated @RequestBody final ProductPostRequest request) {
		Product product = productService.save(request);
		return SuccessResponseHelper.success(new ProductPostResponse(product));
	}

	@PostMapping(value = Url.PRODUCT  + "/{productId}" + "/bids", produces = MediaType.APPLICATION_JSON_VALUE)
	public SuccessResponse<BidPostResponse> postBid(@PathVariable(required = true) final Long productId, @Validated @RequestBody final BidPostRequest request) {
		Bid bid = bidService.registerBid(productId, request);
		return SuccessResponseHelper.success(new BidPostResponse(bid));
	}

	@GetMapping(value = Url.PRODUCT  + "/autocomplete", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getAutoComplete(@RequestParam String prductName) {
		String result = "{\n" +
				"  \"success\": \"true\",\n" +
				"  \"data\": {\n" +
				"    \"size\": \"10\",\n" +
				"    \"content\": [\n" +
				"      {\n" +
				"        \"productId\": 1,\n" +
				"        \"productName\": \"Nike Air Zoom Iorem lorem lorem Pegasus 36 Miami\"\n" +
				"      },\n" +
				"      {\n" +
				"        \"productId\": 2,\n" +
				"        \"productName\": \"Zoom Iorem Pegasus 75 Nike Air\"\n" +
				"      },\n" +
				"      {\n" +
				"        \"productId\": 3,\n" +
				"        \"productName\": \"Zoom Nike Iorem Pegasus 75  Air\"\n" +
				"      },\n" +
				"      {\n" +
				"        \"productId\": 4,\n" +
				"        \"productName\": \"Zoom Iorem Pegasus 75 Air Nike\"\n" +
				"      },\n" +
				"      {\n" +
				"        \"productId\": 5,\n" +
				"        \"productName\": \"Zoom Iorem Pegasus 75  Air Nike Iorem Pegasus\"\n" +
				"      },\n" +
				"      {\n" +
				"        \"productId\": 6,\n" +
				"        \"productName\": \"나이키 에어 Nike Air\"\n" +
				"      },\n" +
				"      {\n" +
				"        \"productId\": 7,\n" +
				"        \"productName\": \"줌 나이키 Zoom Iorem Pegasus 75 Nike Air\"\n" +
				"      },\n" +
				"      {\n" +
				"        \"productId\": 8,\n" +
				"        \"productName\": \"Zoom Iorem Pegasus 75 Nike Air 나이키\"\n" +
				"      },\n" +
				"      {\n" +
				"        \"productId\": 9,\n" +
				"        \"productName\": \"Zoom 나이키 에어 Iorem Pegasus 75 Nike Air\"\n" +
				"      },\n" +
				"      {\n" +
				"        \"productId\": 10,\n" +
				"        \"productName\": \"Nike 나이키 에어\"\n" +
				"      }\n" +
				"    ]\n" +
				"  }\n" +
				"}";

		return result;
	}

	@GetMapping(value = Url.PRODUCT, produces = MediaType.APPLICATION_JSON_VALUE)
	public SuccessResponse<Page<ProductGetResponse>> getProducts(final Pageable pageable, final ProductGetRequest productGetRequest) {
		Page<ProductGetResponse> pageProduct = productService.pageProduct(pageable, productGetRequest);
		return SuccessResponseHelper.success(pageProduct);
	}

}