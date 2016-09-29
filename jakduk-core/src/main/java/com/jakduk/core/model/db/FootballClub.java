package com.jakduk.core.model.db;

import com.jakduk.core.model.embedded.LocalName;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.List;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 9. 11.
 * @desc     :
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document
public class FootballClub {

	@Id  @GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	
	@DBRef
	private FootballClubOrigin origin;

	private String active;
	
	private List<LocalName> names;
}
