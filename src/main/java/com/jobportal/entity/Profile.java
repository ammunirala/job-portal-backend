package com.jobportal.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(columnDefinition = "TEXT")
    private String bio;

    private String skills;
    private String resumeUrl;
    private String linkedinUrl;
    private Integer experienceYears;

    public Profile() {}

    public static ProfileBuilder builder() { return new ProfileBuilder(); }

    public static class ProfileBuilder {
        private User user;
        private String bio, skills, resumeUrl, linkedinUrl;
        private Integer experienceYears;

        public ProfileBuilder user(User u) { this.user = u; return this; }
        public ProfileBuilder bio(String b) { this.bio = b; return this; }
        public ProfileBuilder skills(String s) { this.skills = s; return this; }
        public ProfileBuilder resumeUrl(String r) { this.resumeUrl = r; return this; }
        public ProfileBuilder linkedinUrl(String l) { this.linkedinUrl = l; return this; }
        public ProfileBuilder experienceYears(Integer e) { this.experienceYears = e; return this; }

        public Profile build() {
            Profile p = new Profile();
            p.user = this.user;
            p.bio = this.bio;
            p.skills = this.skills;
            p.resumeUrl = this.resumeUrl;
            p.linkedinUrl = this.linkedinUrl;
            p.experienceYears = this.experienceYears;
            return p;
        }
    }
}