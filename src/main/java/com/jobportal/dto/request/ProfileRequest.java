package com.jobportal.dto.request;

import lombok.Data;

@Data
public class ProfileRequest {
    private String bio;
    private String skills;
    private String linkedinUrl;
    private Integer experienceYears;
}