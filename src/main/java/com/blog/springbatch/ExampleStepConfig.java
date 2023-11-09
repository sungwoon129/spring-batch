package com.blog.springbatch;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;


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
                .processor(jpaItemProcessor())
                .writer(customItemWriter())
                .build();
    }


    @Bean
    @StepScope
    public JpaPagingItemReader<User> exampleItemReader() {
        return new JpaPagingItemReaderBuilder<User>()
                .name("exampleItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(jobParameter.getChunkSize())
                .queryString("SELECT u FROM User u")
                .build();
    }

    @Bean
    public ItemProcessor<User,User> jpaItemProcessor() {

        return user -> user;
    }

    @Bean
    public ItemWriter<User> customItemWriter() {
        return users -> {
            for (User user : users) {
                System.out.println(user.getId());
            }
        };
    }
}
