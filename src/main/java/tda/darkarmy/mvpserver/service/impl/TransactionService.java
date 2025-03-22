package tda.darkarmy.mvpserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tda.darkarmy.mvpserver.exception.ResourceNotFoundException;
import tda.darkarmy.mvpserver.model.Notifications;
import tda.darkarmy.mvpserver.model.Property;
import tda.darkarmy.mvpserver.model.Transaction;
import tda.darkarmy.mvpserver.repository.PropertyRepository;
import tda.darkarmy.mvpserver.repository.TransactionRepository;
import tda.darkarmy.mvpserver.service.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class TransactionService {

//    private static final int POINTS_CONVERSION_RATE = 10;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationTriggerService notificationTriggerService;

//    @Autowired
//    private PointsRepository pointsRepository;
//
//    public boolean redeemPoints(Long userId, int pointsToRedeem) {
//        Points points = pointsRepository.findByUserId(userId);
//        if (points != null && points.getTotalPoints() >= pointsToRedeem) {
//            points.setTotalPoints(points.getTotalPoints() - pointsToRedeem);
//            pointsRepository.save(points);
//            return true;
//        }
//        return false;
//    }

    public List<Transaction> getMyTransactions() {
        return transactionRepository.findByUser(userService.getLoggedInUser());
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction redeem(Transaction transaction) {
        Property property = propertyRepository.findById(transaction.getPropertyName()).orElseThrow(()-> new ResourceNotFoundException("Property not found!"));
        transaction.setStatus("Completed");
        transaction.setPropertyName(property.getName());
        transaction.setUser(userService.getLoggedInUser());
        transaction.setDate(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));

        Notifications notifications = new Notifications();
        notifications.setRead(false);
        notifications.setCreatedAt(LocalDateTime.now());
        notifications.setMessage("You have successfully redeemed "+ transaction.getPointsRedeemed()+  " points for a discount on "+ property.getName());
        notifications.setUserId(userService.getLoggedInUser().getId());
        notifications.setTitle("Points redemption successful!");
        notificationTriggerService.sendNotification(notifications);
        notificationService.createNotification(notifications);
        return transactionRepository.save(transaction);
    }
}
