package com.jobportal.service;

import com.jobportal.entity.Job;
import com.jobportal.entity.SavedJob;
import com.jobportal.entity.User;
import com.jobportal.repository.JobRepository;
import com.jobportal.repository.SavedJobRepository;
import com.jobportal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SavedJobService {

    private final SavedJobRepository savedJobRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<Map<String, Object>> getSavedJobs() {
        User user = getCurrentUser();
        List<SavedJob> savedJobs = savedJobRepository.findByUser(user);

        return savedJobs.stream().map(saved -> {
            Job job = saved.getJob();

            String salary = "";
            if (job.getSalaryMin() != null && job.getSalaryMax() != null) {
                salary = "₹" + job.getSalaryMin().intValue() + " - ₹" + job.getSalaryMax().intValue();
            } else if (job.getSalaryMin() != null) {
                salary = "₹" + job.getSalaryMin().intValue() + "+";
            }

            String companyName = "";
            if (job.getRecruiter() != null && job.getRecruiter().getName() != null) {
                companyName = job.getRecruiter().getName();
            }

            Map<String, Object> map = new HashMap<>();
            map.put("jobId",       job.getId());
            map.put("title",       job.getTitle() != null ? job.getTitle() : "");
            map.put("companyName", companyName);
            map.put("location",    job.getLocation() != null ? job.getLocation() : "");
            map.put("salary",      salary);
            map.put("jobType",     job.getJobType() != null ? job.getJobType().toString() : "");
            map.put("description", job.getDescription() != null ? job.getDescription() : "");
            map.put("postedAt",    job.getCreatedAt() != null ? job.getCreatedAt().toString() : "");
            return map;
        }).collect(Collectors.toList());
    }

    public Map<String, String> saveJob(Long jobId) {
        User user = getCurrentUser();
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (savedJobRepository.existsByUserAndJob(user, job)) {
            return Map.of("message", "Job already saved");
        }

        SavedJob savedJob = SavedJob.builder()
                .user(user)
                .job(job)
                .build();
        savedJobRepository.save(savedJob);

        return Map.of("message", "Job saved successfully");
    }

    @Transactional
    public Map<String, String> unsaveJob(Long jobId) {
        User user = getCurrentUser();
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!savedJobRepository.existsByUserAndJob(user, job)) {
            return Map.of("message", "Job was not saved");
        }

        savedJobRepository.deleteByUserAndJob(user, job);
        return Map.of("message", "Job unsaved successfully");
    }

    public boolean isJobSaved(Long jobId) {
        User user = getCurrentUser();
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        return savedJobRepository.existsByUserAndJob(user, job);
    }
}