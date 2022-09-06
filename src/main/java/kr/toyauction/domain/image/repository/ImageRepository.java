package kr.toyauction.domain.image.repository;

import kr.toyauction.domain.image.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
    List<ImageEntity> findAllByTargetId(Long targetId);
}
