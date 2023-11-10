package com.blog.springbatch.controller;

import com.blog.springbatch.service.ExampleJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.blog.springbatch.job.ExampleJobConfig.JOB_NAME;
import static com.blog.springbatch.job.ExampleJobConfig.JOB_NAME_JDBC;

@RequiredArgsConstructor
@RestController
public class ExampleJobController {

    private final ExampleJobService exampleJobService;

    @PostMapping("/job/example/jdbc")
    public int doSomethingByJdbc() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        exampleJobService.exampleJobByJdbc(JOB_NAME_JDBC);

        return 1;
    }

    @GetMapping("/job/example/orm")
    public int doSomethingByJPA() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        exampleJobService.exampleJobByJPA(JOB_NAME, null);

        return 1;
    }
}
