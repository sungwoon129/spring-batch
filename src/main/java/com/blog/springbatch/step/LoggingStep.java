package com.blog.springbatch.step;

import com.blog.springbatch.User;
import com.blog.springbatch.UserDto;
import com.blog.springbatch.job.ExampleJobConfig;
import com.blog.springbatch.listener.CustomStepListener.CustomStepListener;
import com.blog.springbatch.parameter.ExampleJobParameter;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Configuration
public class LoggingStep {

    public static final String STEP_NAME = ExampleJobConfig.JOB_NAME + ".Logging_STEP";

    private final EntityManagerFactory entityManagerFactory;
    private final ExampleJobParameter jobParameter;
    private final CustomStepListener customStepListener;

    @Bean(STEP_NAME)
    @JobScope
    public Step exampleStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder(STEP_NAME, jobRepository)
                .<UserDto, UserDto>chunk(jobParameter.getChunkSize(), transactionManager)
                .reader(customItemReader())
                .writer(customItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<UserDto> customItemReader(JobExplorer jobExplorer) {

        List<JobInstance> jobInstances = jobExplorer.findJobInstancesByJobName(ExampleJobConfig.JOB_NAME, 0, 1);

        if (!jobInstances.isEmpty()) {
            JobInstance jobInstance = jobInstances.get(0);

            // 실행 중인 JobInstances 중에서 최근의 JobExecution을 얻음
            List<JobExecution> jobExecutions = jobExplorer.getJobExecutions(jobInstance);
            JobExecution jobExecution = jobExecutions.get(0);

            // JobExecution에서 JobExecutionContext를 얻어옴
            ExecutionContext jobContext = jobExecution.getExecutionContext();

            List<UserDto> processedData = customStepListener.();
            return new CustomItemReader(processedData);
        } else {
            // 처리할 JobInstance가 없는 경우 예외 처리 또는 기본 값 등을 반환
            return null;
        }
    }

    @Bean
    public ItemProcessor<UserDto,UserDto> jpaItemProcessor() {

        return user -> user;
    }

    @Bean
    public ItemWriter<UserDto> customItemWriter() {
        return users -> {
            for (UserDto user : users) {
                System.out.println(user.getUser_id());
            }
        };
    }
}
