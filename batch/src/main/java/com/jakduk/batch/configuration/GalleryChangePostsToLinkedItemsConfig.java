package com.jakduk.batch.configuration;

import com.jakduk.batch.processor.GalleryChangePostsToLinkedItemsProcessor;
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
 * Gallery의 posts를 linkedItems으로 바꾼다. 기존에는 글만 저장했는데 이제는 댓글까지 확장하기 위함이다.
 *
 * Created by pyohwanjang on 2017. 4. 11..
 */

@Configuration
public class GalleryChangePostsToLinkedItemsConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private MongoOperations mongoOperations;

    @Bean
    public Job galleryChangePostsToLinkedItemsJob(@Qualifier("galleryChangePostsToLinkedItemsStep") Step step) {

        return jobBuilderFactory.get("galleryChangePostsToLinkedItemsJob")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

    @Bean
    public Step galleryChangePostsToLinkedItemsStep() {
        return stepBuilderFactory.get("galleryChangePostsToLinkedItemsStep")
                .<Gallery, Gallery>chunk(1000)
                .reader(galleryChangePostsToLinkedItemsReader())
                .processor(galleryChangePostsToLinkedItemsProcessor())
                .writer(galleryChangePostsToLinkedItemsWriter())
                .build();
    }

    @Bean
    public ItemReader<Gallery> galleryChangePostsToLinkedItemsReader() {

        String query = String.format("{'status.status':'%s', 'batch':{$nin:['%s']}}",
                CoreConst.GALLERY_STATUS_TYPE.ENABLE, CoreConst.BATCH_TYPE.GALLERY_CHANGE_POSTS_TO_LINKED_ITEMS_01);

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
    public ItemProcessor<Gallery, Gallery> galleryChangePostsToLinkedItemsProcessor() {
        return new GalleryChangePostsToLinkedItemsProcessor();
    }

    @Bean
    public MongoItemWriter<Gallery> galleryChangePostsToLinkedItemsWriter() {
        MongoItemWriter<Gallery> writer = new MongoItemWriter<>();
        writer.setTemplate(mongoOperations);

        return writer;
    }

}
