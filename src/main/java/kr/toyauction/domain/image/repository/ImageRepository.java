package kr.toyauction.domain.image.repository;

import kr.toyauction.domain.image.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
}
