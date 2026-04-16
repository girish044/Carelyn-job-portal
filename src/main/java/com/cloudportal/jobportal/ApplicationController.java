package com.cloudportal.jobportal;

import com.cloudportal.jobportal.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "*")
public class ApplicationController {

    @Autowired
    private ApplicationRepository applicationRepository;

    @PostMapping
    public Application applyJob(@RequestBody Application application) {
        return applicationRepository.save(application);
    }

    @GetMapping
    public List<Application> getApplications(@RequestParam String userEmail) {
        return applicationRepository.findByUserEmail(userEmail);
    }
}