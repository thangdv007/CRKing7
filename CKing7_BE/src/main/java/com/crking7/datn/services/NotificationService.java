package com.crking7.datn.services;


import com.crking7.datn.models.Notification;

import java.util.List;

public interface NotificationService {
    List<Notification> loadNotification(Boolean isRead, Boolean deliverStatus);
    Notification modifyNotification(Long id);
    Notification updateNotification(Notification notification);
    Notification createNotification(Notification notification);
}
