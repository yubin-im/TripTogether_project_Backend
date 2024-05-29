package com.hanaro.triptogether.common.firebase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.hanaro.triptogether.common.response.BaseResponse;
import com.hanaro.triptogether.common.response.ResponseStatus;
import com.hanaro.triptogether.exchangeRate.dto.request.FcmMessageDto;
import com.hanaro.triptogether.exchangeRate.dto.request.FcmSendDto;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FirebaseFCMService {

    private final String API_URL = "https://fcm.googleapis.com/v1/projects/triptogether-e7bac/messages:send";
    String firebaseConfigPath = "triptogether-e7bac-firebase-adminsdk-peiki-127517aa66.json";


    private String getAccessToken() throws IOException {

        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();;
        return googleCredentials.getAccessToken().getTokenValue();
    }

    public void sendMessageTo(FcmSendDto fcmSendDto) throws IOException {
        String message = makeMessage(fcmSendDto);

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION,"Bearer "+
                        getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE,"application/json; UTF-8")
                .build();

        Response response = client.newCall(request)
                .execute();

        System.out.println(response.body().string()+"Asdfasdf");
    }

    private String makeMessage(FcmSendDto fcmSendDto) throws JsonProcessingException {

        ObjectMapper om = new ObjectMapper();
        FcmMessageDto fcmMessageDto = FcmMessageDto
                .builder()
                .message(FcmMessageDto.Message.builder()
                        .token(fcmSendDto.getToken())
                        .notification(FcmMessageDto.Notification.builder()
                                .title(fcmSendDto.getTitle())
                                .body(fcmSendDto.getBody())
                                .image(null)
                                .build()
                        ).build())
                .validateOnly(false)
                .build();

        return om.writeValueAsString(fcmMessageDto);
    }


    private void firebaseCreateOption() throws IOException {
        FileInputStream refreshToken = new FileInputStream(firebaseConfigPath);
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(refreshToken))
                .build();

        FirebaseApp.initializeApp(options);
    }
    public BaseResponse notificationAlarm(String title, String body, List<String> tokenList) throws  IOException, FirebaseMessagingException{

        firebaseCreateOption();

        MulticastMessage message = MulticastMessage.builder()
                .putData("fcm_type","NOTIFICATION")
                .putData("title",title)
                .putData("body",body)
                .addAllTokens(tokenList)
                .build();

        BatchResponse response = FirebaseMessaging.getInstance().sendEachForMulticast(message);

        return BaseResponse.res(ResponseStatus.SUCCESS,ResponseStatus.SUCCESS.getMessage());
    }
}
