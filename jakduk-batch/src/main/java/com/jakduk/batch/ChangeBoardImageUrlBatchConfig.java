package com.jakduk.batch;

import com.jakduk.batch.processor.ChangeBoardImageUrlProcessor;
import com.jakduk.core.model.db.BoardFree;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jang, Pyohwan
 * @since 2016. 9. 5.
 */

@Configuration
public class ChangeBoardImageUrlBatchConfig {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private MongoOperations mongoOperations;

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("changeBoardImageUrlStep01")
				.<BoardFree, BoardFree>chunk(1000)
				.reader(reader())
				.processor(processor())
				.writer(writer())
				.build();
	}

	@Bean
	public Job job(Step step1) throws Exception {
		return jobBuilderFactory.get("changeBoardImageUrlJob01")
				.incrementer(new RunIdIncrementer())
				.start(step1)
				.build();
	}

	@Bean
	public ItemReader<BoardFree> reader() {
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
	public ItemProcessor<BoardFree, BoardFree> processor() {
		return new ChangeBoardImageUrlProcessor();
	}

	@Bean
	public ItemWriter<BoardFree> writer() {
		MongoItemWriter writer = new MongoItemWriter();
		writer.setTemplate(mongoOperations);

		return writer;
	}
}
