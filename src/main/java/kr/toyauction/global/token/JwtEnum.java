package kr.toyauction.global.token;

import lombok.Getter;

@Getter
public enum JwtEnum {
    AUTHORITY("권한"),
    HEADER("Authorization"),
    USER_NAME("닉네임"),
    ISSUER("toyAuction"),
    BEARER("Bearer ");

    private final String description;

    JwtEnum(String description) {
        this.description = description;
    }
}
