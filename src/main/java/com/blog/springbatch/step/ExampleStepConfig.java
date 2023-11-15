package com.blog.springbatch.step;

import com.blog.springbatch.UserDto;
import com.blog.springbatch.job.ExampleJobConfig;
import com.blog.springbatch.listener.CustomStepListener.CustomStepListener;
import com.blog.springbatch.parameter.ExampleJobParameter;
import com.blog.springbatch.User;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
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
    private final CustomItemWriter customItemWriter;

    @Bean(STEP_NAME)
    @JobScope
    public Step exampleStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder(STEP_NAME, jobRepository)
                .<User, UserDto>chunk(jobParameter.getChunkSize(), transactionManager)
                .reader(exampleItemReader())
                .processor(jpaItemProcessor())
                .writer(customItemWriter)
                .listener(promotionListener())
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
    public ItemProcessor<User,UserDto> jpaItemProcessor() {

        return user -> new UserDto(user.getId(), user.getEmail(), user.getNickname(), user.getPassword(), user.getRegDate());
    }

    @Bean
    public ExecutionContextPromotionListener promotionListener() {
        ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();
        listener.setKeys(new String[]{"processData"}); // 자동으로 승격시킬 키 목록
        return listener;
    }

}
