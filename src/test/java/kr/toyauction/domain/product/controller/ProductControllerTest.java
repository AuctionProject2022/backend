package kr.toyauction.domain.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.toyauction.domain.product.dto.ProductPostRequest;
import kr.toyauction.domain.product.entity.DeliveryOption;
import kr.toyauction.domain.product.entity.ExchangeType;
import kr.toyauction.domain.product.entity.ProductCondition;
import kr.toyauction.domain.product.entity.PurchaseTime;
import kr.toyauction.global.property.TestProperty;
import kr.toyauction.global.property.Url;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class})
class ProductControllerTest {

	@Autowired
	private ObjectMapper objectMapper;

	private MockMvc mockMvc;

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
				.build();
	}

	@Test
	@DisplayName("success : 상품을 등록 합니다.")
	void postProduct() throws Exception {

		ProductPostRequest request = ProductPostRequest.builder()
				.productName("NIKE 에어포스")
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

		mockMvc.perform(post(Url.PRODUCT)
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
	@DisplayName("success : 상품을 조회 합니다.")
	void getProduct() throws Exception {

		Long productId = 5L;

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
								fieldWithPath("data.images[].fileId").description("상품 이미지 파일번호"),
								fieldWithPath("data.images[].fileType").description("상품 이미지 파일타입"),
								fieldWithPath("data.images[].url").description("상품 이미지 경로"),
								fieldWithPath("data.thumbnailImage.fileId").description("상품 썸네일 이미지 파일번호"),
								fieldWithPath("data.thumbnailImage.fileType").description("상품 썸네일 이미지 파일타입"),
								fieldWithPath("data.thumbnailImage.url").description("상품 썸네일 이미지 경로"),
								fieldWithPath("data.productName").description("상품 이름"),
								fieldWithPath("data.maxBidPrice").description("즉시 구매가"),
								fieldWithPath("data.minBidPrice").description("최초 입찰 시작가"),
								fieldWithPath("data.rightPrice").description("현재 입찰가"),
								fieldWithPath("data.startSaleDateTime").description("판매 시작 기간"),
								fieldWithPath("data.endSaleDateTime").description("판매 종료 기간"),
								fieldWithPath("data.unitPrice").description("입찰 단위"),
								fieldWithPath("data.purchaseTime.code").description("구매 시간 코드"),
								fieldWithPath("data.purchaseTime.name").description("구매 시간 내용"),
								fieldWithPath("data.deliveryOption.code").description("배송 옵션 코드"),
								fieldWithPath("data.deliveryOption.name").description("배송 옵션 내용"),
								fieldWithPath("data.isExchange.code").description("교환가능 코드"),
								fieldWithPath("data.isExchange.name").description("교환가능 내용"),
								fieldWithPath("data.productCondition").description("상품 상태"),
								fieldWithPath("data.detail").description("상품 내용"),
								fieldWithPath("data.bidCount").description("총 입찰 수"),
								fieldWithPath("data.bids[].bidId").description("입찰 번호"),
								fieldWithPath("data.bids[].bidSeq").description("입찰 순서"),
								fieldWithPath("data.bids[].bidPrice").description("입찰 금액"),
								fieldWithPath("data.bids[].bidDateTime").description("입찰 시간"),
								fieldWithPath("data.registerMemberId").description("등록자 회원번호"),
								fieldWithPath("data.productSttus.code").description("판매 상태 코드"),
								fieldWithPath("data.productSttus.name").description("판매 상태 이름"),
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