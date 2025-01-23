package fiveguys.Tom.Cafeteria.Server.notification.repository;

import fiveguys.Tom.Cafeteria.Server.notification.entity.AppNotification;
import fiveguys.Tom.Cafeteria.Server.notification.entity.UserAppNotification;
import fiveguys.Tom.Cafeteria.Server.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAppNotificationRepository extends JpaRepository<UserAppNotification, Long> {
    public Optional<UserAppNotification> findByUserAndAppNotification(User user, AppNotification appNotification);

    public void deleteAllByUser(User user);
}
