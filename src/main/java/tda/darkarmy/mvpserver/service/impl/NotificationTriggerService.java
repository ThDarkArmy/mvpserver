package tda.darkarmy.mvpserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import tda.darkarmy.mvpserver.model.Notifications;

@Service
public class NotificationTriggerService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendNotification(Notifications message) {
        // Send the notification to all subscribers of "/topic/notifications"
        messagingTemplate.convertAndSend("/topic/notifications", message);
    }
}
