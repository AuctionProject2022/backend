package kr.toyauction.domain.alert.controller;

import kr.toyauction.domain.alert.dto.AlertGetResponse;
import kr.toyauction.domain.alert.entity.Alert;
import kr.toyauction.domain.alert.service.AlertService;
import kr.toyauction.global.dto.SuccessResponse;
import kr.toyauction.global.dto.SuccessResponseHelper;
import kr.toyauction.global.dto.VerifyMember;
import kr.toyauction.global.property.Url;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    @GetMapping(value= Url.ALERT, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USER')")
    public SuccessResponse<Page<AlertGetResponse>> getAlerts(Pageable pageable, VerifyMember verifyMember){
        return SuccessResponseHelper.success(alertService.getAlerts(verifyMember.getId(),pageable));
    }

    @PostMapping(Url.ALERT +"/{alertId}")
    public SuccessResponse<String> getAlert(@PathVariable final Long alertId){
        return SuccessResponseHelper.success(alertService.getAlert(alertId));
    }
}
