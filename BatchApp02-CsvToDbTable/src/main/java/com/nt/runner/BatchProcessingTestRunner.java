//BatchProcessingTestRunner.java
package com.nt.runner;



import java.util.Random;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BatchProcessingTestRunner implements CommandLineRunner {
	@Autowired
	private JobLauncher launcher;
	@Autowired
	private Job job;

	@Override
	public void run(String... args) throws Exception {
		//prepare Job Parameters
		JobParameters params = new JobParametersBuilder().addLong("run.id", new Random().nextLong(1000)).toJobParameters();
		//run the Job
		JobExecution execution = launcher.run(job, params);
		System.out.println("Job Execution Status :: "+execution.getStatus());
		System.out.println("Job Exit Status :: "+execution.getExitStatus());
		System.out.println("Job Id :: "+execution.getJobId());
	}//method

}//class
