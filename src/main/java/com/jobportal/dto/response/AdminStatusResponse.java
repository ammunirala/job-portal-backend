package com.jobportal.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminStatusResponse {
    private long totalUsers;
    private long totalJobs;
    private long totalApplications;
    private long activeJobs;
    private long expiredJobs;
}