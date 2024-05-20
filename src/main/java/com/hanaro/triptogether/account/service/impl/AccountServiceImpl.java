package com.hanaro.triptogether.account.service.impl;

import com.hanaro.triptogether.account.domain.AccountRepository;
import com.hanaro.triptogether.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

}
