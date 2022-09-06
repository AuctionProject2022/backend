package kr.toyauction.global.dto;

import kr.toyauction.global.entity.CodeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnumCodeValue {
    private String code;
    private String value;

    public EnumCodeValue(CodeEnum codeEnum){
        this.code = codeEnum.getCode();
        this.value = codeEnum.getValue();
    }

    public String getCode() {
        return this.code;
    }

    public String getValue() {
        return this.value;
    }
}
