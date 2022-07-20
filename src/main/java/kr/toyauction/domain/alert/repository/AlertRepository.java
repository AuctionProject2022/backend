package kr.toyauction.domain.alert.repository;

import kr.toyauction.domain.alert.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertRepository extends JpaRepository<Alert, Long> {
}
