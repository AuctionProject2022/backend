package kr.toyauction.domain.product.service;

import kr.toyauction.domain.product.dto.BidPostRequest;
import kr.toyauction.domain.product.entity.Bid;
import kr.toyauction.domain.product.entity.Product;
import kr.toyauction.domain.product.repository.BidRepository;
import kr.toyauction.global.exception.DomainValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BidService {

    private final BidRepository bidRepository;
    private final ProductService productService;

    @Transactional
    public Bid registerBid(final Long productId, @NonNull final BidPostRequest bidPostRequest) {

        //입찰할 상품 있는지 확인
        Product product = productService.getProduct(productId);

        //상품 입찰시 바로 구매가 보다 높게 입력한 경우 exception 을 발생 시킨다.
        if(bidPostRequest.getBidPrice() > product.getRightPrice() ){
            throw new DomainValidationException();
        }

        //상품 입찰시 입찰단위가격으로 나머지 연산시 0으로 떨지지 않으면 exception 발생 시킨다.
        if(bidPostRequest.getBidPrice() % product.getUnitPrice() != 0){
            throw new DomainValidationException();
        }

        //상품 입찰시 최소 입찰가보다 낮은 금액으로 입찰할 수 없다.
        if(bidPostRequest.getBidPrice() > product.getMinBidPrice()){
            throw new DomainValidationException();
        }

        Bid bid = Bid.builder()
                .productId(productId)
                .bidPrice(bidPostRequest.getBidPrice())
                .registerMemberId(1L) //임시로 registerId 임의값
                .build();

        bid.validation();

        Bid saved = bidRepository.save(bid);

        return saved;
    }

}