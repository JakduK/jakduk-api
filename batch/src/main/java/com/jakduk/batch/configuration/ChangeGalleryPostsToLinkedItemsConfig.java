package com.jakduk.batch.configuration;

import com.jakduk.batch.processor.ChangeGalleryPostsToLinkedItemsProcessor;
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
public class ChangeGalleryPostsToLinkedItemsConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private MongoOperations mongoOperations;

    @Bean
    public Job changeGalleryPostsToLinkedItemsJob01(@Qualifier("changeGalleryPostsToLinkedItems01") Step step1) throws Exception {

        return jobBuilderFactory.get("changeGalleryPostsToLinkedItemsJob01")
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .build();
    }

    @Bean
    public Step changeGalleryPostsToLinkedItems01() {
        return stepBuilderFactory.get("changeGalleryPostsToLinkedItems01")
                .<Gallery, Gallery>chunk(1000)
                .reader(changeGalleryPostsToLinkedItemsReader())
                .processor(changeGalleryPostsToLinkedItemsProcessor())
                .writer(changeGalleryPostsToLinkedItemsWriter())
                .build();
    }

    @Bean
    public ItemReader<Gallery> changeGalleryPostsToLinkedItemsReader() {

        String query = String.format("{'status.status':'%s', 'batch':{$nin:['%s']}}",
                CoreConst.GALLERY_STATUS_TYPE.ENABLE, CoreConst.BATCH_TYPE.CHANGE_GALLERY_POSTS_TO_LINKED_ITEMS_01);

        MongoItemReader<Gallery> itemReader = new MongoItemReader<>();
        itemReader.setTemplate(mongoOperations);
        itemReader.setTargetType(Gallery.class);
        itemReader.setPageSize(100);
        itemReader.setQuery(query);
        Map<String, Sort.Direction> sorts = new HashMap<>();
        sorts.put("id", Sort.Direction.ASC);
        itemReader.setSort(sorts);

        return itemReader;
    }

    @Bean
    public ItemProcessor<Gallery, Gallery> changeGalleryPostsToLinkedItemsProcessor() {
        return new ChangeGalleryPostsToLinkedItemsProcessor();
    }

    @Bean
    public MongoItemWriter<Gallery> changeGalleryPostsToLinkedItemsWriter() {
        MongoItemWriter<Gallery> writer = new MongoItemWriter<>();
        writer.setTemplate(mongoOperations);

        return writer;
    }

}
