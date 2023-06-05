//BatchConfig.java
package com.nt.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.nt.document.ExamResult;
import com.nt.listener.JobMonitoringListener;
import com.nt.processor.ExamResultItemProcessor;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
	@Autowired
	private JobBuilderFactory jobFactory;
	@Autowired
	private StepBuilderFactory stepFactory;
	@Autowired
	private JobMonitoringListener listener;
	@Autowired
	private MongoTemplate template;
	@Autowired
	private ExamResultItemProcessor processor;
	
	@Bean(name = "ffir")
	public FlatFileItemReader<ExamResult> createReader(){
		return new FlatFileItemReaderBuilder<ExamResult>()
											.name("file-reader")
											.resource(new FileSystemResource("TopperStudents.csv"))
											.delimited().delimiter(",")
											.names("id","dob","percentage","semester")
											.targetType(ExamResult.class)
											.build();
	}//method
	
	@Bean(name = "mir")
	public MongoItemWriter<ExamResult> createWriter(){
		MongoItemWriter<ExamResult> writer = new MongoItemWriter<ExamResult>();
		writer.setCollection("SuperBrains");
		writer.setTemplate(template);
		return writer;
	}//method
	
	//create Step object using StepBuilderFactory
	@Bean("step1")
	public Step createStep1() {
		System.out.println("BatchConfig.createStep1()");
		return stepFactory.get("step1")
				.<ExamResult,ExamResult>chunk(50)
				.reader(createReader())
				.processor(processor)
				.writer(createWriter())
				.build();
	}//method
	
	@Bean(name = "job1")
	public Job createJob1() {
		return jobFactory.get("job1")
							.listener(listener)
							.incrementer(new RunIdIncrementer())
							.start(createStep1())
							.build();
	}//method

}//class
