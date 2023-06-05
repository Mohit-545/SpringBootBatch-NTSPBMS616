//IExamResult.java
package com.nt.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class IExamResult {
	@Id
	private Integer id;
	private String dob;
	private Integer semester;
	private Float percentage;

}//class
