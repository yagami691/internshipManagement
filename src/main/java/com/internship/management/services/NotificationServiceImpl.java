package com.internship.management.services;

import com.internship.management.entities.Enterprise;
import com.internship.management.entities.Notification;
import com.internship.management.entities.Users;
import com.internship.management.interfaces.NotificationInterface;
import com.internship.management.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationInterface {

     private final NotificationRepository notificationRepository;
     private final SimpMessagingTemplate messagingTemplate;

     public void sendNotification(Enterprise enterprise, String message) {

          Notification notif = new Notification();
          notif.setRecipient(enterprise);
          notif.setMessage(message);
          notificationRepository.save(notif);

          messagingTemplate.convertAndSend(
                  "/topic/enterprise/" + enterprise.getId(),
                  Map.of("content", message)
          );
     }

     public List<Notification> getAllUnSeenNotificationsByUser(Users user) {
          return notificationRepository.findByRecipientAndSeenFalse(user);
     }

}
