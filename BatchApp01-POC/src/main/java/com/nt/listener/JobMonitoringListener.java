//JobMonitoringListener.java
package com.nt.listener;

import java.util.Date;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component("jmListener")
public class JobMonitoringListener implements JobExecutionListener {
	private Long startTime, endTime;
	
	public JobMonitoringListener() {
		System.out.println("JobMonitoringListener :: 0-param constructor");
	}//constructor
	
	@Override
	public void beforeJob(JobExecution jobExecution) {
		System.out.println("Job started at :: "+new Date());
		startTime = System.currentTimeMillis();
		System.out.println("job status :: "+jobExecution.getStatus());
	}//method

	@Override
	public void afterJob(JobExecution jobExecution) {
		System.out.println("job ended at :: "+new Date());
		endTime = System.currentTimeMillis();
		System.out.println("Job Status :: "+jobExecution.getStatus());
		System.out.println("Job Execution Time :: "+(endTime-startTime));
		System.out.println("Job Exit Status :: "+jobExecution.getExitStatus());
	}//method

}//class
