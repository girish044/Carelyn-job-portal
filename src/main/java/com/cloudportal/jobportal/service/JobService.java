package com.cloudportal.jobportal.service;

import com.cloudportal.jobportal.model.Job;
import com.cloudportal.jobportal.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    private final JobRepository repo;

    public JobService(JobRepository repo) {
        this.repo = repo;
    }

    public List<Job> getAllJobs() {
        return repo.findAll();
    }

    public Job addJob(Job job) {
        return repo.save(job);
    }
}

