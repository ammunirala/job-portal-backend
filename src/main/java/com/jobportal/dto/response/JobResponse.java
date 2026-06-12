package com.jobportal.dto.response;

import com.jobportal.entity.enums.JobStatus;
import com.jobportal.entity.enums.JobType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class JobResponse {
    @Setter
    private Long id;
    @Setter
    private String title;
    private String description;
    private String location;
    private Double salaryMin;
    private Double salaryMax;
    private JobType jobType;
    private String category;
    private LocalDate deadline;
    @Setter
    private JobStatus status;
    @Setter
    private String recruiterName;
    private String recruiterEmail;
    @Setter
    private LocalDateTime createdAt;

    public JobResponse() {}

    public static JobResponseBuilder builder() { return new JobResponseBuilder(); }

    public static class JobResponseBuilder {
        private Long id;
        private String title, description, location, category;
        private String recruiterName, recruiterEmail;
        private Double salaryMin, salaryMax;
        private JobType jobType;
        private LocalDate deadline;
        private JobStatus status;
        private LocalDateTime createdAt;

        public JobResponseBuilder id(Long i) { this.id = i; return this; }
        public JobResponseBuilder title(String t) { this.title = t; return this; }
        public JobResponseBuilder description(String d) { this.description = d; return this; }
        public JobResponseBuilder location(String l) { this.location = l; return this; }
        public JobResponseBuilder salaryMin(Double s) { this.salaryMin = s; return this; }
        public JobResponseBuilder salaryMax(Double s) { this.salaryMax = s; return this; }
        public JobResponseBuilder jobType(JobType j) { this.jobType = j; return this; }
        public JobResponseBuilder category(String c) { this.category = c; return this; }
        public JobResponseBuilder deadline(LocalDate d) { this.deadline = d; return this; }
        public JobResponseBuilder status(JobStatus s) { this.status = s; return this; }
        public JobResponseBuilder recruiterName(String n) { this.recruiterName = n; return this; }
        public JobResponseBuilder recruiterEmail(String e) { this.recruiterEmail = e; return this; }
        public JobResponseBuilder createdAt(LocalDateTime c) { this.createdAt = c; return this; }

        public JobResponse build() {
            JobResponse r = new JobResponse();
            r.id = id; r.title = title; r.description = description;
            r.location = location; r.salaryMin = salaryMin;
            r.salaryMax = salaryMax; r.jobType = jobType;
            r.category = category; r.deadline = deadline;
            r.status = status; r.recruiterName = recruiterName;
            r.recruiterEmail = recruiterEmail; r.createdAt = createdAt;
            return r;
        }
    }
}