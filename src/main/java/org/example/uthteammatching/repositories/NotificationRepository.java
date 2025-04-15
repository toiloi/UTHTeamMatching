package org.example.uthteammatching.repositories;

import org.example.uthteammatching.models.Notification;
import org.example.uthteammatching.models.NotificationType;
import org.example.uthteammatching.models.UthUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserOrderByCreatedAtDesc(UthUser user);
    List<Notification> findByUserAndSeenFalse(UthUser user);
    List<Notification> findByUserAndTypeOrderByCreatedAtDesc(UthUser user, NotificationType notificationType);
}

