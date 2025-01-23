package fiveguys.Tom.Cafeteria.Server.notification.repository;

import fiveguys.Tom.Cafeteria.Server.notification.entity.AppNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppNotificationRepository extends JpaRepository<AppNotification, Long> {
    public Optional<AppNotification> findById(Long id);

}

