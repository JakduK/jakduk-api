package com.jakduk.model.db;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import com.jakduk.common.CommonConst;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 9. 28.
 * @desc     :
 */

@Data
@Document
public class FootballClubOrigin {

	@Id  @GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	
	private String name;

	private CommonConst.CLUB_TYPE clubType;

	private CommonConst.CLUB_AGE age;

	private CommonConst.CLUB_SEX sex;
}
