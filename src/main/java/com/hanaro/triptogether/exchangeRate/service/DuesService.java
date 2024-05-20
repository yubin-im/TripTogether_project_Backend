package com.hanaro.triptogether.exchangeRate.service;

import com.hanaro.triptogether.exchangeRate.domain.repository.DuesRepository;
import com.hanaro.triptogether.exchangeRate.dto.request.DuesRuleRequestDto;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DuesService {

    private final DuesRepository duesRepository;


    public ResponseEntity setDuesRule(DuesRuleRequestDto duesRuleRequestDto){
        duesRepository.save(duesRuleRequestDto.)
        return ResponseEntity.
    }
}
