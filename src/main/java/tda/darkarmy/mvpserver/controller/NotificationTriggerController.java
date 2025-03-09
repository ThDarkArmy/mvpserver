package tda.darkarmy.mvpserver.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import tda.darkarmy.mvpserver.model.Notifications;

@Controller
public class NotificationTriggerController {

    @MessageMapping("/send-notification")
    @SendTo("/topic/notifications")
    public Notifications sendNotification(Notifications message) {
        // Broadcast the notification to all subscribed clients
        return message;
    }
}
