package com.jakduk.api.model.db;

import com.jakduk.api.common.CoreConst;
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

	@Id
	private String id;
	
	private String name;

	private CoreConst.CLUB_TYPE clubType;

	private CoreConst.CLUB_AGE age;

	private CoreConst.CLUB_SEX sex;
}
