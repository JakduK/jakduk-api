package com.jakduk.model.db;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotNull;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 3. 10.
 * @desc     :
 */

@Data
@Document
public class AttendanceLeague {
	
	/**
	 * ID
	 */
	@Id  @GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	
	@NotEmpty
	private String league;
	
	@NotNull
	private Integer season;
	
	@NotNull
	private Integer games;
	
	@NotNull
	private Integer total;
	
	@NotNull
	private Integer average;
	
	@NotNull
	private Integer numberOfClubs;
}
