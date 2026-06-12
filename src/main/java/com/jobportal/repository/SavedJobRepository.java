package com.jobportal.repository;

import com.jobportal.entity.Job;
import com.jobportal.entity.SavedJob;
import com.jobportal.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SavedJobRepository extends JpaRepository<SavedJob, Long> {
    Page<SavedJob> findByUser(User user, Pageable pageable);
    Optional<SavedJob> findByUserAndJob(User user, Job job);
    boolean existsByUserAndJob(User user, Job job);
}