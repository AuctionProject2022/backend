package kr.toyauction.domain.alert.entity;

public enum AlertCode {
    AC0001("경매 낙찰완료"),
    AC0002("경매 낙찰실패"),
    AC0003("경매 입찰 참여 완료"),
    AC0004("경매 판매완료"),
    AC0005("경매 판매실패"),
    AC0006("경매 등록완료")
    ;

    private final String description;

    AlertCode(String description) {
        this.description = description;
    }
}
