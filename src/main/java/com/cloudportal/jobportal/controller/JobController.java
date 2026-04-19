package com.cloudportal.jobportal.controller;

import com.cloudportal.jobportal.Application;
import com.cloudportal.jobportal.model.Job;
import com.cloudportal.jobportal.repository.ApplicationRepository;
import com.cloudportal.jobportal.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin
public class JobController {

    private final JobService service;

    @Autowired
    private ApplicationRepository applicationRepository;

    public JobController(JobService service) {
        this.service = service;
    }

    // ── Get all jobs ──
    @GetMapping
    public List<Job> getJobs() {
        return service.getAllJobs();
    }

    // ── Post a new job ──
    @PostMapping
    public Job addJob(@RequestBody Job job) {
        return service.addJob(job);
    }

    // ── Apply for a job ── (New endpoint added)
    @PostMapping("/apply")
    public ResponseEntity<?> applyForJob(@RequestBody ApplyRequest request) {
        Application app = new Application();
        app.setJobId(request.getJobId());
        app.setUserEmail(request.getApplicantEmail());
        app.setApplicantName(request.getApplicantName());
        app.setApplicantPhone(request.getApplicantPhone());
        app.setCoverNote(request.getCoverNote());
        applicationRepository.save(app);
        return ResponseEntity.ok("Application submitted successfully");
    }

    // ── DTO for apply request ──
    public static class ApplyRequest {
        private Long jobId;
        private String applicantName;
        private String applicantPhone;
        private String applicantEmail;
        private String coverNote;

        public Long getJobId() { return jobId; }
        public void setJobId(Long jobId) { this.jobId = jobId; }

        public String getApplicantName() { return applicantName; }
        public void setApplicantName(String applicantName) { this.applicantName = applicantName; }

        public String getApplicantPhone() { return applicantPhone; }
        public void setApplicantPhone(String applicantPhone) { this.applicantPhone = applicantPhone; }

        public String getApplicantEmail() { return applicantEmail; }
        public void setApplicantEmail(String applicantEmail) { this.applicantEmail = applicantEmail; }

        public String getCoverNote() { return coverNote; }
        public void setCoverNote(String coverNote) { this.coverNote = coverNote; }
    }
}