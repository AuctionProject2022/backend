package kr.toyauction.domain.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.toyauction.domain.alert.dto.AlertGetResponse;
import kr.toyauction.domain.alert.entity.Alert;
import kr.toyauction.domain.image.dto.ImageDto;
import kr.toyauction.domain.image.entity.ImageType;
import kr.toyauction.domain.image.service.ImageService;
import kr.toyauction.domain.product.dto.*;
import kr.toyauction.domain.product.entity.*;
import kr.toyauction.domain.product.service.ProductService;
import kr.toyauction.global.dto.EnumCodeValue;
import kr.toyauction.global.entity.AlertCode;
import kr.toyauction.global.error.GlobalErrorCode;
import kr.toyauction.global.property.TestProperty;
import kr.toyauction.global.property.Url;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, MockitoExtension.class})
class ProductControllerTest {

	@Autowired
	private ObjectMapper objectMapper;

	private MockMvc mockMvc;

	@MockBean
	ImageService imageService;

	@MockBean
	ProductService productService;

	@Value("${property.intra.aws-s3-host}")
	private String imageHost;

	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.apply(
						documentationConfiguration(restDocumentation)
								.uris()
								.withScheme(TestProperty.SPRING_REST_DOCS_SERVER_SCHEME)
								.withHost(TestProperty.SPRING_REST_DOCS_SERVER_HOST)
								.withPort(TestProperty.SPRING_REST_DOCS_SERVER_PORT)
								.and()
								.operationPreprocessors()
								.withRequestDefaults(prettyPrint())
								.withResponseDefaults(prettyPrint()))
				.apply(springSecurity())
				.build();
	}

	@Test
	@DisplayName("success : 상품을 등록 합니다.")
	void postProduct() throws Exception {

		Long thumbnailImageId = 66L;
		List<Long> imageIds = List.of(66L, 67L);

		ProductPostRequest request = ProductPostRequest.builder()
				.productName("NIKE 에어포스")
				.imageIds(imageIds)
				.thumbnailImageId(thumbnailImageId)
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
				.build();
		doNothing().when(imageService).updateProductTarget(any(), any(), any());
		given(productService.save(any(), any())).willReturn(
				Product.builder()
						.id(1249L)
						.productName(request.getProductName())
						.minBidPrice(request.getMinBidPrice())
						.rightPrice(request.getRightPrice())
						.startSaleDateTime(request.getStartSaleDateTime())
						.endSaleDateTime(request.getEndSaleDateTime())
						.unitPrice(request.getUnitPrice())
						.purchaseTime(request.getPurchaseTime())
						.deliveryOption(request.getDeliveryOption())
						.exchangeType(request.getExchangeType())
						.productCondition(request.getProductCondition())
						.detail(request.getDetail())
						.registerMemberId(0L)
						.productStatus(ProductStatus.ON_SALE)
						.build()
		);

		mockMvc.perform(post(Url.PRODUCT)
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + TestProperty.TEST_ACCESS_TOKEN)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(objectMapper.writeValueAsString(request)))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document("post-product",
						requestHeaders(
								headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
						),
						requestFields(
								fieldWithPath("productName").description("상품 이름"),
								fieldWithPath("imageIds").description("상품 이미지 파일 번호"),
								fieldWithPath("thumbnailImageId").description("상품 이미지 썸네일 번호"),
								fieldWithPath("minBidPrice").description("최소 입찰 금액"),
								fieldWithPath("rightPrice").description("즉시 구매 금액"),
								fieldWithPath("startSaleDateTime").description("경매 시작일"),
								fieldWithPath("endSaleDateTime").description("경매 종료일"),
								fieldWithPath("unitPrice").description("입찰 최소 단위"),
								fieldWithPath("purchaseTime").description("구매 시기"),
								fieldWithPath("deliveryOption").description("배송 옵션"),
								fieldWithPath("exchangeType").description("교환 옵션"),
								fieldWithPath("productCondition").description("상품 상태"),
								fieldWithPath("detail").description("상품 상세 설명")

						),
						responseHeaders(
								headerWithName(HttpHeaders.CONTENT_TYPE).description("Content Type")
						),
						relaxedResponseFields(
								fieldWithPath("success").description("성공 여부")
						)
				));
	}

	@Test
	@DisplayName("fail : 상품이름을 입력하지 않으면 상품등록을 실패 합니다.")
	void postProductNotProductName() throws Exception {

		//given
		ProductPostRequest request = ProductPostRequest.builder()
				.productName(null)
				.imageIds(List.of(66L, 67L))
				.thumbnailImageId(66L)
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
				.build();


		// when
		ResultActions resultActions = mockMvc.perform(post(Url.PRODUCT)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(objectMapper.writeValueAsString(request)))
				.andDo(print());


		// then
		resultActions.andExpect(status().isBadRequest());
		resultActions.andExpect(jsonPath("code").value(GlobalErrorCode.G0001.name()));
		resultActions.andExpect(jsonPath("errors[0].field").value("productName"));
	}

	@Test
	@DisplayName("fail : 썸네일을 입력하지 않으면 상품등록을 실패 합니다.")
	void postProductNotThumbnail() throws Exception {

		//given
		ProductPostRequest request = ProductPostRequest.builder()
				.productName("나이키")
				.imageIds(List.of(66L, 67L))
				.thumbnailImageId(null)
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
				.build();


		// when
		ResultActions resultActions = mockMvc.perform(post(Url.PRODUCT)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(objectMapper.writeValueAsString(request)))
				.andDo(print());


		// then
		resultActions.andExpect(status().isBadRequest());
		resultActions.andExpect(jsonPath("code").value(GlobalErrorCode.G0001.name()));
		resultActions.andExpect(jsonPath("errors[0].field").value("thumbnailImageId"));
	}


	@Test
	@DisplayName("success : 상품을 조회 합니다.")
	void getProduct() throws Exception {

		//given

		Long productId = 5L;
		ImageDto thumbnail = ImageDto.builder().imageId(14L).imageType(ImageType.PRODUCT_THUMBNAIL).imageUrl(imageHost + UUID.randomUUID() + ".png").build();
		ImageDto image = ImageDto.builder().imageId(99L).imageType(ImageType.PRODUCT).imageUrl(imageHost + UUID.randomUUID() + ".png").build();

		BidPostResponse bids1 = BidPostResponse.builder().bidId(196L).bidPrice(1500).createDatetime(LocalDateTime.now()).build();
		BidPostResponse bids2 = BidPostResponse.builder().bidId(195L).bidPrice(1200).createDatetime(LocalDateTime.now().minusDays(1)).build();


		given(productService.getProduct(any())).willReturn(
				ProductViewResponse.builder()
						.images(List.of(thumbnail, image))
						.productName("NIKE 에어포스")
						.minBidPrice(1000)
						.rightPrice(100000)
						.startSaleDateTime(LocalDateTime.now())
						.endSaleDateTime(LocalDateTime.now().plusDays(7))
						.unitPrice(1000)
						.purchaseTime(EnumCodeValue.builder().code(PurchaseTime.SIX_MONTHS.name()).value(PurchaseTime.SIX_MONTHS.getValue()).build())
						.deliveryOption(EnumCodeValue.builder().code(DeliveryOption.DELIVERY.name()).value(DeliveryOption.DELIVERY.getValue()).build())
						.exchangeType(EnumCodeValue.builder().code(ExchangeType.IMPOSSIBLE.name()).value(ExchangeType.IMPOSSIBLE.getValue()).build())
						.productCondition(EnumCodeValue.builder().code(ProductCondition.CLEAN.name()).value(ProductCondition.CLEAN.getValue()).build())
						.detail("구매한지 한달밖에 안된 최고의 상품입니다.")
						.productId(productId)
						.registerMemberId(0L)
						.productStatus(EnumCodeValue.builder().code(ProductStatus.ON_SALE.name()).value(ProductStatus.ON_SALE.getValue()).build())
						.bids(List.of(bids1, bids2))
						.build()
		);

		// when then
		mockMvc.perform(get(Url.PRODUCT + "/{productId}", productId)
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document("get-product",
						pathParameters(
								parameterWithName("productId").description("상품 번호")
						),
						responseHeaders(
								headerWithName(HttpHeaders.CONTENT_TYPE).description("Content Type")
						),
						relaxedResponseFields(
								fieldWithPath("data.productId").description("상품 고유번호"),
								fieldWithPath("data.images[].imageId").description("상품 이미지 파일번호"),
								fieldWithPath("data.images[].imageType").description("상품 이미지 파일타입"),
								fieldWithPath("data.images[].imageUrl").description("상품 이미지 경로"),
								fieldWithPath("data.productName").description("상품 이름"),
								fieldWithPath("data.maxBidPrice").description("즉시 구매가"),
								fieldWithPath("data.minBidPrice").description("최초 입찰 시작가"),
								fieldWithPath("data.rightPrice").description("현재 입찰가"),
								fieldWithPath("data.startSaleDateTime").description("판매 시작 기간"),
								fieldWithPath("data.endSaleDateTime").description("판매 종료 기간"),
								fieldWithPath("data.unitPrice").description("입찰 단위"),
								fieldWithPath("data.purchaseTime.code").description("구매 시간 코드"),
								fieldWithPath("data.purchaseTime.value").description("구매 시간 내용"),
								fieldWithPath("data.deliveryOption.code").description("배송 옵션 코드"),
								fieldWithPath("data.deliveryOption.value").description("배송 옵션 내용"),
								fieldWithPath("data.exchangeType.code").description("교환가능 코드"),
								fieldWithPath("data.exchangeType.value").description("교환가능 내용"),
								fieldWithPath("data.productCondition.code").description("상품 상태 코드"),
								fieldWithPath("data.productCondition.value").description("상품 상태 내용"),
								fieldWithPath("data.detail").description("상품 내용"),
								fieldWithPath("data.bids[].bidId").description("입찰 번호"),
								fieldWithPath("data.bids[].bidPrice").description("입찰 금액"),
								fieldWithPath("data.bids[].createDatetime").description("입찰 생성 시간"),
								fieldWithPath("data.registerMemberId").description("등록자 회원번호"),
								fieldWithPath("data.productStatus.code").description("판매 상태 코드"),
								fieldWithPath("data.productStatus.value").description("판매 상태 이름"),
								fieldWithPath("data.createDatetime").description("등록일"),
								fieldWithPath("data.updateDatetime").description("수정일")
						)
				));
	}

	@Test
	@DisplayName("success : 상품을 삭제 합니다.")
	void deleteProduct() throws Exception {

		Long productId = 5L;

		doNothing().when(productService);

		mockMvc.perform(delete(Url.PRODUCT + "/{productId}", productId)
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + TestProperty.TEST_ACCESS_TOKEN)
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document("delete-product",
						pathParameters(
								parameterWithName("productId").description("상품 번호")
						),
						responseHeaders(
								headerWithName(HttpHeaders.CONTENT_TYPE).description("Content Type")
						),
						relaxedResponseFields(
								fieldWithPath("success").description("성공 여부")
						)
				));
	}

	@Test
	@DisplayName("fail : 상품을 삭제 토큰이 없을 때")
	void deleteProductNonToken() throws Exception {

		Long productId = 5L;

		ResultActions resultActions = mockMvc.perform(delete(Url.PRODUCT + "/{productId}", productId)
				.contentType(MediaType.APPLICATION_JSON_VALUE));

		resultActions.andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("success : 상품 목록 조회")
	void getProducts() throws Exception {

		//give
		int page = 0, size = 20;

		ProductGetResponse productGetResponse = ProductGetResponse.builder()
				.productId(1L)
				.thumbnailImage(ImageDto.builder().imageId(14L).imageType(ImageType.PRODUCT_THUMBNAIL).imageUrl(imageHost + UUID.randomUUID() + ".png").build())
				.productName("NIKE 에어포스")
				.maxBidPrice(8000)
				.rightPrice(100000)
				.minBidPrice(1000)
				.unitPrice(1000)
				.endSaleDateTime(LocalDateTime.now().plusDays(7))
				.build();

		List<ProductGetResponse> productList = new ArrayList<>();
		productList.add(productGetResponse);
		Page<ProductGetResponse> responses = new PageImpl<>(productList);

		given(productService.pageProduct(any(),any())).willReturn(responses);

		//when
		ResultActions resultActions = mockMvc.perform(get(Url.PRODUCT+"?page={page}&size={size}&sort=createDatetime,desc",page,size)
						//.header(HttpHeaders.AUTHORIZATION, "Bearer " + TestProperty.TEST_ACCESS_TOKEN)
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document("get-products",
						responseHeaders(
								headerWithName(HttpHeaders.CONTENT_TYPE).description("Content Type")
						),
						relaxedResponseFields(
								fieldWithPath("data.content[].productId").description("상품 고유번호"),
								fieldWithPath("data.content[].thumbnailImage.imageId").description("상품 이미지 파일번호"),
								fieldWithPath("data.content[].thumbnailImage.imageUrl").description("상품 이미지 경로"),
								fieldWithPath("data.content[].thumbnailImage.imageType").description("상품 이미지 파일타입"),
								fieldWithPath("data.content[].productName").description("상품 이름"),
								fieldWithPath("data.content[].maxBidPrice").description("현재 입찰가"),
								fieldWithPath("data.content[].rightPrice").description("즉시 입찰가"),
								fieldWithPath("data.content[].minBidPrice").description("최초 입찰 시작가"),
								fieldWithPath("data.content[].unitPrice").description("입찰 단위"),
								fieldWithPath("data.content[].endSaleDateTime").description("판매 종료 기간")
						)
				));

		// then
		resultActions.andExpect(status().isOk());
		resultActions.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
	}

	@Test
	@DisplayName("success : 상품 검색 자동완성")
	void getAutoComplete() throws Exception {

		//give
		String productName = "NIKE";

		List<Product> productList = List.of(
											Product.builder()
													.id(1L)
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
													.registerMemberId(1L)
													.build(),
											Product.builder()
													.id(2L)
													.productName("DUNK ROW NIKE")
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
													.registerMemberId(2L)
													.build()
										);
		given(productService.getAutoCompleteProduct(any())).willReturn(productList);

		ResultActions resultActions = mockMvc.perform(get(Url.PRODUCT+"/autocomplete?productName={productName}",productName)
						.contentType(MediaType.APPLICATION_JSON_VALUE))
						.andDo(print())
						.andExpect(status().isOk())
						.andDo(document("get-product-autocomplete",
								responseHeaders(
										headerWithName(HttpHeaders.CONTENT_TYPE).description("Content Type")
								),
								relaxedResponseFields(
										fieldWithPath("data[].productId").description("상품 고유번호"),
										fieldWithPath("data[].productName").description("상품 이름")
								)
						));

		// then
		resultActions.andExpect(status().isOk());
		resultActions.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
	}
}