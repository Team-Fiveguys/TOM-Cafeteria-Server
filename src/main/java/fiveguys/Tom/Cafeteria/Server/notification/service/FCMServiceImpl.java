package fiveguys.Tom.Cafeteria.Server.notification.service;

import com.google.firebase.messaging.*;
import fiveguys.Tom.Cafeteria.Server.apiPayload.code.status.ErrorStatus;
import fiveguys.Tom.Cafeteria.Server.notification.entity.AppNotification;
import fiveguys.Tom.Cafeteria.Server.notification.repository.AppNotificationRepository;
import fiveguys.Tom.Cafeteria.Server.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FCMServiceImpl implements FCMService{
    private final AppNotificationRepository notificationRepository;

    @Override
    public void sendMessage(Message message) {
        try {
            String response = FirebaseMessaging.getInstance().send(message);
            log.info("response = {}", response);
        } catch (FirebaseMessagingException e) { // 보내지 못했을 때 예외 처리 생각하기
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendMessage(MulticastMessage message, List<String> filteredTokens) {
        BatchResponse response = null;
        try {
            response = FirebaseMessaging.getInstance().sendEachForMulticast(message);
            ArrayList<String> failedTokens = null;
            if (response.getFailureCount() > 0) {
                List<SendResponse> responses = response.getResponses();
                failedTokens = new ArrayList<>();
                for (int i = 0; i < responses.size(); i++) {
                    if (!responses.get(i).isSuccessful()) {
                        // The order of responses corresponds to the order of the registration tokens.
                        failedTokens.add(filteredTokens.get(i));
                    }
                }
            }
            System.out.println("List of tokens that caused failures: " + failedTokens);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public MulticastMessage createMultiCastMessage(String title, String content, List<String> tokenList) {
        AndroidConfig androidConfig = AndroidConfig.builder()
                .setNotification(
                        AndroidNotification.builder()
                                .setColor("#ffffff")
                                .build())
                .build();
//        ApnsConfig.builder()
//                .setAps(Aps.builder()
//                        .)
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(content)
                .build();

        if (tokenList.isEmpty()) {
            throw new GeneralException(ErrorStatus.REGISTRATION_TOKEN_EMPTY);
        }
        return MulticastMessage.builder()
                .setNotification(notification)
                .setAndroidConfig(androidConfig)
                .addAllTokens(tokenList)
                .build();
    }

    @Override
    public Message createPMessage(String title, String content, String token) {
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(content)
                .build();

        return Message.builder()
                .setToken(token)
                .setNotification(notification)
                .build();
    }
    @Override
    @Transactional
    public AppNotification storeNotification(String title, String content){
        AppNotification notification = AppNotification.builder()
                .title(title)
                .content(content)
                .userAppNotificationList(new ArrayList<>())
                .build();
        return notificationRepository.save(notification);
    }
}
