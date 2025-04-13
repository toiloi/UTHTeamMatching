package org.example.uthteammatching.services;

import org.example.uthteammatching.models.Notification;
import org.example.uthteammatching.models.UthUser;
import org.example.uthteammatching.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public void sendNotification(UthUser user, String message) {
        Notification notification = new Notification(message, user);
        notificationRepository.save(notification);
    }
}
