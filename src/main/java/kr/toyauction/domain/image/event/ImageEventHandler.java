package kr.toyauction.domain.image.event;

import kr.toyauction.domain.image.service.ImageService;
import kr.toyauction.global.event.ImageRegistryEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImageEventHandler {

	private final ImageService imageService;

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void publishEventListener(@Validated final ImageRegistryEvent event) {
		imageService.registerTargetId(event.getImageIds(), event.getImageType(), event.getTargetId());
	}
}
