//OExamResult.java
package com.nt.document;



import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class OExamResult {
	@Id
	private Integer id;
	private LocalDateTime dob;
	private Integer semester;
	private Float percentage;

}//class
