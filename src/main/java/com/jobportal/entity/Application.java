package com.jobportal.entity;

import com.jobportal.entity.enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "applications",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"job_id", "user_id"}
        ))
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User applicant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status;

    @Column(columnDefinition = "TEXT")
    private String coverLetter;

    @Column(name = "applied_at")
    private LocalDateTime appliedAt;

    @PrePersist
    public void prePersist() {
        this.appliedAt = LocalDateTime.now();
        if (this.status == null) this.status = ApplicationStatus.APPLIED;
    }

    public Application() {}

    public static ApplicationBuilder builder() { return new ApplicationBuilder(); }

    public static class ApplicationBuilder {
        private Job job;
        private User applicant;
        private ApplicationStatus status;
        private String coverLetter;

        public ApplicationBuilder job(Job j) { this.job = j; return this; }
        public ApplicationBuilder applicant(User u) { this.applicant = u; return this; }
        public ApplicationBuilder status(ApplicationStatus s) { this.status = s; return this; }
        public ApplicationBuilder coverLetter(String c) { this.coverLetter = c; return this; }

        public Application build() {
            Application a = new Application();
            a.job = this.job;
            a.applicant = this.applicant;
            a.status = this.status;
            a.coverLetter = this.coverLetter;
            return a;
        }
    }
}