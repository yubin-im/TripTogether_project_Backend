package com.hanaro.triptogether.common.firebase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.hanaro.triptogether.exchangeRate.dto.request.FcmMessageDto;
import com.hanaro.triptogether.exchangeRate.dto.request.FcmSendDto;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FirebaseFCMService {

    private final String API_URL = "https://fcm.googleapis.com/v1/projects/triptogether-e7bac/messages:send";


    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "triptogether-e7bac-firebase-adminsdk-peiki-127517aa66.json";

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
}
