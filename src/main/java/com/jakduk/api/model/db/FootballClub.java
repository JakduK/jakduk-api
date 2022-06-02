package com.jakduk.api.model.db;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.jakduk.api.model.embedded.LocalName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 9. 11.
 * @desc     :
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document
public class FootballClub {

	@Id
	private String id;
	@DBRef
	private FootballClubOrigin origin;
	private String active;
	private List<LocalName> names;

}
