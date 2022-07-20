package kr.toyauction.domain.product.entity;

public enum ExchangeType {
    POSSIBLE("교환가능"),
    IMPOSSIBLE("교환불가");

    private final String description;

    ExchangeType(String description) {
        this.description = description;
    }
}
