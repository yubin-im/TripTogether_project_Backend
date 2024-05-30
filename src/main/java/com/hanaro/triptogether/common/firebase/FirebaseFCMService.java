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
import com.hanaro.triptogether.dues.dto.request.DuesAlarmRequestDto;
import com.hanaro.triptogether.dues.dto.response.DuesRequestResponseDto;
import com.hanaro.triptogether.exception.ApiException;
import com.hanaro.triptogether.exception.ExceptionEnum;
import com.hanaro.triptogether.exchangeRate.dto.request.FcmMessageDto;
import com.hanaro.triptogether.exchangeRate.dto.request.FcmSendDto;
import com.hanaro.triptogether.member.domain.Member;
import com.hanaro.triptogether.member.domain.MemberRepository;
import com.hanaro.triptogether.team.domain.Team;
import com.hanaro.triptogether.team.domain.TeamRepository;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FirebaseFCMService {

    private final String API_URL = "https://fcm.googleapis.com/v1/projects/triptogether-e7bac/messages:send";
    String firebaseConfigPath = "triptogether-e7bac-firebase-adminsdk-peiki-127517aa66.json";

    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;


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
        try {
            InputStream refreshToken = new ClassPathResource(firebaseConfigPath).getInputStream();

            FirebaseApp firebaseApp = null;
            List firebaseApps = FirebaseApp.getApps();

            if(firebaseApps != null && !firebaseApps.isEmpty()){

                for(Object app : firebaseApps){
                    if (app instanceof FirebaseApp) {
                        FirebaseApp apps = (FirebaseApp) app;
                        if (apps.getName().equals(FirebaseApp.DEFAULT_APP_NAME)) {
                            firebaseApp = (FirebaseApp) app;
                        }
                    }
                }

            }else{
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(refreshToken)).build();

                FirebaseApp.initializeApp(options);
            }


        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

    }
    public BaseResponse notificationAlarm(String title, String body, DuesAlarmRequestDto duesAlarmRequestDto) throws IOException, FirebaseMessagingException {

        firebaseCreateOption();

        List<String> tokenList = new ArrayList<>();
        for (DuesAlarmRequestDto.RequestMemberInfo memberInfo:duesAlarmRequestDto.getMemberInfos()) {
            tokenList.add(memberInfo.getFcmToken());
        }

        MulticastMessage message = MulticastMessage.builder()
                .putData("fcm_type","NOTIFICATION")
                .putData("title",title)
                .putData("body",body)
                .addAllTokens(tokenList)
                .build();

        try {
            BatchResponse response = FirebaseMessaging.getInstance().sendEachForMulticast(message);
            System.out.println("FCMsendsuccess-"+response);
        } catch (FirebaseMessagingException e) {
            System.out.println("FCMsend-"+e.getMessage());
        }

        //BatchResponse response = FirebaseMessaging.getInstance().sendEachForMulticast(message);

        Team team =teamRepository.findById(duesAlarmRequestDto.getTeamIdx()).orElseThrow(()->new ApiException(ExceptionEnum.TEAM_NOT_FOUND));

        List<String> memberNames = new ArrayList<>();
        for (DuesAlarmRequestDto.RequestMemberInfo memberInfo: duesAlarmRequestDto.getMemberInfos()){
            Member member = memberRepository.findById(memberInfo.getMemberIdx()).orElseThrow(()->new ApiException(ExceptionEnum.MEMBER_NOT_FOUND));
            memberNames.add(member.getMemberName());
        }
        // 모임 이름, 요청 대상, 요청 금액
        DuesRequestResponseDto responseDto = new DuesRequestResponseDto(team.getTeamName(),memberNames,duesAlarmRequestDto.getDuesAmount());
        return BaseResponse.res(ResponseStatus.SUCCESS,ResponseStatus.SUCCESS.getMessage(),responseDto);
    }
}
