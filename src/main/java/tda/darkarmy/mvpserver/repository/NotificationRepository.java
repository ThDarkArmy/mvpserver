package tda.darkarmy.mvpserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tda.darkarmy.mvpserver.model.Notifications;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notifications, Long> {
    List<Notifications> findByUserIdOrderByCreatedAtDesc(Long userId);
    Optional<Notifications> findByIdAndUserId(Long id, Long userId);
}
