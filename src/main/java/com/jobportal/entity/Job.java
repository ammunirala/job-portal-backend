package com.jobportal.entity;

import com.jobportal.entity.enums.JobStatus;
import com.jobportal.entity.enums.JobType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String location;

    private Double salaryMin;
    private Double salaryMax;

    @Enumerated(EnumType.STRING)
    private JobType jobType;

    private String category;
    private LocalDate deadline;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiter_id", nullable = false)
    private User recruiter;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) this.status = JobStatus.ACTIVE;
    }

    public Job() {}

    public static JobBuilder builder() { return new JobBuilder(); }

    public static class JobBuilder {
        private String title, description, location, category;
        private Double salaryMin, salaryMax;
        private JobType jobType;
        private LocalDate deadline;
        private JobStatus status;
        private User recruiter;

        public JobBuilder title(String t) { this.title = t; return this; }
        public JobBuilder description(String d) { this.description = d; return this; }
        public JobBuilder location(String l) { this.location = l; return this; }
        public JobBuilder salaryMin(Double s) { this.salaryMin = s; return this; }
        public JobBuilder salaryMax(Double s) { this.salaryMax = s; return this; }
        public JobBuilder jobType(JobType j) { this.jobType = j; return this; }
        public JobBuilder category(String c) { this.category = c; return this; }
        public JobBuilder deadline(LocalDate d) { this.deadline = d; return this; }
        public JobBuilder status(JobStatus s) { this.status = s; return this; }
        public JobBuilder recruiter(User r) { this.recruiter = r; return this; }

        public Job build() {
            Job j = new Job();
            j.title = this.title;
            j.description = this.description;
            j.location = this.location;
            j.salaryMin = this.salaryMin;
            j.salaryMax = this.salaryMax;
            j.jobType = this.jobType;
            j.category = this.category;
            j.deadline = this.deadline;
            j.status = this.status;
            j.recruiter = this.recruiter;
            return j;
        }
    }
}