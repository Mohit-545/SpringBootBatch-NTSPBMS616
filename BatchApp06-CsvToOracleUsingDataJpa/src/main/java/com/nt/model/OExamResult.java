//OExamResult.java
package com.nt.model;



import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "EXAM_RESULT")
public class OExamResult {
	@Id
	private Integer id;
	private LocalDateTime dob;
	private Integer semester;
	private Float percentage;

}//class
