package com.hanaro.triptogether.exchangeRate.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    public void sendMessageByToken(String token) throws FirebaseMessagingException
    {
        FirebaseMessaging.getInstance().sendAsync(Message.builder()
                .putData("title","asdf")
                .putData("content","ddd")
                .setToken(token)
                .build());
    }

}
