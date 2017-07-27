package com.jakduk.api.model.db;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 3. 18.
 * @desc     :
 */

@NoArgsConstructor
@Getter
@Setter
@Document
public class AttendanceClub {
	
	/**
	 * ID
	 */
	@Id
	private String id;
	
	@DBRef
	private FootballClubOrigin club;
	
	private String league;
	
	private Integer season;
	
	private Integer games;
	
	private Integer total;
	
	private Integer average;

}
