package kr.toyauction.domain.alert.repository;

import kr.toyauction.domain.alert.dto.AlertGetResponse;
import kr.toyauction.domain.alert.entity.Alert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    Page<Alert> findByMemberId(Long MemberId, Pageable pageable);

    Optional<Alert> findTop1ByMemberIdAndAlertRead(Long MemberId, boolean alertRead);
}
