package kr.toyauction.domain.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.toyauction.domain.image.entity.ImageEntity;
import kr.toyauction.domain.image.entity.ImageType;
import kr.toyauction.domain.image.service.ImageService;
import kr.toyauction.domain.member.entity.Member;
import kr.toyauction.domain.member.service.MemberService;
import kr.toyauction.domain.product.dto.ProductPostRequest;
import kr.toyauction.domain.product.dto.ProductViewResponse;
import kr.toyauction.domain.product.entity.*;
import kr.toyauction.domain.product.service.ProductService;
import kr.toyauction.global.error.GlobalErrorCode;
import kr.toyauction.global.exception.DomainNotFoundException;
import kr.toyauction.global.property.TestProperty;
import kr.toyauction.global.property.Url;
import kr.toyauction.global.util.CommonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;

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
		List<Long> imageIds = List.of(66L,67L);

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
				.imageIds(List.of(66L,67L))
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
				.imageIds(List.of(66L,67L))
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

		// given
		Long productId = 5L;

		Product product = Product.builder()
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
				.productStatus(ProductStatus.ON_SALE)
				.build();

		Field createDatetime = product.getClass().getSuperclass().getDeclaredField("createDatetime");
		createDatetime.setAccessible(true);
		createDatetime.set(product, LocalDateTime.now());

		Field updateDatetime = product.getClass().getSuperclass().getDeclaredField("updateDatetime");
		updateDatetime.setAccessible(true);
		updateDatetime.set(product, LocalDateTime.now());

		ProductViewResponse productViewResponse = new ProductViewResponse(product);

		List<Bid> bids = List.of(
				Bid.builder()
						.id(1L)
						.bidPrice(5000)
						.build(),
				Bid.builder()
						.id(2L)
						.bidPrice(6000)
						.build(),
				Bid.builder()
						.id(3L)
						.bidPrice(7000)
						.build()
		);

		for(int i=0; i<bids.size(); i++){
			Field bidDatetime = bids.get(i).getClass().getSuperclass().getDeclaredField("createDatetime");
			bidDatetime.setAccessible(true);
			bidDatetime.set(bids.get(i), LocalDateTime.now());
		}

		productViewResponse.setBids(bids);

		ImageEntity imageEntity = ImageEntity.builder()
				.id(1L)
				.path("https://cdn.toyauction.kr/images/2022-8-30/ccbe55a9-2f97-46f0-8365-4c49874acfda.png")
				.type(ImageType.PRODUCT_THUMBNAIL)
				.build();

		List<ImageEntity> image = List.of(
				ImageEntity.builder()
						.id(1L)
						.path("https://cdn.toyauction.kr/images/2022-8-30/ccbe55a9-2f97-46f0-8365-4c49874acfda.png")
						.type(ImageType.PRODUCT_THUMBNAIL)
						.build(),
				ImageEntity.builder()
						.id(2L)
						.path("https://cdn.toyauction.kr/images/2022-8-30/ccbe55a9-2f97-46f0-8365-4c49874acfda.png")
						.type(ImageType.PRODUCT)
						.build());

		productViewResponse.setImages(image);

		given(productService.getProduct(any())).willReturn(productViewResponse);



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
								fieldWithPath("data.bids[].bidSeq").description("입찰 순서"),
								fieldWithPath("data.bids[].bidPrice").description("입찰 금액"),
								fieldWithPath("data.bids[].bidDatetime").description("입찰 시간"),
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

		mockMvc.perform(delete(Url.PRODUCT + "/{productId}", productId)
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
}