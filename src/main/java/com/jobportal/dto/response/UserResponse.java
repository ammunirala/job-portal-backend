package com.jobportal.dto.response;

import com.jobportal.entity.enums.Role;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private boolean enabled;
}