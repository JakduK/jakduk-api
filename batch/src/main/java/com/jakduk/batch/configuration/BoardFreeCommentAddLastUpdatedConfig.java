package com.jakduk.batch.configuration;

import com.jakduk.batch.processor.BoardFreeCommentAddLastUpdatedProcessor;
import com.jakduk.core.common.CoreConst;
import com.jakduk.core.model.db.BoardFreeComment;
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
 * BoardFree에 lastUpdated 필드를 추가한다.
 *
 * Created by pyohwanjang on 2017. 3. 12..
 */

@Configuration
public class BoardFreeCommentAddLastUpdatedConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private MongoOperations mongoOperations;

    @Bean
    public Job boardFreeAddLastUpdatedJob(@Qualifier("boardFreeCommentAddLastUpdatedStep") Step step) {

        return jobBuilderFactory.get("boardFreeCommentAddLastUpdatedJob")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

    @Bean
    public Step boardFreeCommentAddLastUpdatedStep() {
        return stepBuilderFactory.get("boardFreeCommentAddLastUpdatedStep")
                .<BoardFreeComment, BoardFreeComment>chunk(1000)
                .reader(boardFreeCommentAddLastUpdatedReader())
                .processor(boardFreeCommentAddLastUpdatedProcessor())
                .writer(boardFreeCommentAddLastUpdatedWriter())
                .build();
    }

    @Bean
    public ItemReader<BoardFreeComment> boardFreeCommentAddLastUpdatedReader() {

        String query = String.format("{'batch':{$nin:['%s']}}",
                CoreConst.BATCH_TYPE.BOARD_FREE_COMMENT_ADD_LAST_UPDATED_01);

        MongoItemReader<BoardFreeComment> itemReader = new MongoItemReader<>();
        itemReader.setTemplate(mongoOperations);
        itemReader.setTargetType(BoardFreeComment.class);
        itemReader.setPageSize(1000);
        itemReader.setQuery(query);
        Map<String, Sort.Direction> sorts = new HashMap<>();
        sorts.put("id", Sort.Direction.ASC);
        itemReader.setSort(sorts);

        return itemReader;
    }

    @Bean
    public ItemProcessor<BoardFreeComment, BoardFreeComment> boardFreeCommentAddLastUpdatedProcessor() {
        return new BoardFreeCommentAddLastUpdatedProcessor();
    }

    @Bean
    public MongoItemWriter<BoardFreeComment> boardFreeCommentAddLastUpdatedWriter() {
        MongoItemWriter<BoardFreeComment> writer = new MongoItemWriter<>();
        writer.setTemplate(mongoOperations);

        return writer;
    }

}
