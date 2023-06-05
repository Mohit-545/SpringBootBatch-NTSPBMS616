//IExamResult.java
package com.nt.model;


import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IExamResult {
	@Id
	private Integer id;
	private String dob;
	private Integer semester;
	private Float percentage;

}//class
