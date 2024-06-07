package com.hanaro.triptogether.common.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {
    @Bean
    public FirebaseApp firebaseApp() throws IOException {

        InputStream resource = new ClassPathResource("./triptogether-e7bac-firebase-adminsdk-peiki-127517aa66.json").getInputStream();
        String jsonString = new String(resource.readAllBytes(), StandardCharsets.UTF_8);

        // \n을 실제 줄 바꿈으로 변환
        String formattedJson = jsonString.replace("\\n", "\n");

        InputStream formattedJsonStream = new ByteArrayInputStream(formattedJson.getBytes(StandardCharsets.UTF_8));

        FirebaseOptions options = FirebaseOptions
                .builder()
                .setCredentials(GoogleCredentials.fromStream(formattedJsonStream))
                .build();
        return FirebaseApp.initializeApp(options);
    }

    @Bean
    public FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
        return FirebaseMessaging.getInstance(firebaseApp);
    }
}