package com.jakduk.batch.configuration;

import com.jakduk.core.service.SearchService;
import org.elasticsearch.index.IndexNotFoundException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by pyohwan on 16. 9. 18.
 */

@Configuration
public class InitElasticsearchIndexConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private SearchService searchService;

    @Bean
    public Job initElasticsearchIndexJob(@Qualifier("deleteIndexStep") Step deleteIndexStep,
                                         @Qualifier("initSearchIndexStep") Step initSearchIndexStep,
                                         @Qualifier("initSearchTypeStep") Step initSearchTypeStep,
                                         @Qualifier("initSearchDocumentsStep") Step initSearchDocumentsStep) throws Exception {

        return jobBuilderFactory.get("initElasticsearchIndexJob")
                .incrementer(new RunIdIncrementer())
                .start(deleteIndexStep)
                .next(initSearchIndexStep)
                .next(initSearchTypeStep)
                .next(initSearchDocumentsStep)
                .build();
    }

    @Bean
    public Step deleteIndexStep() {
        return stepBuilderFactory.get("deleteIndexStep")
                .tasklet((contribution, chunkContext) -> {

                    try {
                        searchService.deleteIndex();
                    } catch (IndexNotFoundException e) {
                    }

                    return RepeatStatus.FINISHED;
                })
                .build();

    }

    @Bean
    public Step initSearchIndexStep() {
        return stepBuilderFactory.get("initSearchIndexStep")
                .tasklet((contribution, chunkContext) -> {
                    searchService.initSearchIndex();

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step initSearchTypeStep() {
        return stepBuilderFactory.get("initSearchTypeStep")
                .tasklet((contribution, chunkContext) -> {
                    searchService.initSearchType();

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step initSearchDocumentsStep() {
        return stepBuilderFactory.get("initSearchDocumentsStep")
                .tasklet((contribution, chunkContext) -> {
                    searchService.initSearchDocuments();

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}
