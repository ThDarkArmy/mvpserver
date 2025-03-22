package tda.darkarmy.mvpserver.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tda.darkarmy.mvpserver.model.Notifications;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends MongoRepository<Notifications, String> {
    List<Notifications> findByUserIdOrderByCreatedAtDesc(Long userId);
    Optional<Notifications> findByIdAndUserId(Long id, Long userId);
}
