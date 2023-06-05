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

import com.nt.document.OExamResult;
import com.nt.listener.JobMonitoringListener;
import com.nt.model.IExamResult;
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
	public FlatFileItemReader<IExamResult> createReader(){
		return new FlatFileItemReaderBuilder<IExamResult>()
											.name("file-reader")
											.resource(new FileSystemResource("TopperStudents.csv"))
											.delimited().delimiter(",")
											.names("id","dob","percentage","semester")
											.targetType(IExamResult.class)
											.build();
	}//method
	
	@Bean(name = "mir")
	public MongoItemWriter<OExamResult> createWriter(){
		MongoItemWriter<OExamResult> writer = new MongoItemWriter<OExamResult>();
		writer.setCollection("TopperStudents");
		writer.setTemplate(template);
		return writer;
	}//method
	
	//create Step object using StepBuilderFactory
	@Bean("step1")
	public Step createStep1() {
		System.out.println("BatchConfig.createStep1()");
		return stepFactory.get("step1")
				.<IExamResult,OExamResult>chunk(50)
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
