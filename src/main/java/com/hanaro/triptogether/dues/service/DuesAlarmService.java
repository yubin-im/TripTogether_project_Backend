package com.hanaro.triptogether.dues.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DuesAlarmService {

    //단체 전송

    public void sendPush()throws FirebaseMessagingException{
        FirebaseMessaging.getInstance().sendMulticast(makeMessages("asd","Asdf", Collections.emptyList()));
    }

    public static MulticastMessage makeMessages(String title, String body, List<String> targetTokens) {
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();
        return MulticastMessage.builder()
                .setNotification(notification)
                .addAllTokens(targetTokens)
                .build();

    }
}
