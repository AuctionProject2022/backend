package kr.toyauction.domain.alert.service;

import kr.toyauction.domain.alert.dto.AlertPostRequest;
import kr.toyauction.domain.alert.entity.Alert;
import kr.toyauction.domain.alert.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertService {

	private final AlertRepository alertRepository;

	@Transactional
	public Alert register(final AlertPostRequest alertPostRequest) {

		Alert alert = Alert.builder()
				.memberId(alertPostRequest.getMemberId())
				.message(alertPostRequest.getMessage())
				.build();
		alert.validation();
		return alertRepository.save(alert);
	}
}
