package com.crking7.datn.repositories;

import com.crking7.datn.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> getNotificationByIsReadEqualsAndDeliverStatusEquals(Boolean isRead, Boolean deliverStatus);
}
