//BatchConfig.java
package com.nt.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.nt.listener.JobMonitoringListener;
import com.nt.model.EmployeeInfo;
import com.nt.processor.EmployeeInfoItemProcessor;


@Configuration
@EnableBatchProcessing
public class BatchConfig {
	@Autowired
	private JobBuilderFactory jobFactory;
	@Autowired
	private StepBuilderFactory stepFactory;
	@Autowired
	private JobMonitoringListener jobListener;
	@Autowired
	private EmployeeInfoItemProcessor empProcessor;
	@Autowired
	private DataSource ds;
	
	/*@Bean(name="ffiReader")
	public FlatFileItemReader<EmployeeInfo> createFFIReader(){
		//create Reader Object
		FlatFileItemReader<EmployeeInfo> reader = new FlatFileItemReader<EmployeeInfo>();
		//set CSV file as resource
		reader.setResource(new ClassPathResource("EmployeeInfo.csv"));
	
		//create LineMapperObject (to get each line from csv file)
		DefaultLineMapper<EmployeeInfo> mapper = new DefaultLineMapper<EmployeeInfo>();
		
		//create LineTokenizer to get tokens from lines
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setDelimiter(",");
		tokenizer.setNames("empno","ename","job","salary");
		
		//create FieldSetMapper to set the tokens to Model class objects properties
		BeanWrapperFieldSetMapper<EmployeeInfo> fieldSetMapper = new BeanWrapperFieldSetMapper<EmployeeInfo>();
		fieldSetMapper.setTargetType(EmployeeInfo.class);
		
		//set Tokenizer, fieldSetMapper objects to LineMapper
		mapper.setLineTokenizer(tokenizer);
		mapper.setFieldSetMapper(fieldSetMapper);
		
		//set LineMapper to Reader object
		reader.setLineMapper(mapper);
		
		return reader;
		
	}//method
	*/	
	/*@Bean(name = "jbiw")
	public JdbcBatchItemWriter<EmployeeInfo> createJBIWriter(){
		//create jdbcBatchItemWriter
		JdbcBatchItemWriter<EmployeeInfo> writer = new JdbcBatchItemWriter<EmployeeInfo>();
		//set DataSource
		writer.setDataSource(ds);
		//set Insert SQL Query with named Parameters
		writer.setSql("INSERT INTO BATCH_EMPLOYEEINFO VALUES(:empno, :ename, :job, :salary, :grossSalary, :netSalary)");
		//create BeanPropertyItemSqlParameterSourceProviderObject
		BeanPropertyItemSqlParameterSourceProvider<EmployeeInfo> sourceProvider = new BeanPropertyItemSqlParameterSourceProvider<EmployeeInfo>();
		//set SourceProver to write object
		writer.setItemSqlParameterSourceProvider(sourceProvider);
		return writer;
	}//method	*/	

	//Using Anonymous inner Class and instance block concept to create FlatFileItemReader and JdbcBatchItemReader objects
	/*@Bean(name="ffiReader")
	public FlatFileItemReader<EmployeeInfo> createFFIReader(){
		//create Reader object
		FlatFileItemReader<EmployeeInfo> reader = new FlatFileItemReader<EmployeeInfo>();
		
		//set csv file as resource
		reader.setResource(new ClassPathResource("EmployeeInfo.csv"));
		
		//set LineMapper object	(by creating anonymous inner class and instance block to set tokenizer and fieldSetMapper)
		reader.setLineMapper(new DefaultLineMapper<EmployeeInfo>() {{
			//set DelimitedTokenizer object
			setLineTokenizer(new DelimitedLineTokenizer() {{
					setDelimiter(",");
					setNames("empno", "ename", "job", "salary");
			}});
			
			setFieldSetMapper(new BeanWrapperFieldSetMapper<EmployeeInfo>() {{
					setTargetType(EmployeeInfo.class);
			}});
		}});
		return reader;
	}//method
	*/	
	/*@Bean(name="jbiw")
	public JdbcBatchItemWriter<EmployeeInfo> createJBIWriter(){
		//create jdbcBatchItemWriter object
		JdbcBatchItemWriter<EmployeeInfo> writer = new JdbcBatchItemWriter<EmployeeInfo>(){{
				//set DataSource
				setDataSource(ds);
				setSql("INSERT INTO BATCH_EMPLOYEEINFO VALUES(:empno, :ename, :job, :salary, :grossSalary, :netSalary)");
				setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<EmployeeInfo>());
		}};
		return writer;
	}//method
	*/	
	
	//creating reader and writer object using builder classes using Builder Design Pattern with Method Chaining (industry standard)
	@Bean(name="ffiReader")
	public FlatFileItemReader<EmployeeInfo> createFFIReader(){
		return new FlatFileItemReaderBuilder<EmployeeInfo>()
									.name("file-reader")
									.resource(new ClassPathResource("EmployeeInfo.csv"))
									.delimited()
									.names("empno","ename","job","salary")
									.targetType(EmployeeInfo.class)
									.build();
	}//method
	
	@Bean(name="jbiw")
	public JdbcBatchItemWriter<EmployeeInfo> createJBIWriter(){
		return new JdbcBatchItemWriterBuilder<EmployeeInfo>()
										.dataSource(ds)
										.sql("INSERT INTO BATCH_EMPLOYEEINFO VALUES(:empno, :ename, :job, :salary, :grossSalary, :netSalary)")
										.beanMapped()
										.build();
	}//method
	
	//create Step object using StepBuilderFactory
	@Bean("step1")
	public Step createStep1() {
		System.out.println("BatchConfig.createStep1()");
		return stepFactory.get("step1")
				.<EmployeeInfo,EmployeeInfo>chunk(5)
				.reader(createFFIReader())
				.processor(empProcessor)
				.writer(createJBIWriter())
				.build();
	}//method
	
	@Bean("job1")
	public Job createJob() {
		System.out.println("BatchConfig.createJob()");
		return jobFactory.get("job1")
										.incrementer(new RunIdIncrementer())
										.listener(jobListener)
										.start(createStep1())
										.build();
	}//method

}//class
