package kr.toyauction.global.token;

import lombok.Getter;

@Getter
public enum JwtEnum {
    AUTHORITY("authority"),
    HEADER("Authorization"),
    USER_NAME("userName"),
    ISSUER("toyAuction"),
    USER_ID("memberId"),
    EXPIRATION("expiration"),
    BEARER("Bearer ");

    private final String description;

    JwtEnum(String description) {
        this.description = description;
    }
}
