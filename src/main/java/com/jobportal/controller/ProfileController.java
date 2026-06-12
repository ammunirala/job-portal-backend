package com.jobportal.controller;

import com.jobportal.dto.request.ProfileRequest;
import com.jobportal.dto.response.*;
import com.jobportal.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<ProfileResponse>> getProfile() {
        return ResponseEntity.ok(
                ApiResponse.success("Profile fetched",
                        profileService.getProfile()));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<ProfileResponse>> updateProfile(
            @RequestBody ProfileRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success("Profile updated",
                        profileService.updateProfile(request)));
    }

    @PostMapping("/profile/resume")
    @PreAuthorize("hasRole('JOBSEEKER')")
    public ResponseEntity<ApiResponse<ProfileResponse>> uploadResume(
            @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(
                ApiResponse.success("Resume uploaded",
                        profileService.uploadResume(file)));
    }

    @PostMapping("/jobs/{jobId}/save")
    @PreAuthorize("hasRole('JOBSEEKER')")
    public ResponseEntity<ApiResponse<String>> saveJob(
            @PathVariable Long jobId) {
        return ResponseEntity.ok(
                ApiResponse.success(profileService.saveJob(jobId), null));
    }

    @DeleteMapping("/jobs/{jobId}/save")
    @PreAuthorize("hasRole('JOBSEEKER')")
    public ResponseEntity<ApiResponse<String>> unsaveJob(
            @PathVariable Long jobId) {
        return ResponseEntity.ok(
                ApiResponse.success(profileService.unsaveJob(jobId), null));
    }

    @GetMapping("/jobs/saved")
    @PreAuthorize("hasRole('JOBSEEKER')")
    public ResponseEntity<ApiResponse<Page<JobResponse>>> getSavedJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(
                ApiResponse.success("Saved jobs",
                        profileService.getSavedJobs(page, size)));
    }
}