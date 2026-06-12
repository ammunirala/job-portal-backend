package com.jobportal.service;

import com.jobportal.dto.response.AdminStatusResponse;
import com.jobportal.dto.response.UserResponse;
import com.jobportal.entity.User;
import com.jobportal.entity.enums.JobStatus;
import com.jobportal.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;

    public AdminStatusResponse getStats() {
        return AdminStatusResponse.builder()
                .totalUsers(userRepository.count())
                .totalJobs(jobRepository.count())
                .totalApplications(applicationRepository.count())
                .activeJobs(jobRepository
                        .findByStatus(JobStatus.ACTIVE, Pageable.unpaged())
                        .getTotalElements())
                .expiredJobs(jobRepository
                        .findByStatus(JobStatus.EXPIRED, Pageable.unpaged())
                        .getTotalElements())
                .build();
    }

    public Page<UserResponse> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by("id").descending());
        return userRepository.findAll(pageable)
                .map(this::toUserResponse);
    }

    public void banUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setEnabled(false);
        userRepository.save(user);
    }

    public void unbanUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);
    }

    private UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .enabled(user.isEnabled())
                .build();
    }
}