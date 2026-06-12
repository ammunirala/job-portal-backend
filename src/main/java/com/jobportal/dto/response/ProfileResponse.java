package com.jobportal.dto.response;

import lombok.Getter;

@Getter
public class ProfileResponse {
    private Long id;
    private String name;
    private String email;
    private String bio;
    private String skills;
    private String resumeUrl;
    private String linkedinUrl;
    private Integer experienceYears;

    public ProfileResponse() {}

    public static ProfileResponseBuilder builder() {
        return new ProfileResponseBuilder();
    }

    public static class ProfileResponseBuilder {
        private Long id;
        private String name, email, bio, skills, resumeUrl, linkedinUrl;
        private Integer experienceYears;

        public ProfileResponseBuilder id(Long i) { this.id = i; return this; }
        public ProfileResponseBuilder name(String n) { this.name = n; return this; }
        public ProfileResponseBuilder email(String e) { this.email = e; return this; }
        public ProfileResponseBuilder bio(String b) { this.bio = b; return this; }
        public ProfileResponseBuilder skills(String s) { this.skills = s; return this; }
        public ProfileResponseBuilder resumeUrl(String r) { this.resumeUrl = r; return this; }
        public ProfileResponseBuilder linkedinUrl(String l) { this.linkedinUrl = l; return this; }
        public ProfileResponseBuilder experienceYears(Integer e) { this.experienceYears = e; return this; }

        public ProfileResponse build() {
            ProfileResponse p = new ProfileResponse();
            p.id = id; p.name = name; p.email = email;
            p.bio = bio; p.skills = skills; p.resumeUrl = resumeUrl;
            p.linkedinUrl = linkedinUrl; p.experienceYears = experienceYears;
            return p;
        }
    }
}