package com.crking7.datn.web.admin;

import com.crking7.datn.models.Notification;
import com.crking7.datn.services.ArticleService;
import com.crking7.datn.services.NotificationService;
import com.crking7.datn.web.dto.request.ArticleRequest;
import com.crking7.datn.web.dto.request.ArticleUDRequest;
import com.crking7.datn.web.dto.response.ArticleResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class ANotificationRest {
    private final NotificationService notificationService;

    public ANotificationRest(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/load-notification")
    public ResponseEntity<?> loadNotification(){
        return new ResponseEntity<>(notificationService.loadNotification(false, true), HttpStatus.OK);
    }

    @GetMapping("/read-notification")
    public ResponseEntity<?> readNotification(@RequestParam("id") Long id){
        return new ResponseEntity<>(notificationService.modifyNotification(id), HttpStatus.OK);
    }
    @GetMapping("/push-notification")
    public ResponseEntity<?> pushNotification(){
        List<Notification> notifications = notificationService.loadNotification(false, false);
        for (Notification n: notifications){
            n.setDeliverStatus(true);
            notificationService.updateNotification(n);
        }
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }
}
