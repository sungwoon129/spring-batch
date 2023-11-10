package com.blog.springbatch.service;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ExampleJobService {


    private final ApplicationContext context;
    private final JobLauncher jobLauncher;

    public void exampleJobByJPA(String jobName, JobParameters jobParameters) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        Job jobToStart = context.getBean(jobName, Job.class);

        Map<String, JobParameter<?>> paramMap = new HashMap<>();

        LocalDate today = LocalDate.now();
        String todayStr = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        JobParameter<String> jobParameter1 = new JobParameter<>(todayStr, String.class);
        JobParameter<Integer> jobParameter2 = new JobParameter<>(100, Integer.class);

        paramMap.put("date", jobParameter1);
        paramMap.put("chunkSize", jobParameter2);

        JobParameters params = new JobParameters(paramMap);

        jobLauncher.run(jobToStart, params);
    }

    public void exampleJobByJdbc(String jobName) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        Job jobToStart = context.getBean(jobName, Job.class);

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", String.valueOf(System.currentTimeMillis()))
                .addString("chunkSize", "100")
                .toJobParameters();


        jobLauncher.run(jobToStart, jobParameters);
    }
}
