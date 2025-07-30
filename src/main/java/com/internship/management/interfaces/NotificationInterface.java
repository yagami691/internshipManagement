package com.internship.management.interfaces;

import com.internship.management.entities.Enterprise;
import com.internship.management.entities.Notification;
import com.internship.management.entities.Users;

import java.util.List;

public interface NotificationInterface {

    void sendNotification(Enterprise enterprise, String message);

    List<Notification> getAllUnSeenNotificationsByUser(Users user);
}
