package com.cloudportal.jobportal.repository;

import com.cloudportal.jobportal.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByUserEmail(String userEmail);
}