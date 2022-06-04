package com.jakduk.api.model.db;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 3. 10.
 * @desc     :
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document
public class AttendanceLeague {

	@Id
	private String id;
	@DBRef
	private Competition competition;
	private Integer season;
	private Integer games;
	private Integer total;
	private Integer average;
	private Integer numberOfClubs;

}
