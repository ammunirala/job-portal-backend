package com.jobportal.controller;

import com.jobportal.dto.request.ApplicationRequest;
import com.jobportal.dto.request.StatusUpdateRequest;
import com.jobportal.dto.response.ApiResponse;
import com.jobportal.dto.response.ApplicationResponse;
import com.jobportal.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    // Jobseeker — job ke liye apply karo
    @PostMapping("/jobs/{jobId}/apply")
    @PreAuthorize("hasRole('JOBSEEKER')")
    public ResponseEntity<ApiResponse<ApplicationResponse>> apply(
            @PathVariable Long jobId,
            @RequestBody(required = false) ApplicationRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                "Applied successfully",
                applicationService.applyForJob(jobId, request)));
    }

    // Jobseeker — apni applications dekho
    @GetMapping("/applications/my")
    @PreAuthorize("hasRole('JOBSEEKER')")
    public ResponseEntity<ApiResponse<Page<ApplicationResponse>>> myApplications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(
                "Your applications",
                applicationService.getMyApplications(page, size)));
    }

    // Recruiter — job ke applicants dekho
    @GetMapping("/jobs/{jobId}/applicants")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<ApiResponse<Page<ApplicationResponse>>> getApplicants(
            @PathVariable Long jobId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(
                "Applicants list",
                applicationService.getApplicantsForJob(jobId, page, size)));
    }

    // Recruiter — application status update karo
    @PutMapping("/applications/{applicationId}/status")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<ApiResponse<ApplicationResponse>> updateStatus(
            @PathVariable Long applicationId,
            @Valid @RequestBody StatusUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                "Status updated",
                applicationService.updateStatus(applicationId, request)));
    }
}