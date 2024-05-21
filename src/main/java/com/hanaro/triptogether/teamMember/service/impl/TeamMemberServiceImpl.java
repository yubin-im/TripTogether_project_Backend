package com.hanaro.triptogether.teamMember.service.impl;

import com.hanaro.triptogether.team.domain.Team;
import com.hanaro.triptogether.team.domain.TeamRepository;
import com.hanaro.triptogether.teamMember.domain.TeamMember;
import com.hanaro.triptogether.teamMember.domain.TeamMemberRepository;
import com.hanaro.triptogether.teamMember.dto.response.TeamMembersResDto;
import com.hanaro.triptogether.teamMember.service.TeamMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamMemberServiceImpl implements TeamMemberService {
    private final TeamMemberRepository teamMemberRepository;
    private final TeamRepository teamRepository;

    // 모임원 전체 출력
    @Transactional
    @Override
    public List<TeamMembersResDto> teamMembers(Long teamIdx) {
        List<TeamMembersResDto> teamMembersResDtos = new ArrayList<>();

        Team team = teamRepository.findById(teamIdx).orElse(null);
        List<TeamMember> teamMembers = teamMemberRepository.findTeamMembersByTeam(team);

        for(int i = 0; i < teamMembers.size(); i++) {
            TeamMembersResDto teamMembersResDto = TeamMembersResDto.builder()
                    .teamMemberIdx(teamMembers.get(i).getTeamMemberIdx())
                    .memberName(teamMembers.get(i).getMember().getMemberName())
                    .teamMemberState(teamMembers.get(i).getTeamMemberState())
                    .memberIdx(teamMembers.get(i).getMember().getMemberIdx())
                    .build();

            teamMembersResDtos.add(teamMembersResDto);
        }

        return teamMembersResDtos;
    }
}
