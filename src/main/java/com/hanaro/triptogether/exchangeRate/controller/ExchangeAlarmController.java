package com.hanaro.triptogether.exchangeRate.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.hanaro.triptogether.exchangeRate.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExchangeAlarmController {

    private final NotificationService notificationService;

    @PostMapping("/exchange")
    public void sendNotification(@RequestBody String token) throws FirebaseMessagingException {
        notificationService.sendMessageByToken(token);
    }


}
