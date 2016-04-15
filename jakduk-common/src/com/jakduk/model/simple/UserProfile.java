package com.jakduk.model.simple;

import java.util.List;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.jakduk.model.db.FootballClub;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 10. 6.
 * @desc     :
 */

@Data
@Document(collection = "user")
public class UserProfile {

	@Id
	private String id;

	private String email;

	private String username;

	private String about;

	@DBRef
	private FootballClub supportFC;
}
