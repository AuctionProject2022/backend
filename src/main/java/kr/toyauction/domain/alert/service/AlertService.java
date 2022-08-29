package kr.toyauction.domain.alert.service;

import kr.toyauction.domain.alert.dto.AlertPostRequest;
import kr.toyauction.domain.alert.entity.Alert;
import kr.toyauction.domain.alert.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	public Page<Alert> getAlerts(Long memberId,Pageable pageable){
		return alertRepository.findByMemberId(memberId,pageable);
	}
}
