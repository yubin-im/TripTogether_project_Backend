package com.hanaro.triptogether.teamMember.controller;

import com.hanaro.triptogether.teamMember.service.TeamMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TeamMemberContoller {
    private final TeamMemberService teamMemberService;
    
}
