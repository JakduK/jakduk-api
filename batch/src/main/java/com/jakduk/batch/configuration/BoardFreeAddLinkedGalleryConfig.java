package com.jakduk.batch.configuration;

import com.jakduk.batch.processor.BoardFreeAddLinkedGalleryProcessor;
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
 * galleries 필드 대신 linkedGallery 필드를 추가한다.
 *
 * Created by pyohwanjang on 2017. 4. 15..
 */

@Configuration
public class BoardFreeAddLinkedGalleryConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private MongoOperations mongoOperations;

    @Bean
    public Job boardFreeAddLinkedGalleryJob(@Qualifier("boardFreeAddLinkedGalleryStep") Step step) {

        return jobBuilderFactory.get("boardFreeAddLinkedGalleryJob")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

    @Bean
    public Step boardFreeAddLinkedGalleryStep() {
        return stepBuilderFactory.get("boardFreeAddLinkedGalleryStep")
                .<BoardFree, BoardFree>chunk(1000)
                .reader(boardFreeAddLinkedGalleryReader())
                .processor(boardFreeAddLinkedGalleryProcessor())
                .writer(boardFreeAddLinkedGalleryWriter())
                .build();
    }

    @Bean
    public ItemReader<BoardFree> boardFreeAddLinkedGalleryReader() {

        String query = String.format("{'batch':{$nin:['%s']}}",
                CoreConst.BATCH_TYPE.BOARD_FREE_ADD_LINKED_GALLERY_01);

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
    public ItemProcessor<BoardFree, BoardFree> boardFreeAddLinkedGalleryProcessor() {
        return new BoardFreeAddLinkedGalleryProcessor();
    }

    @Bean
    public MongoItemWriter<BoardFree> boardFreeAddLinkedGalleryWriter() {
        MongoItemWriter<BoardFree> writer = new MongoItemWriter<>();
        writer.setTemplate(mongoOperations);

        return writer;
    }

}
