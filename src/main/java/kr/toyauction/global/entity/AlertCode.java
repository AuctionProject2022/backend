package kr.toyauction.global.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlertCode {
    AC0001("경매 낙찰완료","",""),
    AC0002("경매 낙찰실패","",""),
    AC0003("입찰 참여 완료","COMPLETED_BIDDING","/products/"),
    AC0004("경매 판매완료","",""),
    AC0005("경매 판매실패","",""),
    AC0006("판매 등록 완료","AUCTION_SUCCESS","/products/"),
    AC0007("누군가 내상품 입찰","BID_MY_PRODUCT","/products/"),
    AC0008("상품 삭제","DELETE_PRODUCT","/products/")
    ;

    private final String title;
    private final String message;
    private final String url;

}
