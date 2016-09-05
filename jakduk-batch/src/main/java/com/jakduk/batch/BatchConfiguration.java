package com.jakduk.batch;

import com.jakduk.batch.model.BoardFree;
import com.jakduk.batch.processor.MyItemProcessor;
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
 * @author Jang, Pyohwan(1100273)
 * @since 2016. 9. 5.
 */

@Configuration
public class BatchConfiguration {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private MongoOperations mongoOperations;

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
				.<BoardFree, BoardFree>chunk(1000)
				.reader(reader())
				.processor(processor())
//				.writer(writer())
				.build();
	}

	@Bean
	public Job job(Step step1) throws Exception {
		return jobBuilderFactory.get("job1")
				.incrementer(new RunIdIncrementer())
				.start(step1)
				.build();
	}

	@Bean
	public ItemReader<BoardFree> reader() {
		//mongoOperations.find(Query.query(Criteria.where("firstName").exists(true)), Account.class);

		MongoItemReader<BoardFree> itemReader = new MongoItemReader<>();
		itemReader.setTemplate(mongoOperations);
		itemReader.setTargetType(BoardFree.class);
		itemReader.setPageSize(100);
		itemReader.setQuery("{}");
		Map<String, Sort.Direction> sorts = new HashMap<>();
		sorts.put("id", Sort.Direction.ASC);
		itemReader.setSort(sorts);

		return itemReader;
	}

	@Bean
	public ItemProcessor<BoardFree, BoardFree> processor() {
		return new MyItemProcessor();
	}

	@Bean
	public ItemWriter<BoardFree> writer() {
		MongoItemWriter writer = new MongoItemWriter();
		writer.setTemplate(mongoOperations);

		return writer;
	}
}
