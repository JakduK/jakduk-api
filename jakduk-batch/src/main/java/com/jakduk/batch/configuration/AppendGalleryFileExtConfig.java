package com.jakduk.batch.configuration;

import com.jakduk.batch.processor.AppendGalleryFileExtProcessor;
import com.jakduk.core.common.CommonConst;
import com.jakduk.core.model.db.Gallery;
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
 * 사진첩의 사진 파일에 확장자를 추가한다.
 *
 * Created by pyohwan on 16. 10. 4.
 */

@Configuration
public class AppendGalleryFileExtConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private MongoOperations mongoOperations;

    @Bean
    public Job appendGalleryFileExtJob(@Qualifier("appendGalleryFileExtStep") Step step1) throws Exception {

        return jobBuilderFactory.get("appendGalleryFileExtJob")
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .build();
    }

    @Bean
    public Step appendGalleryFileExtStep() {
        return stepBuilderFactory.get("appendGalleryFileExtStep")
                .<Gallery, Gallery>chunk(1000)
                .reader(appendGalleryFileExtReader())
                .processor(appendGalleryFileExtProcessor())
                .writer(appendGalleryFileExtWriter())
                .build();
    }

    @Bean
    public ItemReader<Gallery> appendGalleryFileExtReader() {

        String query = String.format("{'status.status':'%s', 'batch':{$nin:['%s']}}",
                CommonConst.GALLERY_STATUS_TYPE.ENABLE, CommonConst.BATCH_TYPE.APPEND_GALLERY_FILE_EXT_01);

        MongoItemReader<Gallery> itemReader = new MongoItemReader<>();
        itemReader.setTemplate(mongoOperations);
        itemReader.setTargetType(Gallery.class);
        itemReader.setPageSize(100);
        itemReader.setQuery(query);

        Map<String, Sort.Direction> sorts = new HashMap<>();
        sorts.put("id", Sort.Direction.DESC);
        itemReader.setSort(sorts);

        return itemReader;
    }

    @Bean
    public ItemProcessor<Gallery, Gallery> appendGalleryFileExtProcessor() {
        return new AppendGalleryFileExtProcessor();
    }

    @Bean
    public MongoItemWriter<Gallery> appendGalleryFileExtWriter() {
        MongoItemWriter<Gallery> writer = new MongoItemWriter<>();
        writer.setTemplate(mongoOperations);

        return writer;
    }
}
