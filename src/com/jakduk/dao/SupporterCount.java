package com.jakduk.dao;

import org.springframework.data.mongodb.core.mapping.Document;

import com.jakduk.model.db.FootballClub;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 2. 16.
 * @desc     :
 */

@Document
public class SupporterCount {
	
	private FootballClub id;
	
	private Integer count;

	public FootballClub getId() {
		return id;
	}

	public void setId(FootballClub id) {
		this.id = id;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "SupporterCount [id=" + id + ", count=" + count + "]";
	}

}
