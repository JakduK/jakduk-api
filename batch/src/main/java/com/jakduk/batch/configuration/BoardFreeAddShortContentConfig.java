package com.jakduk.batch.configuration;

import com.jakduk.batch.processor.BoardFreeAddShortContentProcessor;
import com.jakduk.core.common.CoreConst;
import com.jakduk.core.model.db.BoardFree;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;

import java.util.HashMap;
import java.util.Map;

/**
 * 본문 미리보기 용으로, HTML이 제거된 100자 정도의 본문 요약 필드가 필요하다
 *
 * Created by pyohwanjang on 2017. 3. 2..
 */

@Configuration
public class BoardFreeAddShortContentConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private MongoOperations mongoOperations;

    @Bean
    public Job boardFreeAddShortContentJob(@Qualifier("boardFreeAddShortContentStep") Step step) {

        return jobBuilderFactory.get("boardFreeAddShortContentJob")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

    @Bean
    public Step boardFreeAddShortContentStep() {
        return stepBuilderFactory.get("boardFreeAddShortContentStep")
                .<BoardFree, BoardFree>chunk(1000)
                .reader(boardFreeAddShortContentReader())
                .processor(boardFreeAddShortContentProcessor())
                .writer(boardFreeAddShortContentWriter())
                .build();
    }

    @Bean
    public ItemReader<BoardFree> boardFreeAddShortContentReader() {

        String query = String.format("{'batch':{$nin:['%s']}}",
                CoreConst.BATCH_TYPE.BOARD_FREE_ADD_SHORT_CONTENT_01);

        MongoItemReader<BoardFree> itemReader = new MongoItemReader<>();
        itemReader.setTemplate(mongoOperations);
        itemReader.setTargetType(BoardFree.class);
        itemReader.setPageSize(500);
        itemReader.setQuery(query);
        Map<String, Sort.Direction> sorts = new HashMap<>();
        sorts.put("id", Sort.Direction.ASC);
        itemReader.setSort(sorts);

        return itemReader;
    }

    @Bean
    public ItemProcessor<BoardFree, BoardFree> boardFreeAddShortContentProcessor() {
        return new BoardFreeAddShortContentProcessor();
    }

    @Bean
    public MongoItemWriter<BoardFree> boardFreeAddShortContentWriter() {
        MongoItemWriter<BoardFree> writer = new MongoItemWriter<>();
        writer.setTemplate(mongoOperations);

        return writer;
    }
}
