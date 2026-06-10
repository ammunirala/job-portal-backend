package com.jobportal.dto.request;

import com.jobportal.entity.enums.JobType;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class JobRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Location is required")
    private String location;

    private Double salaryMin;
    private Double salaryMax;

    @NotNull(message = "Job type is required")
    private JobType jobType;

    private String category;

    @Future(message = "Deadline must be a future date")
    private LocalDate deadline;
}