package com.jobportal.repository;

import com.jobportal.entity.Profile;
import com.jobportal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUser(User user);
}