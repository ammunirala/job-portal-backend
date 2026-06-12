package com.jobportal.dto.response;

import com.jobportal.entity.enums.Role;
import lombok.Getter;

@Getter
public class AuthResponse {
    private String token;
    private String name;
    private String email;
    private Role role;

    public AuthResponse() {}

    public AuthResponse(String token, String name, String email, Role role) {
        this.token = token;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public static AuthResponseBuilder builder() { return new AuthResponseBuilder(); }

    public static class AuthResponseBuilder {
        private String token, name, email;
        private Role role;

        public AuthResponseBuilder token(String t) { this.token = t; return this; }
        public AuthResponseBuilder name(String n) { this.name = n; return this; }
        public AuthResponseBuilder email(String e) { this.email = e; return this; }
        public AuthResponseBuilder role(Role r) { this.role = r; return this; }

        public AuthResponse build() {
            return new AuthResponse(token, name, email, role);
        }
    }
}