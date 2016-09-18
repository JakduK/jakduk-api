package com.jakduk.batch;

import com.jakduk.core.service.SearchService;
import org.elasticsearch.index.IndexNotFoundException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by pyohwan on 16. 9. 18.
 */

@Configuration
public class InitElasticsearchIndexBatch {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private SearchService searchService;

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("initElasticsearchIndexStep")
                .tasklet((contribution, chunkContext) -> {

                    try {
                        searchService.deleteIndex();
                    } catch (IndexNotFoundException e) {
                    }

                    searchService.initSearchIndex();
                    searchService.initSearchType();
                    searchService.initSearchDocuments();

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Job job(Step step1) throws Exception {
        return jobBuilderFactory.get("initElasticsearchIndexJob")
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .build();
    }
}
