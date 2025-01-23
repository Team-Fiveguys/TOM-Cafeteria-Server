package fiveguys.Tom.Cafeteria.Server.notification.service;

import fiveguys.Tom.Cafeteria.Server.cafeteria.event.DietPhotoUploadEvent;
import fiveguys.Tom.Cafeteria.Server.notification.dto.NotificationRequestDTO;

public interface NotificationService {
    public void sendAll(NotificationRequestDTO.SendAllDTO dto);

    public void sendSubScriber(NotificationRequestDTO.SendSubscriberDTO dto);

    public void sendOne(Long cafeteriaId, String content, Long receiverId);

    public void sendAdmins(NotificationRequestDTO.SendAdminsDTO dto);

    public void sendSubScriberTest(NotificationRequestDTO.SendSubscriberDTO dto);

    public void sendTodayDietNotification();
    public void sendDietUploadNotification(DietPhotoUploadEvent event);
}
