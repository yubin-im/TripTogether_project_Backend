package com.hanaro.triptogether.exchangeRate.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanaro.triptogether.exchangeRate.dto.ExchangeDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class ExchangeUtils {

    @Value("${exchange-authkey}")
    private String authkey;

    @Value("${exchange-data}")
    private String data;

    private final String searchDate = getSearchDate();

    WebClient webClient;

    public JsonNode getExchangeDataSync() {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();

        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

        webClient = WebClient.builder().uriBuilderFactory(factory).build();
        String responseBody = webClient.get()
                .uri(builder -> builder
                        .scheme("https")
                        .host("www.koreaexim.go.kr")
                        .path("/site/program/financial/exchangeJSON")
                        .queryParam("authkey",authkey)
                        .queryParam("searchdate",searchDate)
                        .queryParam("data",data)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return parseJson(responseBody);
    }

    public JsonNode parseJson(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(responseBody);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public List<ExchangeDto> getExchangeDataAsDtoList() {
        JsonNode jsonNode = getExchangeDataSync();

        if(jsonNode != null && jsonNode.isArray()) {
            List<ExchangeDto> exchangeDtoList = new ArrayList<>();

            for(JsonNode node: jsonNode) {
                ExchangeDto exchangeDto = convertJsonToExchangeDto(node);
                exchangeDtoList.add(exchangeDto);
            }
            return exchangeDtoList;
        }
        return Collections.emptyList();
    }

    private ExchangeDto convertJsonToExchangeDto(JsonNode jsonNode) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.treeToValue(jsonNode,ExchangeDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getSearchDate() {
        LocalDate currentDate = LocalDate.now();
        DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
        LocalDateTime nowTime = LocalDateTime.now();
        LocalTime elevenAM = LocalTime.of(11,0);

        if(nowTime.toLocalTime().isBefore(elevenAM)) {
            return currentDate.minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }
        if (dayOfWeek.getValue() == 6)
            return currentDate.minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        if (dayOfWeek.getValue() == 7)
            return currentDate.minusDays(2).format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        return currentDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
}
