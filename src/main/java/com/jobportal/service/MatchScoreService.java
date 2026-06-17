package com.jobportal.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobportal.dto.response.MatchScoreResponse;
import com.jobportal.entity.Job;
import com.jobportal.entity.Profile;
import com.jobportal.entity.User;
import com.jobportal.repository.JobRepository;
import com.jobportal.repository.ProfileRepository;
import com.jobportal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MatchScoreService {

    private final GeminiService geminiService;
    private final JobRepository jobRepository;
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final ObjectMapper mapper = new ObjectMapper();

    public MatchScoreResponse getMatchScore(Long jobId) {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        Profile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Please complete your profile first"));

        String skills = profile.getSkills() != null ? profile.getSkills() : "Not specified";
        String bio = profile.getBio() != null ? profile.getBio() : "";

        String prompt = String.format("""
            You are a resume matching assistant. Compare the candidate's profile with the job description.

            JOB TITLE: %s
            JOB DESCRIPTION: %s

            CANDIDATE SKILLS: %s
            CANDIDATE BIO: %s

            Respond ONLY in this exact JSON format, no markdown, no extra text:
            {
              "matchPercentage": <number 0-100>,
              "matchingSkills": ["skill1", "skill2"],
              "missingSkills": ["skill1", "skill2"],
              "feedback": "<2 sentence feedback in friendly tone>"
            }
            """, job.getTitle(), job.getDescription(), skills, bio);

        String aiResponse = geminiService.generateContent(prompt);

        try {
            String cleanJson = aiResponse.replaceAll("```json", "").replaceAll("```", "").trim();
            JsonNode node = mapper.readTree(cleanJson);

            List<String> matching = new ArrayList<>();
            node.path("matchingSkills").forEach(n -> matching.add(n.asText()));

            List<String> missing = new ArrayList<>();
            node.path("missingSkills").forEach(n -> missing.add(n.asText()));

            return MatchScoreResponse.builder()
                    .matchPercentage(node.path("matchPercentage").asInt())
                    .matchingSkills(matching)
                    .missingSkills(missing)
                    .feedback(node.path("feedback").asText())
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI response");
        }
    }
}