package kr.toyauction.domain.product.entity;

public enum DeliveryOption {
    DELIVERY("배송가능"),
    DIRECT("직거래");

    private final String description;

    DeliveryOption(String description) {
        this.description = description;
    }
}
