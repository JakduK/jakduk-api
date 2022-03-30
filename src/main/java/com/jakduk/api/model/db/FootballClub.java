package com.jakduk.api.model.db;

import com.jakduk.api.model.embedded.LocalName;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 9. 11.
 * @desc     :
 */

@Document
public class FootballClub {

	@Id
	private String id;
	@DBRef
	private FootballClubOrigin origin;
	private String active;
	private List<LocalName> names;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public FootballClubOrigin getOrigin() {
		return origin;
	}

	public void setOrigin(FootballClubOrigin origin) {
		this.origin = origin;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public List<LocalName> getNames() {
		return names;
	}

	public void setNames(List<LocalName> names) {
		this.names = names;
	}
}
