package com.jakduk.model.web;

import org.springframework.data.annotation.Id;


/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 9. 17.
 * @desc     :
 */
public class FootballClubWrite {
	
	@Id
	private String id;
	
	private String origin;

	private String active;
	
	private String shortNameKr;
	
	private String fullNameKr;
	
	private String shortNameEn;
	
	private String fullNameEn;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}
	
	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getShortNameKr() {
		return shortNameKr;
	}

	public void setShortNameKr(String shortNameKr) {
		this.shortNameKr = shortNameKr;
	}

	public String getFullNameKr() {
		return fullNameKr;
	}

	public void setFullNameKr(String fullNameKr) {
		this.fullNameKr = fullNameKr;
	}

	public String getShortNameEn() {
		return shortNameEn;
	}

	public void setShortNameEn(String shortNameEn) {
		this.shortNameEn = shortNameEn;
	}

	public String getFullNameEn() {
		return fullNameEn;
	}

	public void setFullNameEn(String fullNameEn) {
		this.fullNameEn = fullNameEn;
	}

	@Override
	public String toString() {
		return "FootballClubWrite [id=" + id + ", origin=" + origin
				+ ", active=" + active + ", shortNameKr=" + shortNameKr
				+ ", fullNameKr=" + fullNameKr + ", shortNameEn=" + shortNameEn
				+ ", fullNameEn=" + fullNameEn + "]";
	}

}
