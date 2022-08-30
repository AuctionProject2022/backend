package kr.toyauction.domain.alert.service;

import kr.toyauction.domain.alert.dto.AlertGetResponse;
import kr.toyauction.domain.alert.dto.AlertPostRequest;
import kr.toyauction.domain.alert.entity.Alert;
import kr.toyauction.domain.alert.repository.AlertRepository;
import kr.toyauction.global.exception.DomainNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertService {

	private final AlertRepository alertRepository;

	private final MessageSource messageSource;

	@Transactional
	public Alert register(final AlertPostRequest alertPostRequest) {

		Alert alert = Alert.builder()
				.memberId(alertPostRequest.getMemberId())
				.title(alertPostRequest.getAlertCode().getTitle())
				.contents(messageSource.getMessage(alertPostRequest.getContents(),alertPostRequest.getMessageList(), LocaleContextHolder.getLocale()))
				.code(alertPostRequest.getAlertCode())
				.url(alertPostRequest.getUrl())
				.endDatetime(alertPostRequest.getEndDatetime())
				.build();
		alert.validation();
		return alertRepository.save(alert);
	}

	public Page<AlertGetResponse> getAlerts(Long memberId, Pageable pageable){
		return alertRepository.findByMemberId(memberId,pageable).map(AlertGetResponse::new);
	}

	@Transactional
	public String alertCheck(Long alertId){
		Alert alert = alertRepository.findById(alertId)
				.orElseThrow(DomainNotFoundException::new);
		alert.setAlertRead(true);
		return null;
	}

	public String alertUnreadCheck(Long memberId){
		alertRepository.findTop1ByMemberIdAndAlertRead(memberId,false)
				.orElseThrow(DomainNotFoundException::new);
		return null;
	}
}
