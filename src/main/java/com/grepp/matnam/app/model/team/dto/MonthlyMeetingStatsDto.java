package com.grepp.matnam.app.model.team.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthlyMeetingStatsDto {
    private String meetingMonth;
    private Long totalMeetings;
    private Long completedMeetings;
}
