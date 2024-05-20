package com.hanaro.triptogether.team.service.impl;

import com.hanaro.triptogether.team.domain.TeamRepository;
import com.hanaro.triptogether.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;

}
