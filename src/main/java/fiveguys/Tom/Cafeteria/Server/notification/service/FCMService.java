package fiveguys.Tom.Cafeteria.Server.notification.service;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import fiveguys.Tom.Cafeteria.Server.notification.entity.AppNotification;

import java.util.List;

public interface FCMService {
    public void sendMessage(Message message);
    public void sendMessage(MulticastMessage message, List<String> filteredTokens);

    public MulticastMessage createMultiCastMessage(String title, String content, List<String> tokenList);

    public Message createPMessage(String title, String content, String token);

    public AppNotification storeNotification(String title, String content);
}
