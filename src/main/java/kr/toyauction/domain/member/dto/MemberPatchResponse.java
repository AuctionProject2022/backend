package kr.toyauction.domain.member.dto;

import kr.toyauction.domain.member.entity.Member;
import kr.toyauction.global.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MemberPatchResponse{
    private String token;
    public MemberPatchResponse(String token){
        this.token = token;
    }
}
