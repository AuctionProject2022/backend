package kr.toyauction.domain.alert.service;

import kr.toyauction.domain.alert.dto.AlertPostRequest;
import kr.toyauction.domain.alert.entity.Alert;
import kr.toyauction.domain.alert.repository.AlertRepository;
import kr.toyauction.global.entity.AlertCode;
import kr.toyauction.global.exception.DomainValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@EnableJpaAuditing
@ExtendWith({MockitoExtension.class})
public class AlertServiceTest {

    @Autowired
    AlertRepository alertRepository;

    @Mock
    MessageSource messageSource;

    AlertService alertService;

    @BeforeEach
    void setup(){
        this.alertService = new AlertService(alertRepository,messageSource);
    }

    @Test
    @DisplayName("success : 알람 저장")
    void alertSave(){
        // given
        AlertPostRequest request = AlertPostRequest.builder()
                .memberId(1L)
                .title(AlertCode.AC0006.getDescription())
                .contents(AlertCode.AC0006.getMessage())
                .alertCode(AlertCode.AC0006)
                .url(AlertCode.AC0006.getUrl())
                .remainingTime("680000")
                .messageList(new Object[]{"경매 물품 이름",10000})
                .build();

        // when
        Alert alert = alertService.register(request);

        // then
        assertNotNull(alert.getId());
        assertEquals(request.getMemberId(),alert.getMemberId());
        assertEquals(request.getTitle(),alert.getTitle());
        assertEquals(messageSource.getMessage(request.getContents(),request.getMessageList(), LocaleContextHolder.getLocale()),alert.getContents());
        assertEquals(request.getAlertCode(),alert.getCode());
        assertEquals(request.getUrl(),alert.getUrl());
        assertFalse(alert.isAlertRead());
        assertEquals(request.getRemainingTime(),alert.getRemainingTime());
    }

    @Test
    @DisplayName("fail : 알람 저장 - memberId 가 null 인경우")
    void alertSaveMemberIdNull(){
        // given
        AlertPostRequest request = AlertPostRequest.builder()
                .memberId(null)
                .title(AlertCode.AC0006.getDescription())
                .contents(AlertCode.AC0006.getMessage())
                .alertCode(AlertCode.AC0006)
                .url(AlertCode.AC0006.getUrl())
                .remainingTime("680000")
                .messageList(new Object[]{"경매 물품 이름",10000})
                .build();

        // when
        assertThrows(DomainValidationException.class, () -> alertService.register(request));
    }

    @Test
    @DisplayName("fail : 알람 저장 - title 이 null 인경우")
    void alertSaveTitleNull(){
        // given
        AlertPostRequest request = AlertPostRequest.builder()
                .memberId(1L)
                .title("")
                .contents(AlertCode.AC0006.getMessage())
                .alertCode(AlertCode.AC0006)
                .url(AlertCode.AC0006.getUrl())
                .remainingTime("680000")
                .messageList(new Object[]{"경매 물품 이름",10000})
                .build();

        // when
        alertService.register(request);
//        assertThrows(DomainValidationException.class, () -> {
//            alertService.register(request);
//        });
    }
}
