package com.blog.springbatch.step;

import com.blog.springbatch.UserDto;
import lombok.Getter;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Component
public class CustomItemWriter implements ItemWriter<UserDto> {

    private final List<UserDto> processedData = new ArrayList<>();


    @Override
    public void write(Chunk<? extends UserDto> chunk) throws Exception {
        processedData.addAll(chunk.getItems());
    }
}
