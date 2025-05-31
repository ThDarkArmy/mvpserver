package tda.darkarmy.mvpserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tda.darkarmy.mvpserver.model.Notifications;
import tda.darkarmy.mvpserver.repository.NotificationRepository;

import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public List<Notifications> getNotificationsByUserId(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<Notifications> getAll() {
        return notificationRepository.findAll();
    }

    public Notifications markAsRead(Long notificationId, Long userId) {
        Notifications notification = notificationRepository.findByIdAndUserId(notificationId, userId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        return notificationRepository.save(notification);
    }

    @Transactional
    public Notifications createNotification(Notifications notifications) {
        try{
            System.out.println("Notifications: "+notifications.toString());
            Notifications notifications1 = notificationRepository.save(notifications);
//            notificationRepository.flush();
            return notifications1;
        }catch(Exception exception){
            System.out.println("Error Message: "+ exception.getLocalizedMessage());
            return null;
        }

    }
}
