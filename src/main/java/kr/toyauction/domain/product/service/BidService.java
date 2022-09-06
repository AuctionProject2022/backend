package kr.toyauction.domain.product.service;

import kr.toyauction.domain.product.dto.BidPostRequest;
import kr.toyauction.domain.product.entity.Bid;
import kr.toyauction.domain.product.entity.Product;
import kr.toyauction.domain.product.entity.QBid;
import kr.toyauction.domain.product.repository.BidRepository;
import kr.toyauction.domain.product.repository.ProductRepository;
import kr.toyauction.global.entity.AlertCode;
import kr.toyauction.global.event.AlertPublishEvent;
import kr.toyauction.global.exception.DomainValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BidService {

    private final BidRepository bidRepository;
    private final ProductRepository productRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public Bid registerBid(final Long productId, @NonNull final BidPostRequest bidPostRequest) {

        //입찰할 상품 있는지 확인
        Product product = productRepository.findById(productId).orElseThrow();

        //상품 입찰시 바로 구매가 보다 높게 입력한 경우 exception 을 발생 시킨다.
        if(bidPostRequest.getBidPrice() > product.getRightPrice() ){
            throw new DomainValidationException();
        }

        //상품 입찰시 입찰단위가격으로 나머지 연산시 0으로 떨지지 않으면 exception 발생 시킨다.
        if(bidPostRequest.getBidPrice() % product.getUnitPrice() != 0){
            throw new DomainValidationException();
        }

        //상품 입찰시 최소 입찰가보다 낮은 금액으로 입찰할 수 없다.
        if(bidPostRequest.getBidPrice() < product.getMinBidPrice()){
            throw new DomainValidationException();
        }

        Bid bid = Bid.builder()
                .product(product)
                .bidPrice(bidPostRequest.getBidPrice())
                .registerMemberId(1L) //임시로 registerId 임의값
                .build();

        bid.validation();

        Bid saved = bidRepository.save(bid);

        Object[] messageList = {product.getProductName(),saved.getBidPrice()};
        applicationEventPublisher.publishEvent(
                new AlertPublishEvent(saved.getRegisterMemberId()
                        ,AlertCode.AC0003
                        ,AlertCode.AC0003.getMessage()
                        ,AlertCode.AC0003.getUrl()+product.getId()
                        ,product.getEndSaleDateTime()
                        ,messageList));

        applicationEventPublisher.publishEvent(
                new AlertPublishEvent(product.getRegisterMemberId()
                        ,AlertCode.AC0007
                        ,AlertCode.AC0007.getMessage()
                        ,AlertCode.AC0007.getUrl()+product.getId()
                        ,product.getEndSaleDateTime()
                        ,messageList));

        return saved;
    }

}