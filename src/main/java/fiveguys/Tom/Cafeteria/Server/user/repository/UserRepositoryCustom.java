package fiveguys.Tom.Cafeteria.Server.user.repository;

import fiveguys.Tom.Cafeteria.Server.notification.entity.AppNotificationType;
import fiveguys.Tom.Cafeteria.Server.user.entity.User;

import java.util.List;

public interface UserRepositoryCustom {
    List<User> findUsersByNotificationSet(String cafeteriaName, AppNotificationType appNotificationType);

    List<User> findUsersByNotificationSet(String subscribedCafeteriaName, String unsubscribedCafeteriaName, AppNotificationType appNotificationType);

    List<User> findUsersByNotificationSet(String subscribedCafeteriaName1, AppNotificationType appNotificationType, String subscribedCafeteriaName2);
}

