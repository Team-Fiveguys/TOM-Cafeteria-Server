package fiveguys.Tom.Cafeteria.Server.notification.dto;

import fiveguys.Tom.Cafeteria.Server.notification.entity.AppNotificationType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class NotificationRequestDTO {
    @Getter
    public static class SendOneDTO{
        private Long cafeteriaId;
        private String content;
        private Long receiverId;
    }

    @Getter
    @Builder
    public static class SendAdminsDTO{
        private String title;
        private String content;
    }

    @Getter
    public static class SendAllDTO{
        private String title;
        private String content;
    }
    @Getter
    public static class SendSubscriberDTO{
        private String title;
        private String content;
        private String cafeteriaName;
        private AppNotificationType notificationType;
    }

    @Getter
    public static class ReadNotifications{
        private List<Long> notificationIdList;
    }

}
