package com.jobportal.controller;

import com.jobportal.dto.response.*;
import com.jobportal.service.AdminService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<AdminStatusResponse>> getStats() {
        return ResponseEntity.ok(
                ApiResponse.success("Stats fetched",
                        adminService.getStats()));
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(
                ApiResponse.success("Users fetched",
                        adminService.getAllUsers(page, size)));
    }

    @PutMapping("/users/{id}/ban")
    public ResponseEntity<ApiResponse<Void>> banUser(@PathVariable Long id) {
        adminService.banUser(id);
        return ResponseEntity.ok(ApiResponse.success("User banned", null));
    }

    @PutMapping("/users/{id}/unban")
    public ResponseEntity<ApiResponse<Void>> unbanUser(@PathVariable Long id) {
        adminService.unbanUser(id);
        return ResponseEntity.ok(ApiResponse.success("User unbanned", null));
    }
}