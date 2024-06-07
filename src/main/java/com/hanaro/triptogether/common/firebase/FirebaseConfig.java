package com.hanaro.triptogether.common.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {
    @Bean
    public FirebaseApp firebaseApp() throws IOException {

        System.out.println("fcm");
        InputStream resource = new ClassPathResource("triptogether-e7bac-firebase-adminsdk-peiki-127517aa66.json").getInputStream();

//        JsonReader jsonReader = new JsonReader(new InputStreamReader(resource));
//        jsonReader.setLenient(true);

        byte[] jsonBytes = resource.readAllBytes();
        System.out.println("fcm");
        String jsonString = new String(jsonBytes, StandardCharsets.UTF_8);
        System.out.println("fcm"+jsonString);

        // Gson을 사용하여 JSON 문자열을 JsonObject로 파싱
        Gson gson = new Gson();
        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();

        // JsonObject를 다시 문자열로 변환
        String jsonFormattedString = gson.toJson(jsonObject);

        // 문자열을 다시 InputStream으로 변환
        InputStream jsonInputStream = new ByteArrayInputStream(jsonFormattedString.getBytes(StandardCharsets.UTF_8));


        FirebaseOptions options = FirebaseOptions
                .builder()
                .setCredentials(GoogleCredentials.fromStream(jsonInputStream))
                .build();
        return FirebaseApp.initializeApp(options);
    }

    @Bean
    public FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
        return FirebaseMessaging.getInstance(firebaseApp);
    }
}