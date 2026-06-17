package com.jobportal.controller;

import com.jobportal.dto.response.ApiResponse;
import com.jobportal.dto.response.MatchScoreResponse;
import com.jobportal.service.MatchScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/match")
@RequiredArgsConstructor
public class MatchScoreController {

    private final MatchScoreService matchScoreService;

    @GetMapping("/{jobId}")
    @PreAuthorize("hasRole('JOBSEEKER')")
    public ResponseEntity<ApiResponse<MatchScoreResponse>> getMatchScore(
            @PathVariable Long jobId) {
        return ResponseEntity.ok(ApiResponse.success(
                "Match score calculated",
                matchScoreService.getMatchScore(jobId)));
    }
}