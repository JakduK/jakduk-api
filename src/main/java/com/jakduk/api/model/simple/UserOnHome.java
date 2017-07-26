package com.jakduk.api.model.simple;

import com.jakduk.api.model.db.FootballClub;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 2.
 * @desc     :
 */

@NoArgsConstructor
@Getter
@Setter
@Document(collection = "user")
public class UserOnHome {
	
	@Id
	private String id;
	
	private String username;
	
	private String about;
	
	private FootballClub supportFC;
	
}
