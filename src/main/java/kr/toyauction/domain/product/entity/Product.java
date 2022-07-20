package kr.toyauction.domain.product.entity;

import kr.toyauction.global.entity.BaseEntity;
import kr.toyauction.global.entity.EntitySupport;
import kr.toyauction.global.exception.DomainValidationException;
import kr.toyauction.global.property.Regex;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.*;


@Slf4j
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product extends BaseEntity implements EntitySupport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;

    private int minBidPrice;

    private int rightPrice;

    private LocalDateTime startSaleDateTime;

    private LocalDateTime endSaleDateTime;

    private int unitPrice;
    
    @Enumerated(EnumType.STRING)
	private PurchaseTime purchaseTime;
    
    @Enumerated(EnumType.STRING)
    private DeliveryOption deliveryOption;
    
    @Enumerated(EnumType.STRING)
    private ExchangeType exchangeType;

    @Enumerated(EnumType.STRING)
    private ProductCondition productCondition;

    @Column(length = 4000)
    private String detail;
    
    private long registerMemberId;
    
    @Enumerated(EnumType.STRING)
    private ProductSttus productSttus;

    @Override
    public void validation() {
        if (productName == null) {
            log.error("productName is null");
            throw new DomainValidationException();
        }

        if (startSaleDateTime == null) {
            log.error("startSaleDateTime is null");
            throw new DomainValidationException();
        }

        if (endSaleDateTime == null) {
            log.error("endSaleDateTime is null");
            throw new DomainValidationException();
        }

        //구매시기는 반드시 선택해야 한다.
        if (purchaseTime == null) {
            log.error("purchaseTime is null");
            throw new DomainValidationException();
        }

        //배송옵션을 반드시 선택해야 한다.
        if (deliveryOption == null) {
            log.error("deliveryOption is null");
            throw new DomainValidationException();
        }

        //교환가능 여부는 반드시 선택해야 한다.
        if (exchangeType == null) {
            log.error("exchangeType is null");
            throw new DomainValidationException();
        }

        //상품 상태는 반드시 선택해야 한다.
        if (productCondition == null) {
            log.error("productCondition is null");
            throw new DomainValidationException();
        }

        if (detail == null) {
            log.error("detail is null");
            throw new DomainValidationException();
        }

        //상품명은 1~40글자만 사용할 수 있다.
        //상품명에는 한글, 숫자, 영어 만 입력할 수 있다.
        //상품명은 공백으로만 이루어 질 수 없다.
        if (!productName.matches(Regex.PRODUCTNAME)) {
            log.error("productName : {}", productName);
            throw new DomainValidationException();
        }

        //최소 구매가는 100원 부터 입력할 수 있다.
        if (minBidPrice < 100) {
        	log.error("minBidPrice : {}", minBidPrice);
            throw new DomainValidationException();
        }

        //즉시구매가는 최대 5000만원 이내로 입력할 수 있다.
        if (rightPrice < 100 || rightPrice > 50000000) {
        	log.error("rightPrice : {}", rightPrice);
            throw new DomainValidationException();
        }

        //입찰단위 가격은 1000원 단위로 입력할 수 있다.
        //판매단위 가격은 최대 1000만원 까지 입력할 수 있다.
        if (unitPrice % 1000 != 0 || unitPrice > 10000000) {
        	log.error("unitPrice : {}", unitPrice);
            throw new DomainValidationException();
        }

        //판매종료기간은 판매시작기간보다 더 빠를 수 없다.
        if (endSaleDateTime.isBefore(startSaleDateTime)) {
        	log.error("startSaleDateTime : {}", startSaleDateTime);
            throw new DomainValidationException();
        }

        //상품 설명은 최소 10글자 ~최대 4000글자  입력할 수 있다.
        if (detail.length() <= 10 || detail.length() >= 4000){
            log.error("detail : {}", detail);
            throw new DomainValidationException();
        }

        //판매시작기간은 등록일 기준보다 더 이전일 수 없다.
        if(startSaleDateTime.toLocalDate().isAfter(LocalDate.now())){
            log.error("startSaleDateTime : {}", startSaleDateTime);
            throw new DomainValidationException();
        }

        //판매종료기간은 최대 7일을 넘길 수 없다.
        if(Duration.between(startSaleDateTime, endSaleDateTime).toDays() > 7){
            log.error("startSaleDateTime : {}, endSaleDateTime : {}", startSaleDateTime, endSaleDateTime);
            throw new DomainValidationException();
        }

        //상품 설명 텍스트 입력시 자바스크립트 삽입 공격 방지 기능이 들어가야 한다.

    }
}
