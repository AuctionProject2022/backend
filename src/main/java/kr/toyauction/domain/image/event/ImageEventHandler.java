package kr.toyauction.domain.image.event;

import kr.toyauction.domain.image.service.ImageService;
import kr.toyauction.global.event.ImageProductEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImageEventHandler {

	private final ImageService imageService;

	@EventListener
	public void publishEventListener(final ImageProductEvent event) {
		System.out.println("asbasd");
		imageService.updateProductTarget(event.getThumbnailImageId(), event.getImageIds(), event.getTargetId());
	}
}
