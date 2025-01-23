package fiveguys.Tom.Cafeteria.Server.notification.service;

import fiveguys.Tom.Cafeteria.Server.apiPayload.code.status.ErrorStatus;
import fiveguys.Tom.Cafeteria.Server.notification.entity.AppNotification;
import fiveguys.Tom.Cafeteria.Server.notification.repository.AppNotificationRepository;
import fiveguys.Tom.Cafeteria.Server.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class NotificationQueryServiceImpl implements NotificationQueryService{
    private final AppNotificationRepository notificationRepository;

    @Override
    public AppNotification getNotificationById(Long id) {
        AppNotification notification = notificationRepository.findById(id).orElseThrow(
                () -> new GeneralException(ErrorStatus.NOTIFICATION_NOT_FOUND)
        );
        return notification;
    }
}
