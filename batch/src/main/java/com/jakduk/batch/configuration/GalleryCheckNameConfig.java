package com.jakduk.batch.configuration;

import com.jakduk.batch.processor.GalleryCheckNameProcessor;
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
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;

import java.util.HashMap;
import java.util.Map;

/**
 * Gallery 의 name이 fileName과 동일하면 ""로 엎어친다.
 *
 * Created by pyohwanjang on 2017. 4. 21..
 */
public class GalleryCheckNameConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private MongoOperations mongoOperations;

    @Bean
    public Job galleryCheckNameJob(@Qualifier("galleryCheckNameStep") Step step) {

        return jobBuilderFactory.get("galleryCheckNameJob")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

    @Bean
    public Step galleryCheckNameStep() {
        return stepBuilderFactory.get("galleryCheckNameStep")
                .<Gallery, Gallery>chunk(1000)
                .reader(galleryCheckNameReader())
                .processor(galleryCheckNameProcessor())
                .writer(galleryCheckNameWriter())
                .build();
    }

    @Bean
    public ItemReader<Gallery> galleryCheckNameReader() {

        String query = String.format("{'status.status':'%s', 'batch':{$nin:['%s']}}",
                CoreConst.GALLERY_STATUS_TYPE.ENABLE, CoreConst.BATCH_TYPE.GALLERY_CHECK_NAME_01);

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
    public ItemProcessor<Gallery, Gallery> galleryCheckNameProcessor() {
        return new GalleryCheckNameProcessor();
    }

    @Bean
    public MongoItemWriter<Gallery> galleryCheckNameWriter() {
        MongoItemWriter<Gallery> writer = new MongoItemWriter<>();
        writer.setTemplate(mongoOperations);

        return writer;
    }
}
