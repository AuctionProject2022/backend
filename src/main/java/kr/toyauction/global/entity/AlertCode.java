package kr.toyauction.global.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlertCode {
    AC0001("경매 낙찰완료","",""),
    AC0002("경매 낙찰실패","",""),
    AC0003("경매 입찰 참여 완료","",""),
    AC0004("경매 판매완료","",""),
    AC0005("경매 판매실패","",""),
    AC0006("판매 등록 완료","AUCTION_SUCCESS","/프론트url")
    ;

    private final String description;
    private final String message;
    private final String url;

}
