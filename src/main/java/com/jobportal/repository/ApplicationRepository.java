package com.jobportal.repository;

import com.jobportal.entity.Application;
import com.jobportal.entity.Job;
import com.jobportal.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {


    Page<Application> findByApplicant(User applicant, Pageable pageable);


    Page<Application> findByJob(Job job, Pageable pageable);


    boolean existsByJobAndApplicant(Job job, User applicant);
}