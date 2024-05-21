package com.hanaro.triptogether.teamMember.service.impl;

import com.hanaro.triptogether.enumeration.TeamMemberState;
import com.hanaro.triptogether.team.domain.Team;
import com.hanaro.triptogether.team.domain.TeamRepository;
import com.hanaro.triptogether.teamMember.domain.TeamMember;
import com.hanaro.triptogether.teamMember.domain.TeamMemberRepository;
import com.hanaro.triptogether.teamMember.dto.request.AcceptTeamMemberReqDto;
import com.hanaro.triptogether.teamMember.dto.request.ChangeOwnerReqDto;
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

    // 총무 변경
    @Transactional
    @Override
    public void changeOwner(ChangeOwnerReqDto changeOwnerReqDto) {
        Team team = teamRepository.findById(changeOwnerReqDto.getTeamIdx()).orElse(null);
        List<TeamMember> teamMembers = teamMemberRepository.findTeamMembersByTeam(team);

        for(int i = 0; i < teamMembers.size(); i++) {
            Long teamMemberIdx = teamMembers.get(i).getTeamMemberIdx();

            // 기존 총무 -> 모임원으로 벼경
            if(changeOwnerReqDto.getCurOwnerIdx().equals(teamMemberIdx)) {
                teamMembers.get(i).updateTeamMemberState(TeamMemberState.모임원);
                teamMemberRepository.save(teamMembers.get(i));

                // 모임원 -> 새로운 총무로 변경
            } else if(changeOwnerReqDto.getNewOwnerIdx().equals(teamMemberIdx)) {
                teamMembers.get(i).updateTeamMemberState(TeamMemberState.총무);
                teamMemberRepository.save(teamMembers.get(i));
            }
        }
    }

    // 모임원 수락 (수락대기-> 모임원으로 상태 변경)
    @Transactional
    @Override
    public void acceptTeamMember(AcceptTeamMemberReqDto acceptTeamMemberReqDto) {
        Team team = teamRepository.findById(acceptTeamMemberReqDto.getTeamIdx()).orElse(null);
        List<TeamMember> teamMembers = teamMemberRepository.findTeamMembersByTeam(team);

        for(int i = 0; i < teamMembers.size(); i++) {
            if (acceptTeamMemberReqDto.getTeamMemberIdx().equals(teamMembers.get(i).getTeamMemberIdx())) {
                teamMembers.get(i).updateTeamMemberState(TeamMemberState.모임원);
                teamMemberRepository.save(teamMembers.get(i));
            }
        }
    }

    // 모임원 전체 수락 (수락대기-> 모임원으로 상태 변경)
    @Transactional
    @Override
    public void acceptTeamMembers(Long teamIdx) {
        Team team = teamRepository.findById(teamIdx).orElse(null);
        List<TeamMember> teamMembers = teamMemberRepository.findTeamMembersByTeam(team);

        for(int i = 0; i < teamMembers.size(); i++) {
            if(teamMembers.get(i).getTeamMemberState() == TeamMemberState.수락대기) {
                teamMembers.get(i).updateTeamMemberState(TeamMemberState.모임원);
                teamMemberRepository.save(teamMembers.get(i));
            }
        }
    }

}
