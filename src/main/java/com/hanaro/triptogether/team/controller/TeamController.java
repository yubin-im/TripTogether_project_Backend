package com.hanaro.triptogether.team.controller;

import com.hanaro.triptogether.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

}
