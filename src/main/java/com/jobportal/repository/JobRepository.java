package com.jobportal.repository;

import com.jobportal.entity.Job;
import com.jobportal.entity.User;
import com.jobportal.entity.enums.JobStatus;
import com.jobportal.entity.enums.JobType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JobRepository extends JpaRepository<Job, Long> {

    Page<Job> findByStatus(JobStatus status, Pageable pageable);

    Page<Job> findByRecruiter(User recruiter, Pageable pageable);

    @Query("""
        SELECT j FROM Job j WHERE
        j.status = 'ACTIVE' AND
        (:title IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND
        (:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND
        (:jobType IS NULL OR j.jobType = :jobType) AND
        (:category IS NULL OR LOWER(j.category) LIKE LOWER(CONCAT('%', :category, '%'))) AND
        (:minSalary IS NULL OR j.salaryMin >= :minSalary) AND
        (:maxSalary IS NULL OR j.salaryMax <= :maxSalary)
    """)
    Page<Job> searchJobs(
            @Param("title") String title,
            @Param("location") String location,
            @Param("jobType") JobType jobType,
            @Param("category") String category,
            @Param("minSalary") Double minSalary,
            @Param("maxSalary") Double maxSalary,
            Pageable pageable
    );
}