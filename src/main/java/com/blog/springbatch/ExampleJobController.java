package com.blog.springbatch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.blog.springbatch.ExampleJobConfig.JOB_NAME;

@RequiredArgsConstructor
@RestController
public class ExampleJobController {

    private final ExampleJobService exampleJobService;

    @GetMapping("/job/example")
    public int doSomething() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        exampleJobService.exampleJob(JOB_NAME, null);

        return 1;
    }
}
