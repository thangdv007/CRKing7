package com.crking7.datn.services.impl;

import com.crking7.datn.models.Notification;
import com.crking7.datn.repositories.NotificationRepository;
import com.crking7.datn.services.NotificationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;


    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public List<Notification> loadNotification(Boolean isRead, Boolean deliverStatus) {
        return notificationRepository.getNotificationByIsReadEqualsAndDeliverStatusEquals(isRead, deliverStatus);
    }

    @Override
    public Notification modifyNotification(Long id) {
        Notification notification = notificationRepository.findById(id).get();
        notification.setIsRead(true);
        notification.setDeliverStatus(true);
        return notificationRepository.save(notification);
    }

    @Override
    public Notification updateNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public Notification createNotification(Notification notification) {
        return notificationRepository.save(notification);
    }
}
