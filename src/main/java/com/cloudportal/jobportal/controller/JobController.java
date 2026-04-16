package com.cloudportal.jobportal.controller;

import com.cloudportal.jobportal.model.Job;
import com.cloudportal.jobportal.service.JobService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin
public class JobController {

    private final JobService service;

    public JobController(JobService service) {
        this.service = service;
    }

    @GetMapping
    public List<Job> getJobs() {
        return service.getAllJobs();
    }

    @PostMapping
    public Job addJob(@RequestBody Job job) {
        return service.addJob(job);
    }
}
