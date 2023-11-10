package com.blog.springbatch;

import com.blog.springbatch.step.ExampleJDBCStepConfig;
import com.blog.springbatch.step.ExampleStepConfig;
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
    private final Step exampleJDBCStep;


    public ExampleJobConfig(
            @Qualifier(ExampleStepConfig.STEP_NAME) Step exampleStep, @Qualifier(ExampleJDBCStepConfig.STEP_NAME)Step exampleJDBCStepConfig) {
        this.exampleStep = exampleStep;
        this.exampleJDBCStep = exampleJDBCStepConfig;

    }

    @Bean(JOB_NAME)
    public Job exampleJob(JobRepository jobRepository) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(exampleStep)
                .build();
    }

    @Bean(JOB_NAME_JDBC)
    public Job exampleJob_Jdbc(JobRepository jobRepository) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(exampleJDBCStep)
                .build();
    }



}
