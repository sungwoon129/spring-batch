package com.blog.springbatch;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@RequiredArgsConstructor
@Configuration
public class ExampleStepConfig {

    public static final String STEP_NAME = ExampleJobConfig.JOB_NAME + ".EXAMPLE_STEP";

    private final EntityManagerFactory entityManagerFactory;
    private final ExampleJobParameter jobParameter;

    @Bean(STEP_NAME)
    @JobScope
    public Step exampleStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder(STEP_NAME, jobRepository)
                .<User, User>chunk(jobParameter.getChunkSize(), transactionManager)
                .reader(exampleItemReader())
                .writer(exampleItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<User> exampleItemReader() {
        return new JpaPagingItemReaderBuilder<User>()
                .name("exampleItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(jobParameter.getChunkSize())
                .queryString("SELECT u.user_id,u.nickname,u.reg_date FROM users u")
                .build();
    }

    @Bean
    public ItemWriter<User> exampleItemWriter() {
        return users -> {
            for (User user : users) {
                System.out.println(user);
            }
        };
    }
}
