package com.hanaro.triptogether.team.dto.request;

import com.hanaro.triptogether.enumeration.PreferenceType;
import com.hanaro.triptogether.enumeration.TeamType;
import lombok.Getter;

@Getter
public class AddTeamReqDto {
    private Long memberIdx;
    private Long accIdx;
    private TeamType teamType;
    private String teamName;
    private PreferenceType preferenceType;
}
