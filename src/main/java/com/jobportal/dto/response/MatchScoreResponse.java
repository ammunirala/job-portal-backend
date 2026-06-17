package com.jobportal.dto.response;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchScoreResponse {
    private int matchPercentage;
    private List<String> matchingSkills;
    private List<String> missingSkills;
    private String feedback;
}