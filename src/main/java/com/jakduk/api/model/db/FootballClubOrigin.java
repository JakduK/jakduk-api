package com.jakduk.api.model.db;

import com.jakduk.api.common.Constants;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 9. 28.
 * @desc     :
 */

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Document
public class FootballClubOrigin {

	@Id @Setter
	private String id;
	private String name;
	private Constants.CLUB_TYPE clubType;
	private Constants.CLUB_AGE age;
	private Constants.CLUB_SEX sex;

}
