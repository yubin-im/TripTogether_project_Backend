package com.hanaro.triptogether.common.firebase;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class FirebaseConfigTest {

    @Test
    public void test() throws IOException {

        InputStream resource = new ClassPathResource("./triptogether-e7bac-firebase-adminsdk-peiki-127517aa66.json").getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(resource, StandardCharsets.UTF_8));

        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }



}