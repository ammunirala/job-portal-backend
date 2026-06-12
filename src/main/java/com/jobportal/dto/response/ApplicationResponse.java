package com.jobportal.dto.response;

import com.jobportal.entity.enums.ApplicationStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ApplicationResponse {
    private Long id;
    private Long jobId;
    private String jobTitle;
    private String companyLocation;
    private String applicantName;
    private String applicantEmail;
    private ApplicationStatus status;
    private String coverLetter;
    private LocalDateTime appliedAt;

    public ApplicationResponse() {}

    public static ApplicationResponseBuilder builder() {
        return new ApplicationResponseBuilder();
    }

    public static class ApplicationResponseBuilder {
        private Long id, jobId;
        private String jobTitle, companyLocation;
        private String applicantName, applicantEmail, coverLetter;
        private ApplicationStatus status;
        private LocalDateTime appliedAt;

        public ApplicationResponseBuilder id(Long i) { this.id = i; return this; }
        public ApplicationResponseBuilder jobId(Long j) { this.jobId = j; return this; }
        public ApplicationResponseBuilder jobTitle(String t) { this.jobTitle = t; return this; }
        public ApplicationResponseBuilder companyLocation(String l) { this.companyLocation = l; return this; }
        public ApplicationResponseBuilder applicantName(String n) { this.applicantName = n; return this; }
        public ApplicationResponseBuilder applicantEmail(String e) { this.applicantEmail = e; return this; }
        public ApplicationResponseBuilder status(ApplicationStatus s) { this.status = s; return this; }
        public ApplicationResponseBuilder coverLetter(String c) { this.coverLetter = c; return this; }
        public ApplicationResponseBuilder appliedAt(LocalDateTime a) { this.appliedAt = a; return this; }

        public ApplicationResponse build() {
            ApplicationResponse r = new ApplicationResponse();
            r.id = id; r.jobId = jobId; r.jobTitle = jobTitle;
            r.companyLocation = companyLocation;
            r.applicantName = applicantName; r.applicantEmail = applicantEmail;
            r.status = status; r.coverLetter = coverLetter;
            r.appliedAt = appliedAt;
            return r;
        }
    }
}