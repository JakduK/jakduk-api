package com.jakduk.api.model.db;

import com.jakduk.api.common.Constants;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 9. 28.
 * @desc     :
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document
public class FootballClubOrigin {

	@Id
	private String id;
	private String name;
	private Constants.CLUB_TYPE clubType;
	private Constants.CLUB_AGE age;
	private Constants.CLUB_SEX sex;
}
