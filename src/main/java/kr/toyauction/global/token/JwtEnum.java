package kr.toyauction.global.token;

import lombok.Getter;

@Getter
public enum JwtEnum {
    ERROR_EXPIRED_TOKEN("expiredJwt"),
    EXCEPTION_PRODUCE("exception"),
    AUTHORITY("authority"),
    HEADER("Authorization"),
    BEARER("Bearer ");

    private final String description;

    JwtEnum(String description) {
        this.description = description;
    }
}
