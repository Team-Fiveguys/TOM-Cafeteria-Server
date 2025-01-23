package fiveguys.Tom.Cafeteria.Server.user.service;

import fiveguys.Tom.Cafeteria.Server.user.dto.UserRequestDTO;
import fiveguys.Tom.Cafeteria.Server.user.entity.User;

public interface UserCommandService {
    public User create(User user);

    public void grantAdmin(Long userId);

    public void depriveAdmin(Long userId);

    public void initNotificationSet(String token);

    public void updateRegistrationToken(String token);

    public void updateNotificationSet(UserRequestDTO.UpdateNotificationSet updateNotificationSet);

    public void readNotification(Long notificationId);

    public void readAllNotification();

    public void deleteNotification(Long id);
    public void deleteNotifications();

    public User withdrawUser();

    public User revokeRegistrationToken(Long userId);
}
