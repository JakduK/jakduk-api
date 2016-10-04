package com.jakduk.batch.configuration;

import com.jakduk.batch.processor.ChangeBoardImageUrlProcessor;
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
 * 게시물 본문에 이미지 경로가 상대경로였는데, 절대경로로 바꾼다.
 *
 * @author Jang, Pyohwan
 * @since 2016. 9. 5.
 */

@Configuration
public class ChangeBoardImageUrlConfig {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private MongoOperations mongoOperations;

	@Bean
	public Job changeBoardImageUrlJob01(@Qualifier("changeBoardImageUrlStep01") Step step1) throws Exception {

		return jobBuilderFactory.get("changeBoardImageUrlJob01")
				.incrementer(new RunIdIncrementer())
				.start(step1)
				.build();
	}

	@Bean
	public Step changeBoardImageUrlStep01() {
		return stepBuilderFactory.get("changeBoardImageUrlStep01")
				.<BoardFree, BoardFree>chunk(1000)
				.reader(changeBoardImageUrlReader())
				.processor(changeBoardImageUrlProcessor())
				.writer(changeBoardImageUrlWriter())
				.build();
	}

	@Bean
	public ItemReader<BoardFree> changeBoardImageUrlReader() {
		MongoItemReader<BoardFree> itemReader = new MongoItemReader<>();
		itemReader.setTemplate(mongoOperations);
		itemReader.setTargetType(BoardFree.class);
		itemReader.setPageSize(100);
		itemReader.setQuery("{'galleries':{$exists:true}, 'batch':{$nin:['CHANGE_BOARD_CONTENT_IMAGE_URL_01']}}");
		Map<String, Sort.Direction> sorts = new HashMap<>();
		sorts.put("id", Sort.Direction.ASC);
		itemReader.setSort(sorts);

		return itemReader;
	}

	@Bean
	public ItemProcessor<BoardFree, BoardFree> changeBoardImageUrlProcessor() {
		return new ChangeBoardImageUrlProcessor();
	}

	@Bean
	public MongoItemWriter<BoardFree> changeBoardImageUrlWriter() {
		MongoItemWriter<BoardFree> writer = new MongoItemWriter<>();
		writer.setTemplate(mongoOperations);

		return writer;
	}
}
