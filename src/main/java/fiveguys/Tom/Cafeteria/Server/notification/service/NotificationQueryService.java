package fiveguys.Tom.Cafeteria.Server.notification.service;

import fiveguys.Tom.Cafeteria.Server.notification.entity.AppNotification;

public interface NotificationQueryService {
    public AppNotification getNotificationById(Long id);
}
