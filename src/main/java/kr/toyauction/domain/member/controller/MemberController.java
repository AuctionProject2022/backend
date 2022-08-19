package kr.toyauction.domain.member.controller;

import kr.toyauction.domain.member.dto.MemberGetRequest;
import kr.toyauction.domain.member.dto.MemberGetResponse;
import kr.toyauction.domain.member.dto.MemberPostRequest;
import kr.toyauction.domain.member.dto.MemberPostResponse;
import kr.toyauction.domain.member.entity.Member;
import kr.toyauction.domain.member.service.MemberService;
import kr.toyauction.global.dto.SuccessResponse;
import kr.toyauction.global.dto.SuccessResponseHelper;
import kr.toyauction.global.exception.WrongValueException;
import kr.toyauction.global.property.Regex;
import kr.toyauction.global.property.Url;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping(Url.MEMBER)
    @PreAuthorize("hasRole('USER')")
    public SuccessResponse<Page<MemberGetResponse>> getMembers(final Pageable pageable, final MemberGetRequest memberGetRequest) {
        Page<Member> pageMember = memberService.pageMember(pageable, memberGetRequest);
        return SuccessResponseHelper.success(pageMember.map(MemberGetResponse::new));
    }

    @GetMapping(Url.MEMBER + "/{memberId}")
    public SuccessResponse<MemberGetResponse> getMember(@PathVariable final Long memberId) {
        Member member = memberService.getMember(memberId);
        return SuccessResponseHelper.success(new MemberGetResponse(member));
    }

    @PostMapping(Url.MEMBER)
    public SuccessResponse<MemberPostResponse> postMember(@Validated @RequestBody final MemberPostRequest request) {
        Member member = memberService.registerMember(request);
        return SuccessResponseHelper.success(new MemberPostResponse(member));
    }

    @GetMapping(Url.MEMBER + "/username/{username}")
    public SuccessResponse<MemberGetResponse> getMemberByUsername(@PathVariable final String username) {
        if (!Pattern.matches(Regex.USERNAME,username)) throw new WrongValueException(username);
        Member member = memberService.getMemberByUsername(username);
        return SuccessResponseHelper.success(new MemberGetResponse(member));
    }
}
