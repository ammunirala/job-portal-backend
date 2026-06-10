package com.jobportal.dto.response;

import com.jobportal.entity.enums.JobStatus;
import com.jobportal.entity.enums.JobType;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobResponse {
    private Long id;
    private String title;
    private String description;
    private String location;
    private Double salaryMin;
    private Double salaryMax;
    private JobType jobType;
    private String category;
    private LocalDate deadline;
    private JobStatus status;
    private String recruiterName;
    private String recruiterEmail;
    private LocalDateTime createdAt;
}