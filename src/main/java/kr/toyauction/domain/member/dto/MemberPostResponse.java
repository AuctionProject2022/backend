package kr.toyauction.domain.member.dto;

import kr.toyauction.domain.member.entity.Member;
import kr.toyauction.global.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MemberPostResponse extends BaseEntity {

    private Long id;
    private String username;
    private LocalDateTime createDatetime;
    private LocalDateTime updateDatetime;
    protected boolean enabled;

    public MemberPostResponse(final Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
        this.createDatetime = member.getCreateDatetime();
        this.updateDatetime = member.getUpdateDatetime();
        this.enabled = member.isEnabled();
    }
}
