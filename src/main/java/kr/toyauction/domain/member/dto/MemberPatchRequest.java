package kr.toyauction.domain.member.dto;

import kr.toyauction.global.property.Regex;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberPatchRequest {

    @NotBlank
    @Pattern(regexp = Regex.USERNAME, message = "{REGEX_USERNAME}")
    private String username;

}
