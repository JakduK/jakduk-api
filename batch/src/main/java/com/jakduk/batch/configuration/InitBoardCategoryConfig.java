package com.jakduk.batch.configuration;

import com.jakduk.core.service.BoardCategoryService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 게시판 말머리를 초기화 한다.
 *
 * Created by pyohwan on 17. 1. 4.
 */

@Configuration
public class InitBoardCategoryConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private BoardCategoryService boardCategoryService;

    @Bean
    public Job InitBoardCategoryJob(@Qualifier("initBoardCategoryStep") Step initBoardCategoryStep) {
        return jobBuilderFactory.get("initBoardCategoryJob")
                .incrementer(new RunIdIncrementer())
                .start(initBoardCategoryStep)
                .build();
    }

    @Bean
    public Step initBoardCategoryStep() {
        return stepBuilderFactory.get("initBoardCategoryStep")
                .tasklet((contribution, chunkContext) -> {
                    boardCategoryService.initBoardCategory();
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
