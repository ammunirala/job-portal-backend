package com.jobportal.service;

import com.jobportal.dto.request.JobRequest;
import com.jobportal.dto.response.JobResponse;
import com.jobportal.entity.Job;
import com.jobportal.entity.User;
import com.jobportal.entity.enums.JobStatus;
import com.jobportal.entity.enums.JobType;
import com.jobportal.repository.JobRepository;
import com.jobportal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    // Get current logged-in user
    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Map entity to response DTO
    private JobResponse toResponse(Job job) {
        return JobResponse.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .location(job.getLocation())
                .salaryMin(job.getSalaryMin())
                .salaryMax(job.getSalaryMax())
                .jobType(job.getJobType())
                .category(job.getCategory())
                .deadline(job.getDeadline())
                .status(job.getStatus())
                .recruiterName(job.getRecruiter().getName())
                .recruiterEmail(job.getRecruiter().getEmail())
                .createdAt(job.getCreatedAt())
                .build();
    }

    // Post a new job (RECRUITER only)
    public JobResponse postJob(JobRequest request) {
        User recruiter = getCurrentUser();

        Job job = Job.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .location(request.getLocation())
                .salaryMin(request.getSalaryMin())
                .salaryMax(request.getSalaryMax())
                .jobType(request.getJobType())
                .category(request.getCategory())
                .deadline(request.getDeadline())
                .status(JobStatus.ACTIVE)
                .recruiter(recruiter)
                .build();

        return toResponse(jobRepository.save(job));
    }

    // Get all active jobs (public)
    public Page<JobResponse> getAllJobs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by("createdAt").descending());
        return jobRepository.findByStatus(JobStatus.ACTIVE, pageable)
                .map(this::toResponse);
    }

    // Get job by ID (public)
    public JobResponse getJobById(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        return toResponse(job);
    }

    // Update job (RECRUITER — own job only)
    public JobResponse updateJob(Long id, JobRequest request) {
        User recruiter = getCurrentUser();
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getRecruiter().getId().equals(recruiter.getId())) {
            throw new RuntimeException("You can only edit your own jobs");
        }

        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setLocation(request.getLocation());
        job.setSalaryMin(request.getSalaryMin());
        job.setSalaryMax(request.getSalaryMax());
        job.setJobType(request.getJobType());
        job.setCategory(request.getCategory());
        job.setDeadline(request.getDeadline());

        return toResponse(jobRepository.save(job));
    }

    // Delete job (RECRUITER — own job only)
    public void deleteJob(Long id) {
        User recruiter = getCurrentUser();
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getRecruiter().getId().equals(recruiter.getId())) {
            throw new RuntimeException("You can only delete your own jobs");
        }

        jobRepository.delete(job);
    }

    // Get recruiter's own jobs
    public Page<JobResponse> getMyJobs(int page, int size) {
        User recruiter = getCurrentUser();
        Pageable pageable = PageRequest.of(page, size,
                Sort.by("createdAt").descending());
        return jobRepository.findByRecruiter(recruiter, pageable)
                .map(this::toResponse);
    }

    // Search jobs with filters (public)
    public Page<JobResponse> searchJobs(
            String title, String location, String jobType,
            String category, Double minSalary, Double maxSalary,
            int page, int size) {

        JobType type = null;
        if (jobType != null && !jobType.isBlank()) {
            type = JobType.valueOf(jobType.toUpperCase());
        }

        Pageable pageable = PageRequest.of(page, size,
                Sort.by("createdAt").descending());

        return jobRepository.searchJobs(
                        title, location, type, category,
                        minSalary, maxSalary, pageable)
                .map(this::toResponse);
    }
}