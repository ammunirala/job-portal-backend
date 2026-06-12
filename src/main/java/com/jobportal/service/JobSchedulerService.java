package com.jobportal.service;

import com.jobportal.entity.enums.JobStatus;
import com.jobportal.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import com.jobportal.entity.Job;

@Service
@RequiredArgsConstructor
public class JobSchedulerService {

    private final JobRepository jobRepository;

    // Har roz midnight ko run hoga
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void expireOldJobs() {
        List<Job> activeJobs = jobRepository
                .findByStatus(JobStatus.ACTIVE,
                        org.springframework.data.domain.Pageable.unpaged())
                .getContent();

        LocalDate today = LocalDate.now();
        int count = 0;

        for (Job job : activeJobs) {
            if (job.getDeadline() != null &&
                    job.getDeadline().isBefore(today)) {
                job.setStatus(JobStatus.EXPIRED);
                jobRepository.save(job);
                count++;
            }
        }

        System.out.println("Expired " + count + " jobs on " + today);
    }
}