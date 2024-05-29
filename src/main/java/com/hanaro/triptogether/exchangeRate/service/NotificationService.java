package com.hanaro.triptogether.exchangeRate.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.hanaro.triptogether.exchangeRate.dto.request.FcmSendDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {


    // 단일 전송
    public void sendMessageByToken(FcmSendDto fcmSendDto) {
        try {
            FirebaseMessaging.getInstance().send(Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(fcmSendDto.getTitle())
                            .setBody(fcmSendDto.getBody())
                            .build())
                    .setToken(fcmSendDto.getToken())
                    .build());
        } catch (FirebaseMessagingException e) {
            System.out.println("Firebase error sending: {}"+ e);

        }
    }

}
