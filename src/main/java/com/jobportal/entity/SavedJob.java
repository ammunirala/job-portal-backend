package com.jobportal.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "saved_jobs",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"user_id", "job_id"}
        ))
public class SavedJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    private LocalDateTime savedAt;

    @PrePersist
    public void prePersist() {
        this.savedAt = LocalDateTime.now();
    }

    public SavedJob() {}

    public static SavedJobBuilder builder() { return new SavedJobBuilder(); }

    public static class SavedJobBuilder {
        private User user;
        private Job job;

        public SavedJobBuilder user(User u) { this.user = u; return this; }
        public SavedJobBuilder job(Job j) { this.job = j; return this; }

        public SavedJob build() {
            SavedJob s = new SavedJob();
            s.user = this.user;
            s.job = this.job;
            return s;
        }
    }
}