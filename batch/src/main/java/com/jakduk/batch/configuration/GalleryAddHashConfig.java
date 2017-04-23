package com.jakduk.batch.configuration;

import com.jakduk.batch.processor.GalleryAddHashProcessor;
import com.jakduk.core.common.CoreConst;
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
 * Gallery에 hash 필드 추가. 중복 검사할때 쓰임
 *
 * Created by pyohwanjang on 2017. 4. 11..
 */

@Configuration
public class GalleryAddHashConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private MongoOperations mongoOperations;

    @Bean
    public Job galleryAddHashJob(@Qualifier("galleryAddHashStep") Step step) {

        return jobBuilderFactory.get("galleryAddHashJob")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

    @Bean
    public Step galleryAddHashStep() {
        return stepBuilderFactory.get("galleryAddHashStep")
                .<Gallery, Gallery>chunk(1000)
                .reader(galleryAddHashReader())
                .processor(galleryAddHashProcessor())
                .writer(galleryAddHashWriter())
                .build();
    }

    @Bean
    public ItemReader<Gallery> galleryAddHashReader() {

        String query = String.format("{'status.status':'%s', 'batch':{$nin:['%s']}}",
                CoreConst.GALLERY_STATUS_TYPE.ENABLE, CoreConst.BATCH_TYPE.GALLERY_ADD_HASH_FIELD_01);

        MongoItemReader<Gallery> itemReader = new MongoItemReader<>();
        itemReader.setTemplate(mongoOperations);
        itemReader.setTargetType(Gallery.class);
        itemReader.setPageSize(500);
        itemReader.setQuery(query);
        Map<String, Sort.Direction> sorts = new HashMap<>();
        sorts.put("id", Sort.Direction.ASC);
        itemReader.setSort(sorts);

        return itemReader;
    }

    @Bean
    public ItemProcessor<Gallery, Gallery> galleryAddHashProcessor() {
        return new GalleryAddHashProcessor();
    }

    @Bean
    public MongoItemWriter<Gallery> galleryAddHashWriter() {
        MongoItemWriter<Gallery> writer = new MongoItemWriter<>();
        writer.setTemplate(mongoOperations);

        return writer;
    }
}
