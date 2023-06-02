//EmployeeInfoItemProcessor.java
package com.nt.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.nt.model.EmployeeInfo;

@Component
public class EmployeeInfoItemProcessor implements ItemProcessor<EmployeeInfo, EmployeeInfo> {

	@Override
	public EmployeeInfo process(EmployeeInfo emp) throws Exception {
		if(emp.getSalary()>=5000) {
			emp.setGrossSalary((float)Math.round(emp.getSalary()+emp.getSalary()*0.4f));
			emp.setNetSalary((float)Math.round(emp.getGrossSalary()-emp.getGrossSalary()*0.2f));
			return emp;
		}//if
		else
		return null;
	}//method

}//class
