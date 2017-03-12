package com.jakduk.batch.configuration;

import com.jakduk.batch.processor.AddLastUpdatedToBoardFreeProcessor;
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
 * BoardFree에 lastUpdated 필드를 추가한다.
 *
 * Created by pyohwanjang on 2017. 3. 12..
 */

@Configuration
public class AddLastUpdatedToBoardFreeConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private MongoOperations mongoOperations;

    @Bean
    public Job addLastUpdatedToBoardFree(@Qualifier("addLastUpdatedToBoardFreeStep") Step step) throws Exception {

        return jobBuilderFactory.get("addLastUpdatedToBoardFreeJob")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

    @Bean
    public Step addLastUpdatedToBoardFreeStep() {
        return stepBuilderFactory.get("addLastUpdatedToBoardFreeStep")
                .<BoardFree, BoardFree>chunk(1000)
                .reader(addLastUpdatedToBoardFreeReader())
                .processor(addLastUpdatedToBoardFreeProcessor())
                .writer(addLastUpdatedToBoardFreeWriter())
                .build();
    }

    @Bean
    public ItemReader<BoardFree> addLastUpdatedToBoardFreeReader() {

        String query = String.format("{'batch':{$nin:['%s']}}",
                CoreConst.BATCH_TYPE.ADD_LAST_UPDATED_TO_BOARDFREE_01);

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
    public ItemProcessor<BoardFree, BoardFree> addLastUpdatedToBoardFreeProcessor() {
        return new AddLastUpdatedToBoardFreeProcessor();
    }

    @Bean
    public MongoItemWriter<BoardFree> addLastUpdatedToBoardFreeWriter() {
        MongoItemWriter<BoardFree> writer = new MongoItemWriter<>();
        writer.setTemplate(mongoOperations);

        return writer;
    }

}
