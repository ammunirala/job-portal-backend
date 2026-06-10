package com.jobportal.service;

import com.jobportal.dto.request.ApplicationRequest;
import com.jobportal.dto.request.StatusUpdateRequest;
import com.jobportal.dto.response.ApplicationResponse;
import com.jobportal.entity.*;
import com.jobportal.entity.enums.ApplicationStatus;
import com.jobportal.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private ApplicationResponse toResponse(Application app) {
        return ApplicationResponse.builder()
                .id(app.getId())
                .jobId(app.getJob().getId())
                .jobTitle(app.getJob().getTitle())
                .companyLocation(app.getJob().getLocation())
                .applicantName(app.getApplicant().getName())
                .applicantEmail(app.getApplicant().getEmail())
                .status(app.getStatus())
                .coverLetter(app.getCoverLetter())
                .appliedAt(app.getAppliedAt())
                .build();
    }

    // Jobseeker applies for a job
    public ApplicationResponse applyForJob(Long jobId,
                                           ApplicationRequest request) {
        User applicant = getCurrentUser();

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        // Duplicate apply check
        if (applicationRepository.existsByJobAndApplicant(job, applicant)) {
            throw new RuntimeException("You have already applied for this job");
        }

        // Expired job check
        if (job.getStatus().name().equals("EXPIRED")) {
            throw new RuntimeException("This job is no longer accepting applications");
        }

        Application application = Application.builder()
                .job(job)
                .applicant(applicant)
                .status(ApplicationStatus.APPLIED)
                .coverLetter(request != null ? request.getCoverLetter() : null)
                .build();

        return toResponse(applicationRepository.save(application));
    }

    // Jobseeker apni saari applications dekhe
    public Page<ApplicationResponse> getMyApplications(int page, int size) {
        User applicant = getCurrentUser();
        Pageable pageable = PageRequest.of(page, size,
                Sort.by("appliedAt").descending());
        return applicationRepository.findByApplicant(applicant, pageable)
                .map(this::toResponse);
    }

    // Recruiter ek job ke saare applicants dekhe
    public Page<ApplicationResponse> getApplicantsForJob(Long jobId,
                                                         int page, int size) {
        User recruiter = getCurrentUser();

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        // Sirf apni job ke applicants dekh sakta hai
        if (!job.getRecruiter().getId().equals(recruiter.getId())) {
            throw new RuntimeException("Access denied");
        }

        Pageable pageable = PageRequest.of(page, size,
                Sort.by("appliedAt").descending());
        return applicationRepository.findByJob(job, pageable)
                .map(this::toResponse);
    }

    // Recruiter application status update kare
    public ApplicationResponse updateStatus(Long applicationId,
                                            StatusUpdateRequest request) {
        User recruiter = getCurrentUser();

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        // Sirf apni job ka status update kar sakta hai
        if (!application.getJob().getRecruiter().getId()
                .equals(recruiter.getId())) {
            throw new RuntimeException("Access denied");
        }

        application.setStatus(request.getStatus());
        return toResponse(applicationRepository.save(application));
    }
}