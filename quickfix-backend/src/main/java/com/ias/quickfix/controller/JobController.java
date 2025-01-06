package com.ias.quickfix.controller;

import com.ias.quickfix.dto.JobDto;
import com.ias.quickfix.exceptions.JobAlreadyExistsException;
import com.ias.quickfix.service.JobService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jobs")
@Slf4j
@AllArgsConstructor
public class JobController {

    private final JobService jobService;

    // CREATE
    @PostMapping
    public ResponseEntity<?> saveJob(@RequestBody JobDto jobDto) {
        try {
            return ResponseEntity.ok(jobService.saveJob(jobDto));
        } catch (JobAlreadyExistsException e) {
            log.error("Job already exists: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage(), "status", HttpStatus.CONFLICT.value()));
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred");
        }
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<?> getAllJobs() {
        try {
            List<JobDto> jobs = jobService.getAllJobs();
            return ResponseEntity.ok(jobs);
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred");
        }
    }

    // READ BY NAME
    @GetMapping("/{jobName}")
    public ResponseEntity<?> getJobByName(@PathVariable String jobName) {
        try {
            JobDto job = jobService.getJobByName(jobName);
            return ResponseEntity.ok(job);
        } catch (RuntimeException e) { // Customize exception handling as needed
            log.error("Job not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage(), "status", HttpStatus.NOT_FOUND.value()));
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred");
        }
    }

    // UPDATE
    @PutMapping("/{jobId}")
    public ResponseEntity<?> updateJob(@PathVariable String jobId, @RequestBody JobDto jobDto) {
        try {
            return ResponseEntity.ok(jobService.updateJob(jobId, jobDto));
        } catch (RuntimeException e) {
            log.error("Error updating job: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage(), "status", HttpStatus.NOT_FOUND.value()));
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred");
        }
    }

    // DELETE
    @DeleteMapping("/{jobId}")
    public ResponseEntity<?> deleteJob(@PathVariable String jobId) {
        try {
            jobService.deleteJob(jobId);
            return ResponseEntity.ok(Map.of("message", "Job deleted successfully"));
        } catch (RuntimeException e) {
            log.error("Error deleting job: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage(), "status", HttpStatus.NOT_FOUND.value()));
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred");
        }
    }
}

