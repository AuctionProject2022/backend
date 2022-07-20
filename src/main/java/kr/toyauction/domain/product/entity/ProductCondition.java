package kr.toyauction.domain.product.entity;

public enum ProductCondition {
    NEW("새 제품"),
    CLEAN("깨끗한 편"),
    USED("사용감 있음"),
    REPAIR_REQUIRED("수리필요");

    private final String description;

    ProductCondition(String description) {
        this.description = description;
    }
}
