package com.ias.quickfix.service;

import com.ias.quickfix.dto.JobDto;
import com.ias.quickfix.exceptions.JobAlreadyExistsException;
import com.ias.quickfix.model.Job;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class JobService {
    private final ModelMapper modelMapper;
    private final FirebaseService firebaseService;
    private final String JOB_COLLECTION = "jobs";

    // CREATE
    public JobDto saveJob(JobDto jobDto) {
        // Construiește jobId-ul
        String jobId = jobDto.getJobName() + "_" + jobDto.getIndustry();


        // Verifică dacă există deja un job cu acest ID
        if (firebaseService.documentExists(JOB_COLLECTION, jobId)) {
            throw new JobAlreadyExistsException("Job with ID " + jobId + " already exists");
        }

        Job job = modelMapper.map(jobDto, Job.class);
        job.setJobId(jobId);

        // Salvează job-ul în Firestore
        firebaseService.saveObject(JOB_COLLECTION, jobId, job);
        log.info("Saving job: {}", job);

        return modelMapper.map(job, JobDto.class);
    }

    // READ ALL
    public List<JobDto> getAllJobs() {
        // Obține toate documentele din colecția JOB_COLLECTION
        List<Job> jobs = firebaseService.getAllObjects(JOB_COLLECTION, Job.class);

        // Map fiecare entitate Job la un DTO JobDto
        return jobs.stream()
                .map(job -> modelMapper.map(job, JobDto.class))
                .collect(Collectors.toList());
    }

    // READ BY NAME
    public JobDto getJobByName(String jobName) {
        // Filtrare după jobName
        List<Job> jobs = firebaseService.getAllObjects(JOB_COLLECTION, Job.class).stream()
                .filter(job -> job.getJobName().equalsIgnoreCase(jobName))
                .collect(Collectors.toList());

        if (jobs.isEmpty()) {
            throw new RuntimeException("Job with name " + jobName + " not found");
        }

        return modelMapper.map(jobs.get(0), JobDto.class); // Returnează primul rezultat găsit
    }

    // UPDATE
    public JobDto updateJob(String jobId, JobDto jobDto) {
        // Verifică dacă job-ul există
        if (!firebaseService.documentExists(JOB_COLLECTION, jobId)) {
            throw new RuntimeException("Job with ID " + jobId + " does not exist");
        }

        // Mapează DTO-ul în entitate
        Job updatedJob = modelMapper.map(jobDto, Job.class);

        // Calculează noul jobId
        String newJobId = updatedJob.getJobName() + "_" + updatedJob.getIndustry();

        // Dacă noul jobId este diferit de cel vechi, șterge documentul vechi
        if (!newJobId.equals(jobId)) {
            firebaseService.deleteObject(JOB_COLLECTION, jobId);
        }

        // Salvează documentul cu noul jobId
        updatedJob.setJobId(newJobId); // Setează noul ID în entitate
        firebaseService.saveObject(JOB_COLLECTION, newJobId, updatedJob);
        log.info("Updated job: {}", updatedJob);

        // Returnează JobDto mapat
        return modelMapper.map(updatedJob, JobDto.class);
    }

    // DELETE
    public void deleteJob(String jobId) {
        // Verifică dacă job-ul există
        if (!firebaseService.documentExists(JOB_COLLECTION, jobId)) {
            throw new RuntimeException("Job with ID " + jobId + " does not exist");
        }

        // Șterge documentul din Firestore
        firebaseService.deleteObject(JOB_COLLECTION, jobId);
        log.info("Deleted job with ID: {}", jobId);
    }
}

