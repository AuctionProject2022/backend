package kr.toyauction.domain.product.entity;

public enum PurchaseTime {
    SIX_MONTHS("6개월 이내"),
    ONE_TO_TWO_YEARS("1~2년 이내"),
    NO_REMEMBER("기억안남");

    private final String description;

    PurchaseTime(String description) {
        this.description = description;
    }
}
