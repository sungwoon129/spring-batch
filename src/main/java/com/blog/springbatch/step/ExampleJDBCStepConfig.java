package com.blog.springbatch.step;

import com.blog.springbatch.ExampleJobConfig;
import com.blog.springbatch.ExampleJobParameter;
import com.blog.springbatch.User;
import com.blog.springbatch.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@RequiredArgsConstructor
@Configuration
public class ExampleJDBCStepConfig {

    public static final String STEP_NAME = ExampleJobConfig.JOB_NAME_JDBC + ".EXAMPLE_STEP";
    private final int chunkSize = 1000;
    private final DataSource dataSource;


    @Bean(STEP_NAME)
    @JobScope
    public Step exampleStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder(STEP_NAME, jobRepository)
                .<UserDto, UserDto>chunk(chunkSize, transactionManager)
                .reader(jdbcBatchItemWriterReader())
                .writer(jdbcBatchItemWriter())
                .build();
    }

    @Bean
    public JdbcCursorItemReader<UserDto> jdbcBatchItemWriterReader() {
        return new JdbcCursorItemReaderBuilder<UserDto>()
                .fetchSize(chunkSize)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(UserDto.class))
                .sql("SELECT * FROM users u")
                .name("jdbcBatchItemWriter")
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<UserDto> jdbcBatchItemWriter() {
        return new JdbcBatchItemWriterBuilder<UserDto>()
                .dataSource(dataSource)
                .sql("insert into users2(email, regDate, nickname) values (:email, :regDate, :nickname)")
                .beanMapped()
                .build();
    }
}
