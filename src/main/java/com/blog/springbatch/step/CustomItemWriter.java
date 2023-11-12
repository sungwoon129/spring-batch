package com.blog.springbatch.step;

import com.blog.springbatch.UserDto;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class CustomItemWriter implements ItemWriter<UserDto> {
    private StepExecution stepExecution;

    @Override
    public void write(Chunk<? extends UserDto> chunk) throws Exception {

        ExecutionContext stepContext = stepExecution.getExecutionContext();
        stepContext.put("processedData", chunk.getItems());
    }

    @BeforeStep
    public void saveStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }
}
