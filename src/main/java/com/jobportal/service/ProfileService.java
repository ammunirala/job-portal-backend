package com.jobportal.service;

import com.jobportal.config.FileStorageConfig;
import com.jobportal.dto.request.ProfileRequest;
import com.jobportal.dto.response.JobResponse;
import com.jobportal.dto.response.ProfileResponse;
import com.jobportal.entity.*;
import com.jobportal.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final SavedJobRepository savedJobRepository;
    private final JobRepository jobRepository;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private ProfileResponse toResponse(Profile profile) {
        return ProfileResponse.builder()
                .id(profile.getId())
                .name(profile.getUser().getName())
                .email(profile.getUser().getEmail())
                .bio(profile.getBio())
                .skills(profile.getSkills())
                .resumeUrl(profile.getResumeUrl())
                .linkedinUrl(profile.getLinkedinUrl())
                .experienceYears(profile.getExperienceYears())
                .build();
    }

    // Get profile — auto create if not exists
    public ProfileResponse getProfile() {
        User user = getCurrentUser();
        Profile profile = profileRepository.findByUser(user)
                .orElseGet(() -> profileRepository.save(
                        Profile.builder().user(user).build()));
        return toResponse(profile);
    }

    // Update profile
    public ProfileResponse updateProfile(ProfileRequest request) {
        User user = getCurrentUser();
        Profile profile = profileRepository.findByUser(user)
                .orElseGet(() -> Profile.builder().user(user).build());

        profile.setBio(request.getBio());
        profile.setSkills(request.getSkills());
        profile.setLinkedinUrl(request.getLinkedinUrl());
        profile.setExperienceYears(request.getExperienceYears());

        return toResponse(profileRepository.save(profile));
    }

    // Upload resume PDF
    public ProfileResponse uploadResume(MultipartFile file) throws IOException {
        // File type check — PDF only
        if (!file.getContentType().equals("application/pdf")) {
            throw new RuntimeException("Only PDF files are allowed");
        }

        // File size check — max 5MB
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new RuntimeException("File size must be less than 5MB");
        }

        User user = getCurrentUser();

        // Unique filename generate karo
        String filename = UUID.randomUUID() + "_" +
                user.getId() + ".pdf";
        Path path = Paths.get(FileStorageConfig.UPLOAD_DIR + filename);
        Files.copy(file.getInputStream(), path,
                StandardCopyOption.REPLACE_EXISTING);

        Profile profile = profileRepository.findByUser(user)
                .orElseGet(() -> Profile.builder().user(user).build());

        profile.setResumeUrl(FileStorageConfig.UPLOAD_DIR + filename);
        return toResponse(profileRepository.save(profile));
    }

    // Save a job
    public String saveJob(Long jobId) {
        User user = getCurrentUser();
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (savedJobRepository.existsByUserAndJob(user, job)) {
            throw new RuntimeException("Job already saved");
        }

        savedJobRepository.save(SavedJob.builder()
                .user(user).job(job).build());
        return "Job saved successfully";
    }

    // Unsave a job
    public String unsaveJob(Long jobId) {
        User user = getCurrentUser();
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        SavedJob savedJob = savedJobRepository.findByUserAndJob(user, job)
                .orElseThrow(() -> new RuntimeException("Job not saved"));

        savedJobRepository.delete(savedJob);
        return "Job removed from saved list";
    }

    // Get all saved jobs
    public Page<JobResponse> getSavedJobs(int page, int size) {
        User user = getCurrentUser();
        Pageable pageable = PageRequest.of(page, size,
                Sort.by("savedAt").descending());
        return savedJobRepository.findByUser(user, pageable)
                .map(saved -> JobResponse.builder()
                        .id(saved.getJob().getId())
                        .title(saved.getJob().getTitle())
                        .location(saved.getJob().getLocation())
                        .jobType(saved.getJob().getJobType())
                        .salaryMin(saved.getJob().getSalaryMin())
                        .salaryMax(saved.getJob().getSalaryMax())
                        .status(saved.getJob().getStatus())
                        .recruiterName(saved.getJob().getRecruiter().getName())
                        .createdAt(saved.getJob().getCreatedAt())
                        .build());
    }
}