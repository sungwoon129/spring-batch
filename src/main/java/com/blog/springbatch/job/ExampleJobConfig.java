package com.blog.springbatch.job;

import com.blog.springbatch.step.ExampleStepConfig;
import com.blog.springbatch.step.LoggingStep;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@EnableBatchProcessing
@Configuration
public class ExampleJobConfig {

    public static final String JOB_NAME = "EXAMPLE_JOB";
    public static final String JOB_NAME_JDBC = "EXAMPLE_JOB_JDBC";
    private final Step exampleStep;
    private final Step loggingStep;


    public ExampleJobConfig(
            @Qualifier(ExampleStepConfig.STEP_NAME) Step exampleStep, @Qualifier(LoggingStep.STEP_NAME)Step loggingStep) {
        this.exampleStep = exampleStep;
        this.loggingStep = loggingStep;
    }

    @Bean(JOB_NAME)
    public Job exampleJob(JobRepository jobRepository) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(exampleStep)
                .next(loggingStep)
                .build();
    }





}
