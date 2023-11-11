package com.blog.springbatch.listener.CustomStepListener;

import com.blog.springbatch.step.CustomItemWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CustomStepListener implements StepExecutionListener {

    private final CustomItemWriter customItemWriter;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        // Step 실행 전에 처리
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        // Step 실행 후에 처리
        ExecutionContext jobContext = stepExecution.getJobExecution().getExecutionContext();

        jobContext.put("processedData", customItemWriter.getProcessedData());
        return null;
    }
}
