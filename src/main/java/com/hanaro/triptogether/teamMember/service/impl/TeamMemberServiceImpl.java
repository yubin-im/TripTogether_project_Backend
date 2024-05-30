package com.hanaro.triptogether.teamMember.service.impl;

import com.hanaro.triptogether.enumeration.TeamMemberState;
import com.hanaro.triptogether.exception.ApiException;
import com.hanaro.triptogether.exception.ExceptionEnum;
import com.hanaro.triptogether.member.domain.Member;
import com.hanaro.triptogether.member.domain.MemberRepository;
import com.hanaro.triptogether.team.domain.Team;
import com.hanaro.triptogether.team.domain.TeamRepository;
import com.hanaro.triptogether.teamMember.domain.TeamMember;
import com.hanaro.triptogether.teamMember.domain.TeamMemberRepository;
import com.hanaro.triptogether.teamMember.dto.request.*;
import com.hanaro.triptogether.teamMember.dto.response.TeamMembersResDto;
import com.hanaro.triptogether.teamMember.service.TeamMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamMemberServiceImpl implements TeamMemberService {
    private final TeamMemberRepository teamMemberRepository;
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;

    // 모임원 전체 출력
    @Transactional
    @Override
    public List<TeamMembersResDto> teamMembers(Long teamIdx) {
        List<TeamMembersResDto> teamMembersResDtos = new ArrayList<>();

        Team team = teamRepository.findById(teamIdx).orElseThrow(() -> new ApiException(ExceptionEnum.TEAM_NOT_FOUND));
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
        Team team = teamRepository.findById(changeOwnerReqDto.getTeamIdx()).orElseThrow(() -> new ApiException(ExceptionEnum.TEAM_NOT_FOUND));
        List<TeamMember> teamMembers = teamMemberRepository.findTeamMembersByTeam(team);

        for(int i = 0; i < teamMembers.size(); i++) {
            Long teamMemberIdx = teamMembers.get(i).getTeamMemberIdx();

            // 기존 총무 -> 모임원으로 변경
            if(changeOwnerReqDto.getCurOwnerIdx().equals(teamMemberIdx)) {
                teamMembers.get(i).updateTeamMemberState(TeamMemberState.모임원);
                teamMembers.get(i).updateModified(LocalDateTime.now(), changeOwnerReqDto.getNewOwnerIdx());
                teamMemberRepository.save(teamMembers.get(i));
                // 모임원 -> 새로운 총무로 변경
            } else if(changeOwnerReqDto.getNewOwnerIdx().equals(teamMemberIdx)) {
                teamMembers.get(i).updateTeamMemberState(TeamMemberState.총무);
                teamMembers.get(i).updateModified(LocalDateTime.now(), changeOwnerReqDto.getNewOwnerIdx());
                teamMemberRepository.save(teamMembers.get(i));
            }
        }
    }

    // 모임원 수락 (수락대기-> 모임원으로 상태 변경)
    @Transactional
    @Override
    public void acceptTeamMember(AcceptTeamMemberReqDto acceptTeamMemberReqDto) {
        Team team = teamRepository.findById(acceptTeamMemberReqDto.getTeamIdx()).orElseThrow(() -> new ApiException(ExceptionEnum.TEAM_NOT_FOUND));
        List<TeamMember> teamMembers = teamMemberRepository.findTeamMembersByTeam(team);

        for(int i = 0; i < teamMembers.size(); i++) {
            if (acceptTeamMemberReqDto.getTeamMemberIdx().equals(teamMembers.get(i).getTeamMemberIdx())) {
                teamMembers.get(i).updateTeamMemberState(TeamMemberState.모임원);
                teamMembers.get(i).updateModified(LocalDateTime.now(), acceptTeamMemberReqDto.getMemberIdx());
                teamMemberRepository.save(teamMembers.get(i));
            }
        }
    }

    // 모임원 전체 수락 (수락대기-> 모임원으로 상태 변경)
    @Transactional
    @Override
    public void acceptTeamMembers(AcceptTeamMembersReqDto acceptTeamMembersReqDto) {
        Team team = teamRepository.findById(acceptTeamMembersReqDto.getTeamIdx()).orElseThrow(() -> new ApiException(ExceptionEnum.TEAM_NOT_FOUND));
        List<TeamMember> teamMembers = teamMemberRepository.findTeamMembersByTeam(team);

        for(int i = 0; i < teamMembers.size(); i++) {
            if(teamMembers.get(i).getTeamMemberState() == TeamMemberState.수락대기) {
                teamMembers.get(i).updateTeamMemberState(TeamMemberState.모임원);
                teamMembers.get(i).updateModified(LocalDateTime.now(), acceptTeamMembersReqDto.getMemberIdx());
                teamMemberRepository.save(teamMembers.get(i));
            }
        }
    }

    // 모임원 거절 (수락대기-> 모임원 삭제)
    // 모임원 내보내기 (모임원-> 모임원 삭제)
    @Transactional
    @Override
    public void rejectTeamMember(AcceptTeamMemberReqDto acceptTeamMemberReqDto) {
        Team team = teamRepository.findById(acceptTeamMemberReqDto.getTeamIdx()).orElseThrow(() -> new ApiException(ExceptionEnum.TEAM_NOT_FOUND));
        List<TeamMember> teamMembers = teamMemberRepository.findTeamMembersByTeam(team);

        for(int i = 0; i < teamMembers.size(); i++) {
            if (acceptTeamMemberReqDto.getTeamMemberIdx().equals(teamMembers.get(i).getTeamMemberIdx())) {
                teamMembers.get(i).delete(LocalDateTime.now(), acceptTeamMemberReqDto.getMemberIdx());
                teamMemberRepository.save(teamMembers.get(i));
            }
        }
    }

    // 모임원 전체 거절 (수락대기-> 모임원 삭제)
    @Transactional
    @Override
    public void rejectTeamMembers(RejectTeamMembersReqDto rejectTeamMembersReqDto) {
        Team team = teamRepository.findById(rejectTeamMembersReqDto.getTeamIdx()).orElseThrow(() -> new ApiException(ExceptionEnum.TEAM_NOT_FOUND));
        List<TeamMember> teamMembers = teamMemberRepository.findTeamMembersByTeam(team);

        for(int i = 0; i < teamMembers.size(); i++) {
            if(teamMembers.get(i).getTeamMemberState() == TeamMemberState.수락대기) {
                teamMembers.get(i).delete(LocalDateTime.now(), rejectTeamMembersReqDto.getMemberIdx());
                teamMemberRepository.save(teamMembers.get(i));
            }
        }
    }

    // 모임원 전체 내보내기 (모임원-> 모임원 삭제)
    @Transactional
    @Override
    public void exportTeamMembers(RejectTeamMembersReqDto rejectTeamMembersReqDto) {
        Team team = teamRepository.findById(rejectTeamMembersReqDto.getTeamIdx()).orElseThrow(() -> new ApiException(ExceptionEnum.TEAM_NOT_FOUND));
        List<TeamMember> teamMembers = teamMemberRepository.findTeamMembersByTeam(team);

        for(int i = 0; i < teamMembers.size(); i++) {
            if(teamMembers.get(i).getTeamMemberState() == TeamMemberState.모임원) {
                teamMembers.get(i).delete(LocalDateTime.now(), rejectTeamMembersReqDto.getMemberIdx());
                teamMemberRepository.save(teamMembers.get(i));
            }
        }
    }

    @Override
    public TeamMember checkIsMyTeam(Team dtoTeam, List<TeamMember> teamMembers) {
        return teamMembers.stream()
                .filter(tm -> tm.getTeam().equals(dtoTeam))
                .findFirst()
                .orElseThrow(() -> new ApiException(ExceptionEnum.INVALID_TEAM_MEMBER));
    }
    @Override
    public void validateTeamMemberState(TeamMember teamMember) {
        String state = teamMember.getTeamMemberState().name();
        if (!state.equals(TeamMemberState.총무.name()) && !state.equals(TeamMemberState.모임원.name())) {
            throw new ApiException(ExceptionEnum.INVALID_TEAM_MEMBER_ROLE);
        }
    }

    //모임원 검색
    @Override
    public TeamMember findTeamMemberByTeamMemberIdx(Long team_member_idx) {
        return teamMemberRepository.findById(team_member_idx)
                .orElseThrow(() -> new ApiException(ExceptionEnum.TEAM_MEMBER_NOT_FOUND));
    }

    //내 모임 검색
    @Override
    public List<TeamMember> findTeamMemberByMemberIdx(Long member_idx) {
        return teamMemberRepository.findTeamMemberByMember_MemberIdx(member_idx);
    }

    // 모임 가입
    @Transactional
    @Override
    public void joinTeamMember(JoinTeamMemberReq joinTeamMemberReq) {
        Member member = memberRepository.findById(joinTeamMemberReq.getMemberIdx()).orElseThrow(() -> new ApiException(ExceptionEnum.MEMBER_NOT_FOUND));
        Team team = teamRepository.findById(joinTeamMemberReq.getTeamIdx()).orElseThrow(() -> new ApiException(ExceptionEnum.TEAM_NOT_FOUND));

        TeamMember teamMember = TeamMember.builder()
                .team(team)
                .member(member)
                .teamMemberState(TeamMemberState.수락대기)
                .createdAt(LocalDateTime.now()).build();

        teamMemberRepository.save(teamMember);
    }

    @Override
    public TeamMember checkIsMyTeamByTeamMemberIdx(Long team_member_idx) {
        return teamMemberRepository.findById(team_member_idx)
                .orElseThrow(() -> new ApiException(ExceptionEnum.INVALID_TEAM_MEMBER));
    }
}
