package kr.toyauction.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    ROLE_GUEST("ROLE_GUEST", "비로그인"),
    ROLE_USER("ROLE_USER", "일반 유저");

    private final String key;
    private final String title;
}
