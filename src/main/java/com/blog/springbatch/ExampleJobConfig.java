package com.blog.springbatch;

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
    private final Step exampleStep;


    public ExampleJobConfig(
            @Qualifier(ExampleStepConfig.STEP_NAME) Step exampleStep) {
        this.exampleStep = exampleStep;

    }

    @Bean(JOB_NAME)
    public Job exampleJob(JobRepository jobRepository) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(exampleStep)
                .build();
    }



}
