package tda.darkarmy.mvpserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tda.darkarmy.mvpserver.model.Notifications;
import tda.darkarmy.mvpserver.service.impl.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public List<Notifications> getNotifications() {
        return notificationService.getAll();
    }

    @GetMapping("/by-id/{userId}")
    public List<Notifications> getNotifications(@PathVariable Long userId) {
        return notificationService.getNotificationsByUserId(userId);
    }

    @PutMapping("/{id}/read")
    public Notifications markAsRead(@PathVariable Long id, @RequestParam Long userId) {
        return notificationService.markAsRead(id, userId);
    }

    @PostMapping("/create")
    public Notifications createNotification(@RequestBody Notifications notifications) {
        return notificationService.createNotification(notifications);
    }
}
