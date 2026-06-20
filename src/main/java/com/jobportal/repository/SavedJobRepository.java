package com.jobportal.repository;

import com.jobportal.entity.Job;
import com.jobportal.entity.SavedJob;
import com.jobportal.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface SavedJobRepository extends JpaRepository<SavedJob, Long> {

    Page<SavedJob> findByUser(User user, Pageable pageable);

    List<SavedJob> findByUser(User user);

    Optional<SavedJob> findByUserAndJob(User user, Job job);

    boolean existsByUserAndJob(User user, Job job);

    @Transactional
    void deleteByUserAndJob(User user, Job job);
}